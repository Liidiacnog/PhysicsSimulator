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
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { //TODO okay? see recording 07/04 part 2 : min 44
                // (2) reset the simulator
                _ctrl.reset();
                // (3) load the selected file into the simulator
                try (InputStream inChar = new FileInputStream(fc.getSelectedFile().getName())) { //TODO is "try" necessary?
                    _ctrl.loadBodies(inChar);
                } catch (IOException ioe) {
                    throw new IllegalArgumentException("Input file could not be opened"); // TODO okay?
                }
                
            }
        });

        ldForcesB = new JButton("resources/icons/physics.png");
        ldForcesB.setSize(120, 30); // TODO okay?
        ldForcesB.addActionListener((e) -> {
            // (1) open a dialog box and ask the user to select one of the available force laws – see Figure 2;
            JOptionPane.showOptionDialog();
            
        });
        /*  and (2) once selected, change the force laws
of the simulator to the chosen one (using _ctrl.setForceLaws(...)). In order to get
information on the available force laws you can use _ctrl.getForceLawsInfo(). The
combo-box should include the list of all available force laws, where the description of
each is obtained from the value of the key “desc” of the corresponding JSONObject
that describes the force laws. Once a force law is selected in the combo-box, the
parameters table should change to include a list of corresponding parameters (the
keys of the corresponding “data” section) so the use can provide corresponding values:
the first column is the parameter name, the second is where the user provide values,
and the this is a description that is taken from the value of the corresponding key
in the “data” section. The user can edit only the “Values” column. You should
display a corresponding error message (e.g., using JOptionPane.showMessageDialog)
if the change of force laws did not succeed.*/
        goB = new JButton("resources/icons/run.png");
        ;
        stopB = new JButton("resources/icons/stop.png");
        ;
        exitB = new JButton("resources/icons/exit.png");
        exitB.addActionListener((e) -> {
            //TODO : _ctrl.requestExit(); ?
            System.exit(0); //TODO Ok?
        });


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
         * For the steps selector use a JSpinner and for the Delta-Time area use a JTextField.
         * TODO All buttons should have tooltips to describe the corresponding operations,
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
