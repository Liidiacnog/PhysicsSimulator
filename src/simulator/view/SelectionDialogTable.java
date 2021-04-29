package simulator.view;


import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.AbstractTableModel;
import org.json.JSONObject;
import java.awt.BorderLayout;

public class SelectionDialogTable extends JPanel  {

	private JTable _table;
	private ParamsTableModel _model;

	public SelectionDialogTable(int itemIndex, JSONObject onDisplay) {
		_model = new ParamsTableModel(itemIndex, onDisplay);
		_table = new JTable(_model);
		setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(_table);
		this.add(scrollPane);
	}

	class ParamsTableModel extends AbstractTableModel implements ActionListener{

		private String[] headers = { "Key", "Value", "Description" };
		private String[][] data;

		private int _numCols;
		private int _numRows;
		private JSONObject _onDisplay; // JSONObject containing info of the item currently selected on the JCombobox in
										// the SelectionDialog

		public ParamsTableModel(int itemIndex, JSONObject onDisplay) {
			_numCols = headers.length;
			_numRows = onDisplay.keySet().size();
			data = new String[_numRows][_numCols];
			// fill in data:
			update(onDisplay);
		}

		public void update(JSONObject onDisplay) {
			_onDisplay = onDisplay;
			for (int row = 1; row < _onDisplay.keySet().size(); row++) {
				int col = 0;
				data[row][col] = _onDisplay.getString("key");
				col++;
				// value column is initially empty, and has to be filled by the user or we will
				// introduce default values for the simulator
				data[row][col] = "";
				col++;
				data[row][col] = _onDisplay.getString("desc");
			}
			fireTableStructureChanged();
		}

		@Override
		public String getColumnName(int column) {
			return headers[column];
		}

		@Override
		public int getRowCount() {
			return _numRows;
		}

		@Override
		public int getColumnCount() {
			return _numCols;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data[rowIndex][columnIndex];
		}

		//in charge of changes due to new values in table by user input
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO on change of column VALUES ...
			
		}
		//in charge of managing changes due to new selection of user in combobox
		public void actionPerformed(JSONObject newOnDisplay) {
			update(newOnDisplay);
		}
	}

	public void updateData(JSONObject newOnDisplay) {
		_model.actionPerformed(newOnDisplay);
	}


}
