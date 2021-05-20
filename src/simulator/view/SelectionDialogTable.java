package simulator.view;


import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.json.JSONObject;
import java.awt.Component;
import java.awt.BorderLayout;

public class SelectionDialogTable extends JPanel  {

	private JTable _table;
	private ParamsTableModel _model;

	public SelectionDialogTable(JSONObject onDisplay) {
		_model = new ParamsTableModel(onDisplay);

		_table = new JTable(_model) {
			private static final long serialVersionUID = 1L;

			// we override prepareRenderer to resized rows to fit to content
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(
						Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};

		setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(_table);
		this.add(scrollPane);
	}
	
	public void updateData(JSONObject newOnDisplay) {
		_model.update(newOnDisplay);
	}

	//returns the JSONArray for new "data" section of the force, according to values introduced by the user in the JTable
	public JSONObject getData() {
		return _model.getData();
	}



	class ParamsTableModel extends AbstractTableModel {

		private String[] headers = { "Key", "Value", "Description" };
		private String[][] _data = {};

		private int _numCols = headers.length; //only initialized once
		private int _numRows;
		private JSONObject _dataOnDisplay; // JSONObject containing info of the item currently selected on the JCombobox in
										// the SelectionDialog, in the case of forces it'll be the JSONObject corresponding
										// to the 'data' section of a certain force law

		public ParamsTableModel(JSONObject onDisplay) {
			// fill in data:
			update(onDisplay);
		}

		//generates a JSONObject by parsing the text introduced by the user on the editable columns
		public JSONObject getData() { 
			StringBuilder s = new StringBuilder();
			s.append('{');
			for (int i = 0; i < _data.length; i++) {
				if (!_data[i][0].isEmpty() && !_data[i][1].isEmpty()) {
					s.append('"');
					s.append(_data[i][0]);
					s.append('"');
					s.append(':');
					s.append(_data[i][1]);
					s.append(',');
				}
			}

			if (s.length() > 1)
				s.deleteCharAt(s.length() - 1);
			s.append('}');

			return new JSONObject(s.toString());
		}


		public void initTableData(){
			for (int row = 0; row < _numRows; row++) {
				int col = 0;
				//set names of keys:
				String keyName = JSONObject.getNames(_dataOnDisplay)[row];
				setValueAt(keyName, row, col); 
				col++;
				// set column 'value': initially empty, and has to be filled by the user, otherwise we will
				// introduce default values for the simulator:
				setValueAt("", row, col); 
				col++;
				//set description of the keys:
				setValueAt(_dataOnDisplay.getString(keyName), row, col);
			}
		}
		

		public void update(JSONObject onDisplay) {
			_dataOnDisplay = onDisplay.getJSONObject("data");
			_numRows = _dataOnDisplay.keySet().size();
			_data = new String[_numRows][_numCols];
			initTableData();
			fireTableStructureChanged();
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex){
			return columnIndex == 1;  //only one editable is 'value' column
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
			return _data[rowIndex][columnIndex];
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			_data[rowIndex][columnIndex] = (String) aValue;
		}

	}

}
