package simulator.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.swing.*;
import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver {

    private Controller _ctrl;
    private boolean _stopped;
    JButton ldBodiesB, ldForcesB, goB, stopB, exitB;
    JFileChooser fc;

    ControlPanel(Controller ctrl) {
        _ctrl = ctrl;
        _stopped = true;
        initGUI();
        _ctrl.addObserver(this);
    }

    private void initGUI() {

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); // TODO okay?
        this.setVisible(true);

        // TODO build the tool bar by adding buttons, etc.

        ldBodiesB = new JButton("resources/icons/open.png");
        ldBodiesB.setSize(120, 30); // TODO okay?
        ldBodiesB.addActionListener((e) -> {
            // (1) ask the user to select a file using a JFileChooser
            fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { //TODO okay?
                // (2) reset the simulator
                _ctrl.reset();
                // (3) load the selected file into the simulator
                try (InputStream inChar = new FileInputStream(fc.getSelectedFile().getName())) {
                    _ctrl.loadBodies(inChar);
                } catch (IOException ioe) {
                    throw new IllegalArgumentException("Input file could not be opened"); // TODO okay?
                }
                
            }
        });

        ldForcesB = new JButton("resources/icons/physics.png");
        ;
        goB = new JButton("resources/icons/run.png");
        ;
        stopB = new JButton("resources/icons/stop.png");
        ;
        exitB = new JButton("resources/icons/exit.png");

        this.add(ldBodiesB);
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(ldForcesB);
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(goB);
        this.add(stopB);
        this.add(new JSeparator(SwingConstants.VERTICAL));

        // TODO add steps and delta time

        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(exitB);

        /*
         * Corresponding icons are available in the directory resources/icons. For the
         * steps selector use a JSpinner and for the Delta-Time area use a JTextField.
         * TODO All buttons should have tooltips to describe the corresponding
         * operations,
         */

    }

    private void run_sim(int n) {
        if (n > 0 && !_stopped) {
            try {
                _ctrl.run(1);
            } catch (Exception e) {
                // TODO show the error in a dialog box
                // TODO enable all buttons
                _stopped = true;
                return;
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    run_sim(n - 1);
                }
            });
        } else {
            _stopped = true;
            // TODO enable all buttons
        }
    }

    // SimulatorObserver methods:

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeltaTimeChanged(double dt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onForceLawsChanged(String fLawsDesc) {
        // TODO Auto-generated method stub

    }

}
