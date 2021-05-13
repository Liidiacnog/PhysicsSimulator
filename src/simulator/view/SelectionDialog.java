package simulator.view;

import javax.swing.*;
import org.json.JSONObject;
import simulator.control.Controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.awt.Frame;
import simulator.factories.Factory;


public class SelectionDialog extends JDialog {

	protected int _status;

	private JComboBox<String> _CBox;
	private List<JSONObject> _info;
	private static int IntitialItemCBox = 0; //by default selection on ComboBox is item nr 0

	private String _title;
	private String _instructions;
	
	private SelectionDialogTable _table;

	private Controller _ctrl;

	private JSONObject _CBoxSelection;

	public SelectionDialog(Frame parent, Factory<?> factory, Controller ctrl, String title, String instr) {
		super(parent, true); //true for modal
		_info = new ArrayList<>(factory.getInfo()); 
		_title = title;
		_instructions = instr; 
		_ctrl = ctrl;
		_CBoxSelection = _info.get(IntitialItemCBox); 
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
		_CBox.addActionListener((e) -> 
			{
				String name = _CBox.getSelectedItem().toString();
				_CBoxSelection = _info.get(searchNameInInfo(name));
				_table.updateData(_CBoxSelection);
			}
		);
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
		assert(i < _info.size()); //name will always be one contained in the JList so it'll be one in the info
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
		_table = new SelectionDialogTable(_CBoxSelection);
		center.add(_table);

		//combo box:
		setComboBoxNames();
		JPanel forcesCBoxPanel = new JPanel();
		forcesCBoxPanel.add(_CBox, "Select one: ");
		forcesCBoxPanel.setPreferredSize(new Dimension(300, 50));
		center.add(forcesCBoxPanel);

		mainPanel.add(center, BorderLayout.CENTER);

		// PAGE_END

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
			//we change content of newSelection according to the values the user has introduced in the table.
			// Afterwards, newSelection is the JSONObject that will be passed as info to the controller to 
			// create the new force law
			_CBoxSelection.put("data", _table.getData());
			/* once selected, change the force laws of the simulator to the chosen one */
			_ctrl.setForceLaws(_CBoxSelection);
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
		setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 50);// TODO ?
		pack();
		setVisible(true);
		return _status; //TODO ?
	}

}
