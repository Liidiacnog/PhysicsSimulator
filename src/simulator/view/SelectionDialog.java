package simulator.view;

import javax.swing.*;
import org.json.JSONObject;

import simulator.control.Controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectionDialog extends JDialog implements ActionListener {

	protected int _status;//TODO what for?

	private JComboBox<String> _CBox;
	private List<JSONObject> _info;
	private static int IntitialItemCBox = 0; //by default selection on ComboBox is item nr 0

	private String _title;
	private String _instructions;
	
	private SelectionDialogTable _table;

	private Controller _ctrl;

	private JSONObject newSelection;

	//TODO generalize?
	//public SelectionDialog(Controller ctrl, String title, String instructions) {
	public SelectionDialog(Controller ctrl){
		// TODO right now it is non-modal bc it extends JDialog directly, change? 
		_info = new ArrayList<>(ctrl.getForceLawsInfo()); 
		//TODO best way to generalize is passing a factory instead of the controller nd then having to call fLaws.info or bodies.info()
		_title = "Force Laws Selection"; //TODO to generalize, act as parameter in constructor
		_instructions = "Select a force law and provide values for the parameters in the 'Value' column" //TODO same
		 				+ "(default values are used for parameters with no user defined value)";
		newSelection = new JSONObject();
		_ctrl = ctrl;
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
		//_CBox.addActionListener(this); //TODO ok? creo que es mejor _CBox.addActionListener((e) - > lo que sea);
		_CBox.addActionListener((e) -> 
			{
				String name = _CBox.getSelectedItem().toString();
				newSelection = _info.get(searchNameInInfo(name));
				_table.updateData(newSelection);
			}
		);
		//_table.updateData(_info.get(IntitialItemCBox).getJSONObject("data")); //display current selection (default one) in the table
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO ok?
		if(e.getSource() == _CBox){
			JComboBox<String> cb = (JComboBox<String>) e.getSource();
			String name = (String) cb.getSelectedItem();
			newSelection = _info.get(searchNameInInfo(name)).getJSONObject("data");
			_table.updateData(newSelection);
		}
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

	private void initGUI() {

		setTitle(_title); 
		JPanel mainPanel = new JPanel(new BorderLayout());

		// PAGE_START
		JPanel topPanel = new JPanel();
		topPanel.add(new JLabel(_instructions));
		mainPanel.add(topPanel, BorderLayout.PAGE_START);

		// CENTER
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

		//table:
		_table = new SelectionDialogTable(IntitialItemCBox, _info.get(IntitialItemCBox));
		//_table.add(new JScrollPane(_table)); //TODO así es como estaba pero da error
		JScrollPane scrollTable = new JScrollPane(_table); //TODO set size
		scrollTable.setPreferredSize(new Dimension(300, 250));
		center.add(scrollTable);

		//combo box:
		setComboBoxNames();
		JPanel forcesCBoxPanel = new JPanel();
		forcesCBoxPanel.add(_CBox, "Select one: "); //TODO set size
		forcesCBoxPanel.setPreferredSize(new Dimension(300, 50));
		center.add(forcesCBoxPanel);

		mainPanel.add(center, BorderLayout.CENTER);

		// PAGE_END

		//buttons:
		JPanel buttonsPanel = new JPanel();// TODO functionality of everything

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(new Dimension(80, 20)); 
		cancelButton.addActionListener((e) -> {
			_status = 0; //TODO ?
			this.setVisible(false);
		});
		buttonsPanel.add(cancelButton);

		JButton OKButton = new JButton("OK");
		OKButton.setPreferredSize(new Dimension(80, 20)); 
		OKButton.addActionListener((e) -> {
			_status = 1; //TODO ?
			//we change content of newSelection according to the values the user has introduced in the table.
			// Afterwards, newSelection is the JSONObject that will be passed as info to the controller to 
			// create the new force law
			newSelection.put("data", _table.getData());
			if (!newSelection.has("type")) // if does not have type the combo box was not open
				newSelection.put("type", "nlug");
			/* once selected, change the force laws of the simulator to the chosen one */
			_ctrl.setForceLaws(newSelection);
			this.setVisible(false);
		});
		buttonsPanel.add(OKButton);

		//forcesCBoxPanel.add(buttonsPanel);

		mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);

		setContentPane(mainPanel);
		setMinimumSize(new Dimension(100, 100));
		setVisible(false);
	}

	public int getForceNr() {
		return (Integer) _CBox.getSelectedItem();
	}


	public int open() {
		//setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 50);// TODO ?
		pack();
		setVisible(true);
		return _status; //TODO ?
	}

}
