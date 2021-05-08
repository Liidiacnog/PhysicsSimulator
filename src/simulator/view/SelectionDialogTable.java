package simulator.view;


import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;

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

	//TODO: when does that happen?
			/*  You should display a corresponding error message (e.g., using JOptionPane.showMessageDialog)
			if the change of force laws did not succeed.  
			*/
	
	public void updateData(JSONObject newOnDisplay) {
		_model.actionPerformed(newOnDisplay);
	}

	//returns the JSONArray for new "data" section of the force, according to values introduced by the user in the JTable
	public JSONObject getData() {
		return _model.getData();
	}

	class ParamsTableModel extends AbstractTableModel{

		private String[] headers = { "Key", "Value", "Description" };
		private String[][] _data = {};

		private int _numCols = headers.length; //only initialized once
		private int _numRows;
		private String _type;
		private JSONObject _dataOnDisplay; // JSONObject containing info of the item currently selected on the JCombobox in
										// the SelectionDialog, in the case of forces it'll be the JSONObject corresponding
										// to the 'data' section of a certain force law

		public ParamsTableModel(int itemIndex, JSONObject onDisplay) {
			// fill in data:
			update(onDisplay);
		}

		// public JSONArray getData() {
		// 	String[][] sArray = new String[_model.getRowCount()][_model.getColumnCount()];
			
		// 	for(int i = 0; i < _model.getRowCount(); ++i){
		// 		sArray[i][0] = JSONObject.getNames(_dataOnDisplay)[i];
		// 	}
		// 	getTableContents(sArray);
		// 	JSONArray values = new JSONArray(sArray); //TODO is it built properly according to factories' format?
						
		// 	return values;
		// }

		public JSONObject getData() {
			JSONObject data = new JSONObject();

			if (_type != "mtfp") {
				for (int i = 0; i < _numRows; i++) {
					data.put((String) getValueAt(i, 0), Double.parseDouble(((String) getValueAt(i, 1))));
				}
			} else {
				String[] c = ((String) getValueAt(0, 1)).trim().split(",");
				Double x = Double.parseDouble(c[0]);
				Double y = Double.parseDouble(c[1].trim());
				data.put((String) getValueAt(0, 0), (new Vector2D(x, y)).asJSONArray());
				data.put("g", Double.parseDouble(((String) getValueAt(1, 1)))); //TODO da error pero tengo sueño
			}
			

			return data;
		}


		public void getTableContents(Object[][] a){
			for (int row = 0; row < _numRows; row++) { //(row 0 contains headers so it remains untouched) NOOO!
				int col = 0;
				String[] keys = _dataOnDisplay.keySet().toArray(new String[row]);
				a[row][col] = keys[row]; //TODO use setValueAt() instead?, TODO use JSONObject.getNames(_dataOnDisplay)[i] instead of "key" ?
				col = 1;
				// the column 'value' is initially empty, and has to be filled by the user, otherwise we will
				// introduce default values for the simulator:
				a[row][col] = getValueAt(row, col); //TODO not pretty, but 1 is column 'value'; NO FUNCIONA
				col = 2;
				a[row][col] = _dataOnDisplay.getString(keys[row]);
			}
		}

		public void update(JSONObject onDisplay) {
			_dataOnDisplay = onDisplay.getJSONObject("data");
			_type = onDisplay.getString("type");
			_numRows = _dataOnDisplay.keySet().size();
			_data = new String[_numRows][_numCols];
			getTableContents(_data);
			fireTableStructureChanged();
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex){
			return columnIndex == 1;  //TODO better way? only one editable is 'value' column
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
		
		//in charge of managing changes due to new selection of the user in combobox
		public void actionPerformed(JSONObject newOnDisplay) {
			update(newOnDisplay);
		}
	}

}
