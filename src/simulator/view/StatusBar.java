package simulator.view;

import simulator.control.Controller;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

/*
    displays some additional information related to the simulator’s state:
    the current time, the total number of bodies, and the current force laws.
 */
public class StatusBar extends JPanel implements SimulatorObserver {

    private JLabel _currTime; // for current time
    private JLabel _currLaws; // for force laws
    private JLabel _numOfBodies; // for number of bodies

    StatusBar(Controller ctrl) {
        ctrl.addObserver(this);
        initGUI();
    }

    private void initGUI() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBorder(BorderFactory.createBevelBorder(1));

        _currLaws.setPreferredSize(new Dimension(500, 30));
        _currTime.setPreferredSize(new Dimension(120, 30));
        _numOfBodies.setPreferredSize(new Dimension(80, 30));

        _currLaws.setVerticalAlignment(JLabel.TOP);
        _currTime.setVerticalAlignment(JLabel.TOP);
        _numOfBodies.setVerticalAlignment(JLabel.TOP);
        
        this.add(_currTime);
        this.add(_numOfBodies);
        this.add(_currLaws);
    }

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _numOfBodies = new JLabel("Bodies: " + bodies.size());
        _currLaws = new JLabel("Law: " + fLawsDesc);
        _currTime = new JLabel("Time: " + time);
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _numOfBodies.setText("Bodies: " + bodies.size());
        _currLaws.setText("Law: " + fLawsDesc);
        _currTime.setText("Time: " + time);
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
        _numOfBodies.setText("Bodies: " + bodies.size());
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        _numOfBodies.setText("Bodies: " + bodies.size());
        _currTime.setText("Time: " + time);
    }

    @Override
    public void onForceLawsChanged(String fLawsDesc) {
        _currLaws.setText("Law: " + fLawsDesc);
    }

}
