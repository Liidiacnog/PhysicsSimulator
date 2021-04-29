package simulator.view;

import javax.swing.JTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.AbstractTableModel;

import org.json.JSONObject;

public class SelectionDialogTable extends JTable implements ActionListener {
    
    
    
    public SelectionDialogTable(JSONObject onDisplay) {
        super(new ParamsTableModel());
    }


    class ParamsTableModel extends AbstractTableModel {

		private String[] headers = { "Key", "Value", "Description" };
		private String[][] data;

		private int _numCols;
		private int _numRows;
		private JSONObject _onDisplay;


		public ParamsTableModel(int itemIndex, JSONObject onDisplay) {
            _onDisplay =  onDisplay; //TODO ahhasagsvhbdacsnjcjacnjmckd
			_numCols = headers.length;
			_numRows = info.get(itemIndex).keySet().size();
			data = new String[_numRows][_numCols];
			// fill in data:
			updateTableData(info.get(itemIndex).getString("desc"));
		}

		public void updateTableData(String name) {
			boolean found = false;
			int i = 0;
			while (!found && i < info.size()) {
				if (info.get(i).getString("desc").equals(name))
					found = true;
				else
					++i;
			}
	
			if(found){
				for(int row = 1; row < info.get(i).keySet().size(); row++){
					int col = 0;
					data[row][col] = info.get(i).getString("key");
					col++;
					//value column is initially empty, and has to be filled by the user or we will introduce default values for the simulator
					data[row][col] = "";
					col++;
					data[row][col] = info.get(i).getString("desc");
				}	
			}
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

	}

    
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        
    }



}
