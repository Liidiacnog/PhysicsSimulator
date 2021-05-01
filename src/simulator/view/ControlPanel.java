package simulator.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.swing.*;
import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver {
    private static final double Default_deltaT = 2500.0;

    // TODO change to JToolBar
    
    private Controller _ctrl;
    private PhysicsSimulator _simulator;
    private boolean _stopped;
    JButton ldBodiesB, ldForcesB, goB, stopB, exitB;
    JFileChooser fc;
    SelectionDialog _selectionDialog;
    JSpinner _stepsSpinner;
    JTextField _deltaT;

    ControlPanel(Controller ctrl, PhysicsSimulator simulator) {
        _ctrl = ctrl;
        _simulator = simulator;
        _stopped = true;
        initGUI();
        _selectionDialog = new SelectionDialog(_ctrl);
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
            _selectionDialog.setVisible(true);
            
        });
        
        goB = new JButton("resources/icons/run.png");
        goB.setSize(120, 30); // TODO okay?
        goB.addActionListener((e) -> {
            disableAllButtons(stopB);
            _stopped = false;
            /* (2) set the current delta-time of the simulator to the one specified in the corresponding text field;*/
            try{
                _simulator.setDeltaTime(Double.parseDouble(_deltaT.getText()));
            }catch(NullPointerException ex){
                _simulator.setDeltaTime(Default_deltaT);
            }
            
            /*(3) call method run_sim with the current value of steps as specified in the JSpinner*/
            run_sim((Integer) _stepsSpinner.getValue()); //TODO ok?
        });

        stopB = new JButton("resources/icons/stop.png");
        stopB.addActionListener( (e) -> _stopped = true  );

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

        
         
        this.add(new JLabel("Steps: "));
        int currentSteps = 0;                           
                                                        //initial value, min, max, step
        SpinnerModel stepsModel = new SpinnerNumberModel(currentSteps, 0, null, 100); 
        _stepsSpinner = new JSpinner(stepsModel);
        //l.setLabelFor(spinner);
        this.add(_stepsSpinner);


        //Make the year be formatted without a thousands separator.
        _stepsSpinner.setEditor(new JSpinner.NumberEditor(_stepsSpinner, "#")); //TODO check out
         

        //Delta-Time area using a JTextField.
        _deltaT = new JTextField("" + Default_deltaT); //TODO ok?
        _deltaT.setEditable(true);
        this.add(new JLabel("Delta-Time: "));
        this.add(_deltaT);


        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(exitB);

        /*
         * TODO All buttons should have tooltips to describe the corresponding operations,
         */

    }

    private void run_sim(int n) {
        /*
         * You should complete method run_sim to enable all buttons again once the
         * execution is over. Note that method run_sim as provided above guarantees that
         * the interface will not block, in order to understand this behaviour change
         * the body of method run_sim a single instruction _ctrl.run(n) — you will not
         * see the intermediate steps, only the final result, and in the meantime the
         * interface will be completely blocked.
         */
        if (n > 0 && !_stopped) {
            try {
                _ctrl.run(1);
            } catch (Exception e) {
                // TODO show the error in a dialog box
                _stopped = true;
                enableAllButtons();
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
            enableAllButtons();
        }
    }

    // disables all buttons except b
    private void disableAllButtons(JButton b) {
        ldBodiesB.disable();
        ldForcesB.disable();
        goB.disable();
        stopB.disable();
        exitB.disable();

        if (b != null) // TODO too dirty?
            b.enable();
    }

    private void enableAllButtons() {
        ldBodiesB.enable();
        ldForcesB.enable();
        goB.enable();
        stopB.enable();
        exitB.enable();
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
