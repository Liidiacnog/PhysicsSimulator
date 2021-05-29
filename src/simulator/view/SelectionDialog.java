package simulator.view;

import javax.swing.*;
import org.json.JSONObject;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.awt.Frame;
import simulator.factories.Factory;


public class SelectionDialog extends JDialog {

	private int _status; //== 0 if user clicks on Cancel button, or 1 if he clicks on OK button

	private JComboBox<String> _CBox;
	private List<JSONObject> _info;
	private static int IntitialItemCBox = 0; //by default selection on ComboBox is item nr 0

	private String _title;
	private String _instructions;
	
	private SelectionDialogTable _table;

	private JSONObject _CBoxSelection;

	public SelectionDialog(Frame parent, Factory<?> factory, String title, String instr) {
		super(parent, true); //true for modal
		_info = new ArrayList<>(factory.getInfo()); 
		_title = title;
		_instructions = instr; 
		_CBoxSelection = _info.get(IntitialItemCBox); 
		initGUI();
	}


	//sets option names for the combo box, taking them from the key "desc" of every JSONObject in _info   
	private void setComboBoxNames() {
		ComboBoxModel<String> _cBoxModel = new DefaultComboBoxModel<String>() {

			@Override
			public int getSize() {
				return _info.size();
			}

			@Override
			public String getElementAt(int index) {
				return _info.get(index).getString("desc");
			}
			
		};
		_CBox = new JComboBox<String>(_cBoxModel);
		_CBox.setSelectedIndex(IntitialItemCBox);
		_CBox.addActionListener((e) -> {
 				String name = _CBox.getSelectedItem().toString();
				_CBoxSelection = _info.get(searchNameInInfo(name));
				_table.updateData(_CBoxSelection);
		});
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
		setMinimumSize(new Dimension(600, 500));
		
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
		JPanel panelCBox = new JPanel();
		panelCBox.add(_CBox, "Select one: ");
		panelCBox.setPreferredSize(new Dimension(80, 50));
		center.add(panelCBox);

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
			// we change content of _CBoxSelection according to the values the user has introduced in the table.
			// Afterwards, _CBoxSelection is the JSONObject that will be passed as info to the controller to 
			// create the new force law
			_CBoxSelection.put("data", _table.getData());
			this.setVisible(false);
		});
		buttonsPanel.add(OKButton);

		mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);

		setContentPane(mainPanel);
		setVisible(false);
	}

	public int getForceNr() {
		return (Integer) _CBox.getSelectedItem();
	}

	public JSONObject getCBoxSelection() {
		return _CBoxSelection;
	}


	public int open() {
		_status = 0;
		if (getParent() != null)
			setLocation(//
					getParent().getLocation().x + getParent().getWidth() / 2 - getWidth() / 2,
					getParent().getLocation().y + getParent().getHeight() / 2 - getHeight() / 2);
		
		pack();
		setVisible(true);
		return _status;
	}

}
