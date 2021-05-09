package simulator.view;

import javax.swing.*;
import org.json.JSONObject;
import java.awt.Frame;

import simulator.control.Controller;
import simulator.factories.Factory;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;


public class SelectionDialog<T> extends JDialog {

	protected int _status;

	private JComboBox<String> _CBox;
	private List<JSONObject> _info;
	private static int IntitialItemCBox = 0; //by default selection on ComboBox is item nr 0

	private String _title;
	private String _instructions;
	
	private SelectionDialogTable _table;
	private Controller _ctrl;

	private Factory<T> _factory; //TODO do we need an attribute?


	//TODO generalize:
	//public SelectionDialog(Factory factory, Frame parent, String title, String instructions) {
	public SelectionDialog(Controller ctrl, Factory<T> factory, Frame parent){
		super(parent, true);
		_info = new ArrayList<>(factory.getInfo()); 
		_ctrl = ctrl;
		_title = "Force Laws Selection"; //TODO to generalize, act as parameter in constructor
		_instructions = "Select a force law and provide values for the parameters in the 'Value' column" //TODO same
		 				+ "(default values are used for parameters with no user defined value)";
		_factory = factory;
		initGUI();
	}

	private void setComboBoxNames() {
		String[] names = new String[_info.size()];
		int i = 0;
		for (JSONObject o : _info) {
			names[i] = o.getString("desc");
			++i;
		}
		_CBox = new JComboBox<String>(names);
		_CBox.setSelectedIndex(IntitialItemCBox);
		/* _CBox.addActionListener((e) -> 
			{
				String name = _CBox.getSelectedItem().toString();
				newSelection = _info.get(searchNameInInfo(name));
				_table.updateData(newSelection);
			}
		);
		} */ //TODO choose one
		_CBox.addActionListener( (e) ->  
			{
				JComboBox<String> comboBox = (JComboBox<String>) e.getSource(); //TODO why is it needed?
				int index_in_info_list = searchNameInInfo( (String) comboBox.getSelectedItem() );
				
				//copy into newSelection the data of the selected item  
				JSONObject newSelection = new JSONObject();
				
				if(JSONObject.getNames(_info.get(index_in_info_list).getJSONObject("data")) != null){ // if it has some keys to retrieve
					for(String key: JSONObject.getNames(_info.get(index_in_info_list).getJSONObject("data")))
						newSelection.put(key, _info.get(index_in_info_list).getJSONObject("data").getString(key));
				}
				_table.updateData(newSelection);
			} );
	}

	// returns the index which the item with description == 'name' has in the info list
	private int searchNameInInfo(String name) {
		boolean found = false;
		int i = 0;
		while(i < _info.size() && !found){
			if(_info.get(i).getString("desc").equals(name))
				found = true;
			else		
				++i;
		};
		assert(i < _info.size());//name will always be one contained in the JList so it'll be one in the info
		return i;
	}

	/* we return a JSONObject with the info necessary to create an instance of the item being selected, 
	including the 'data' section according to the values the user has introduced in the table.
	
	Afterwards, this JSONObject will be passed to the controller to create a new force law */
	private JSONObject getData(){
		//get whole JSONObject corresponding to info necessary to create an instance of the item being selected
		JSONObject newSelection = new JSONObject(_info.get(searchNameInInfo(getForceName())));
		
		//add the 'data' section according to the table's values
		newSelection.put("data", _table.getData());
        
		return newSelection;
	}

	private void initGUI() {

		setTitle(_title); 
		JPanel mainPanel = new JPanel(new BorderLayout());

		// PAGE_START
		JPanel topPanel = new JPanel();
		topPanel.add(new JLabel(_instructions));
		mainPanel.add(topPanel, BorderLayout.PAGE_START);

		// CENTER
		JPanel centerP = new JPanel();
		centerP.setLayout(new BoxLayout(centerP, BoxLayout.Y_AXIS));

			//table:
		/*  TODO is it needed?
		JScrollPane scrollTable = new JScrollPane(_table);
		scrollTable.setPreferredSize(new Dimension(300, 250));
		center.add(scrollTable); 
		*/
		_table = new SelectionDialogTable(IntitialItemCBox, _info.get(IntitialItemCBox).getJSONObject("data"));
		centerP.add(_table); //TODO , BorderLayout.PAGE_START

			//combo box:
		setComboBoxNames();
		JPanel forcesCBoxPanel = new JPanel();
		forcesCBoxPanel.add(_CBox, "Select one: ");
		forcesCBoxPanel.setPreferredSize(new Dimension(300, 50));
		centerP.add(forcesCBoxPanel);

		mainPanel.add(centerP, BorderLayout.CENTER);

		//PAGE_END:
			//buttons:

		JPanel buttonsPanel = new JPanel();

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(new Dimension(80, 20)); 
		cancelButton.addActionListener((e) -> {
			_status = 0;
			this.setVisible(false);
		});
		buttonsPanel.add(cancelButton);

		JButton OKButton = new JButton("OK");
		OKButton.setPreferredSize(new Dimension(80, 20)); 
		OKButton.addActionListener((e) -> {
			_status = 1;
			/* once selected, change the force laws of the simulator to the chosen one */
			_ctrl.setForceLaws(getData()); //TODO move to controller atthe end of ldForcesButton actionlistener ?
			this.setVisible(false);
		});
		buttonsPanel.add(OKButton);

		mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);


		setContentPane(mainPanel);
		setMinimumSize(new Dimension(100, 100));
		setVisible(false);
	}

	public String getForceName() {
		return (String) _CBox.getSelectedItem();
	}


	public int open() {
		setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 50);// TODO ok ?
		pack();
		setVisible(true);
		return _status;
	}

}

