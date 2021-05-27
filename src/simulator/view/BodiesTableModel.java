package simulator.view;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import simulator.model.Body;
import simulator.model.SimulatorObserver;
import simulator.control.Controller;
import java.util.ArrayList;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {

    private List<Body> _bodies;
    private String[] _headers = {"Id", "Mass", "Position", "Velocity", "Force"};

    BodiesTableModel(Controller ctrl) {
        _bodies = new ArrayList<>();
        ctrl.addObserver(this);
    }
    
    @Override
    public int getRowCount() {
        return _bodies.size();
    }

    @Override
    public int getColumnCount() {
        return _headers.length;
    }


    @Override
    public String getColumnName(int column) {
        return _headers[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object obj = null;
        switch (columnIndex) {
            case 0:
                obj = _bodies.get(rowIndex).getId();
                break;
            case 1:
                obj = _bodies.get(rowIndex).getMass();
                break;
            case 2:
                obj = _bodies.get(rowIndex).getPosition();
                break;
            case 3:
                obj = _bodies.get(rowIndex).getVelocity();
                break;
            case 4:
                obj = _bodies.get(rowIndex).getForce();
                break;
        }
        return obj;
    }

    //auxiliary method to be called by methods inherited of Observer
    private void resetBodiesList(List<Body> l){
        _bodies = new ArrayList<>(l);
        fireTableStructureChanged();
    }

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
        resetBodiesList(bodies);
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
        resetBodiesList(bodies);
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
        resetBodiesList(bodies);
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        resetBodiesList(bodies);
    }

    @Override
    public void onBodyRemoved(List<Body> bodies, Body b) {
        resetBodiesList(bodies);
    }

}
