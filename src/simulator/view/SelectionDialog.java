package simulator.view;

import javax.swing.*;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.factories.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectionDialog extends JDialog implements ActionListener {

	private JList<String> _itemsList;
	protected int _status;//TODO what for?

	private JComboBox<String> _items;
	private List<JSONObject> _info;
	private static int IntitialItemComboBox = 0; //by default selection on ComboBox is item nr 0

	private String _title;
	private String _instructions;
	
	private SelectionDialogTable _table;

	private Controller _ctrl;

	//TODO generalize?
	//public SelectionDialog(Controller ctrl, String title, String instructions) {
	public SelectionDialog(Controller ctrl){
		super(); // change true to false for non-modal TODO ?
		_info = new ArrayList<>(ctrl.getForceLawsInfo());
		_title = "Force Laws Selection";
		_instructions = "Select a force law and provide values for the parameters in the 'Value' column"
		 				+ "(default values are used for parameters with no user defined value)";
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
		_items = new JComboBox<String>(names);
		_items.setSelectedIndex(IntitialItemComboBox);
		_items.addActionListener(this);
		_table.updateData(_info.get(IntitialItemComboBox).getJSONObject("data"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO check origin: combobox OR table (values column only)
		if(e.getSource() == _items){
			JComboBox<String> cb = (JComboBox<String>) e.getSource();
			String name = (String) cb.getSelectedItem();
			JSONObject newSelection = _info.get(searchNameInInfo(name)).getJSONObject("data");
			_table.updateData(newSelection);
			/*once selected, change the force laws of the simulator to the chosen one */
			_ctrl.setForceLaws(newSelection);
		}
		else if(e.getSource() == _table){
			//TODO
			/*  The user can edit only the “Values” column. You should
display a corresponding error message (e.g., using JOptionPane.showMessageDialog)
if the change of force laws did not succeed.*/
		}
	}

	// returns the index which the item with description == name has in the info list
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
		_table = new SelectionDialogTable(IntitialItemComboBox, _info.get(IntitialItemComboBox).getJSONObject("data"));
		_table.add(new JScrollPane(_table));
		mainPanel.add(_table, BorderLayout.CENTER);

		// PAGE_END
		setComboBoxNames();
		JPanel forcesComboBoxPanel = new JPanel();
		forcesComboBoxPanel.add(_items, "Select one: ");

		JPanel buttonsPanel = new JPanel();// TODO functionality of everything

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((e) -> {
			// _status = 0; //TODO ?
			this.setVisible(false);
		});
		buttonsPanel.add(cancelButton);

		JButton OKButton = new JButton("OK");
		OKButton.addActionListener((e) -> {
			// _status = 1; //TODO ?
			this.setVisible(false);
		});
		buttonsPanel.add(OKButton);

		forcesComboBoxPanel.add(buttonsPanel);

		mainPanel.add(forcesComboBoxPanel, BorderLayout.PAGE_END);

		setContentPane(mainPanel);
		setMinimumSize(new Dimension(100, 100));
		setVisible(false);
	}

	public int getForceNr() {
		return (Integer) _items.getSelectedItem();
	}


	public int open() {// TODO ?
		setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 50);
		pack();
		setVisible(true);
		return _status;
	}

}
