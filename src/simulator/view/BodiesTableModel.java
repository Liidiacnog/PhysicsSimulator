package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.model.Body;
import simulator.model.SimulatorObserver;
import simulator.control.Controller;
import java.util.ArrayList;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {

    private List<Body> _bodies;

    BodiesTableModel(Controller ctrl) {
        _bodies = new ArrayList<>();
        ctrl.addObserver(this);
    }

    //for test purposes
    BodiesTableModel(List<Body> bodies) {
        _bodies = new ArrayList<>(bodies);
    }
    
    @Override
    public int getRowCount() {
        return _bodies.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        String name = "";
        switch (column) {
            case 0:
                name = "id";
                break;
            case 1:
                name = "Mass";
                break;
            case 2:
                name = "Position";
                break;
            case 3:
                name = "Velocity";
                break;
            case 4:
                name = "Force";
                break;
        }
        return name;
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

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _bodies = new ArrayList<>(bodies);
        fireTableStructureChanged();
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _bodies = new ArrayList<>(bodies);
        fireTableStructureChanged();
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
        _bodies = new ArrayList<>(bodies);
        fireTableStructureChanged();
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        _bodies = new ArrayList<>(bodies);
        fireTableStructureChanged();
    }


    // private void update(List<Body> bodies) {
    //     _bodies = new ArrayList<>(bodies);
    //     fireTableStructureChanged();
    // }
    
}
