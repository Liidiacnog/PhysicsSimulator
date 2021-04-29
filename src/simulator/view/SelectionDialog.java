package simulator.view;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import org.json.JSONObject;
import simulator.factories.*;
import simulator.model.ForceLaw;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectionDialog extends JDialog implements ActionListener {

	private JList<String> _itemsList;
	protected int _status;//TODO what for?

	private JComboBox<String> _items;
	private List<JSONObject> info;
	private static int IntitialItemComboBox = 0; //by default selection on ComboBox is item nr 0

	private String _title;
	private String _instructions;
	
	private SelectionDialogTable _table;

	public SelectionDialog(JFrame parent, BuilderBasedFactory bBF, String title, String instructions) {
		super(parent, true); // change true to false for non-modal
		info = new ArrayList(bBF.getInfo());
		_title = title;
		_instructions = instructions;
		initGUI();
	}

	private void setComboBoxNames() {
		String[] names = new String[info.size()];
		int i = 0;
		for (JSONObject o : info) {
			names[i] = o.getString("desc");
			++i;
		}
		_items = new JComboBox<String>(names);
		_items.setSelectedIndex(IntitialItemComboBox);
		_items.addActionListener(this);
		updateTableData(names[IntitialItemComboBox]);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO check origin: combobox OR table (values column only)
		JComboBox<String> cb = (JComboBox<String>) e.getSource();
		String name = (String) cb.getSelectedItem();
		updateTableData(name);
		// TODO
	}

	

	
	private void initGUI() {

		setTitle(_title); // "Force Laws Selection"
		JPanel mainPanel = new JPanel(new BorderLayout());

		// PAGE_START
		JPanel topPanel = new JPanel();
		topPanel.add(new JLabel(_instructions));
		/*
		 * "Select a force law and provide values for the parameters in the 'Value' column"
		 * + "(default values are used for parameters with no user defined value)"
		 */
		mainPanel.add(topPanel, BorderLayout.PAGE_START);

		// CENTER

		// TODO on selection of a force, update data
		_table = new SelectionDialogTable(IntitialItemComboBox);
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
