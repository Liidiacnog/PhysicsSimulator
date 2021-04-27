package simulator.view;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import simulator.factories.*;
import simulator.model.ForceLaw;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

public class SelectionDialog extends JDialog {
    
    private JList<String> _itemsList;
	protected int _status;


	private JComboBox<String> _forces;
    private Builder[] forceBuildersArray = { //TODO Okay? Builder<ForceLaw> not allowed
        new MovingTowardsFixedPointBuilder(), 
        new NoForceBuilder(),
        new NewtonUniversalGravitationBuilder()
    };

	public SelectionDialog(Frame parent) {
		super(parent, true); // change true to false for non-modal
		initGUI();
	}

    private void setForceNames(){
        String[] names = new String[forceBuildersArray.length];
        int i = 0;
        for(Builder b : forceBuildersArray){
            names[i] = b.getBuilderInfo().getString("desc");
            ++i;
        }
        _forces = new JComboBox<String>(names); 
    }


	class ParamsTableModel extends AbstractTableModel{

		private String[] headers = { "Key", "Value", "Description"};
		
		private int _numCols;
		private int _numRows;

		String[][] data = new String[0][headers.length];
		
		public ParamsTableModel(){
			_numCols =  headers.length;
			_numRows = 0;
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
			return null;
		}
		

	}

	private void initGUI() {

		setTitle("Force Laws Selection");
		JPanel mainPanel = new JPanel(new BorderLayout());

        //PAGE_START
		JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select a force law and provide values for the parameters in the 'Value' column" +  
                                "(default values are used for parameters with no user defined value)"));
		mainPanel.add(topPanel, BorderLayout.PAGE_START);

        //CENTER
		
		//TODO on selection of a force, update data
        JTable paramsTable = new JTable(new ParamsTableModel());
		paramsTable.add(new JScrollPane(paramsTable));
		mainPanel.add(paramsTable, BorderLayout.CENTER);

        
        //PAGE_END
        setForceNames();
        JPanel forcesComboBoxPanel = new JPanel();
        forcesComboBoxPanel.add(_forces, "Force Law: ");
		
		JPanel buttonsPanel = new JPanel();//TODO functionality of everything
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener( (e) -> {
			//_status = 0; //TODO ?
			this.setVisible(false);
		});
		buttonsPanel.add(cancelButton);

		JButton OKButton = new JButton("OK");
		OKButton.addActionListener( (e) -> {
				//_status = 1; //TODO ?
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
		return (Integer) _forces.getSelectedItem();
	}

	
	public int open() {//TODO ?
		setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 50);
		pack();
		setVisible(true);
		return _status; 
	}
}
