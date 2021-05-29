package simulator.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import simulator.factories.Factory;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;
import java.io.File;
import javax.swing.*;
import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.ForceLaw;
import simulator.model.PhysicsSimulator;
import java.awt.BorderLayout;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver {
    private static final double Default_deltaT = 2500.0;
    private static final int Default_steps = 10000;
    private JToolBar _toolBar;
    private Controller _ctrl;
    private PhysicsSimulator _simulator;

    private static boolean _stopped; //WE ARE NOT REMOVING IT BECAUSE WE USE IT IN THE VIEWER, so that the user cannot drag bodies while the simulation is running
    
    private Factory<ForceLaw> _fFL;
    JButton ldBodiesB, ldForcesB, goB, stopB, exitB, removeB;
    JFileChooser fc;
    SelectionDialog _selectionDialog;
    JSpinner _stepsSpinner, _delaySpinner;
    JTextField _deltaT;
    private SwingWorker<Void, Void> _worker;

    private static String ForcesSelectionDialogTitle = "Force Laws Selection";
    private static String ForcesSelectionDialogInstr = "Select a force law and provide values for the parameters in the 'Value' column"
            + " (default values are used for parameters with no user defined value)";

    public ControlPanel(Controller ctrl, PhysicsSimulator simulator, Factory<ForceLaw> fFL) {
        _ctrl = ctrl;
        _simulator = simulator;
        fc = new JFileChooser();
        fc.setCurrentDirectory(new File("resources"));
        _stopped = true;
        _fFL = fFL;
        initGUI();
        _ctrl.addObserver(this);
    }

    private void initGUI() {

        _toolBar = new JToolBar();

        
        // Load button
        ldBodiesB = new JButton(new ImageIcon("resources/icons/open.png"));
        ldBodiesB.addActionListener((e) -> {
            // (1) ask the user to select a file using a JFileChooser
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                // (2) reset the simulator
                _ctrl.reset();
                // (3) load the selected file into the simulator
                try (InputStream inChar = new FileInputStream(fc.getSelectedFile().toPath().toString())) {
                    _ctrl.loadBodies(inChar);
                } catch (IOException ioe) {
                    openErrorDialog(new IllegalArgumentException("Input file could not be opened"));
                }
            }
        });
        ldBodiesB.setToolTipText("Load a bodies' JSON file");
        // Add load button
        _toolBar.add(ldBodiesB);

        // Add separator
        _toolBar.addSeparator();

        // Change force button
        ldForcesB = new JButton(new ImageIcon("resources/icons/physics.png"));
        ldForcesB.addActionListener((e) -> {
            // (1) open a dialog box and ask the user to select one of the available force
            // laws;
            // user has clicked OK, and wants to continue changing the force law:

            if (_selectionDialog == null) { /*
                                             * it is instantiated here to ensure that the controlPanel has been
                                             * correctly constructed when we use it as a parameter
                                             */
                _selectionDialog = new SelectionDialog((Frame) SwingUtilities.getWindowAncestor(this), _fFL,
                        ForcesSelectionDialogTitle, ForcesSelectionDialogInstr);
            }

            if (_selectionDialog.open() == 1) {
                /* once selected, change the force laws of the simulator to the chosen one : */
                try {
                    _ctrl.setForceLaws(_selectionDialog.getCBoxSelection());
                } catch (IllegalArgumentException iae) {
                    JOptionPane.showMessageDialog(new JFrame(), "The following error occurred: " + iae.getMessage(),
                            "The change of force laws did not succeed:", JOptionPane.ERROR_MESSAGE, null);
                }
            }
            // else : User has clicked Cancel (option nr 0) and we do nothing
        });
        ldForcesB.setToolTipText("Select one of the available force laws");
        // Add load force button
        _toolBar.add(ldForcesB);

        // Remove body button
        removeB = new JButton("Remove Body");
        removeB.setPreferredSize(new Dimension(100, 50));
        removeB.addActionListener((e) -> {
            String op = (String) JOptionPane.showInputDialog(new JFrame(), "Select a body", "Remove body",
                    JOptionPane.PLAIN_MESSAGE, null, _simulator.getBodiesId(), _simulator.getBodiesId()[0]);
            _simulator.removeBody(op);
        });
        removeB.setToolTipText("Remove a certain body from the simulation");
        // Add remove body button
        _toolBar.add(removeB);

        // Add separator
        _toolBar.addSeparator();

        // Start button
        goB = new JButton(new ImageIcon("resources/icons/run.png"));
        goB.addActionListener((e) -> {
            setAllButtonsTo(false, stopB);
            _stopped = false;

            /* (2) set the current delta-time of the simulator to the one specified in the
             * corresponding text field;
             */
            try {
                _simulator.setDeltaTime(Double.parseDouble(_deltaT.getText()));
            } catch (IllegalArgumentException ex) {
                openErrorDialog(ex);
                _simulator.setDeltaTime(Default_deltaT);
            }

            /*
             * (3) call method run_sim with the current value of steps as specified in the
             * JSpinner
             */
            _worker = new SwingWorker<Void, Void>() {
                protected Void doInBackground() {
                    run_sim( Long.valueOf( ((Integer) _delaySpinner.getValue()) ), (Integer) _stepsSpinner.getValue() );
                    
                    return null;
                }

                @Override
                protected void done() {
                    setAllButtonsTo(true, null);
                }
            };
            _worker.execute();
        });
        goB.setToolTipText("Start the simulation");
        // Add start button
        _toolBar.add(goB);

        // Stop button
        stopB = new JButton(new ImageIcon("resources/icons/stop.png"));
        stopB.addActionListener((e) -> {
            if ( _worker != null ) {
                _worker.cancel(true);
                _worker = null; 
            }
            _stopped = true;
        });
        stopB.setToolTipText("Stop the simulation");
        // Add stop button
        _toolBar.add(stopB);


        
        //ASSIGNMENT 3:
        // JSpinner for Delay:
        JLabel delayLabel = new JLabel("Delay: ");
        _toolBar.add(delayLabel); // Add delay label
        SpinnerModel delayModel = new SpinnerNumberModel(1, 1, 1000, 1); // initial value, min, max, step
        _delaySpinner = new JSpinner(delayModel); //delay between consecutive simulation steps
        _delaySpinner.setMaximumSize(new Dimension(80, 50));
        _delaySpinner.setMinimumSize(new Dimension(80, 50));
        // Add spiner for delay
        _toolBar.add(_delaySpinner);


        
        // JSpinner for steps:
        JLabel stepsLabel = new JLabel("Steps: ");
        _toolBar.add(stepsLabel); // Add steps label
        SpinnerModel stepsModel = new SpinnerNumberModel(Default_steps, 1, null, 100); // initial value, min, max, step
        _stepsSpinner = new JSpinner(stepsModel);
        _stepsSpinner.setMaximumSize(new Dimension(80, 50));
        _stepsSpinner.setMinimumSize(new Dimension(80, 50));
        // Add spiner for steps
        _toolBar.add(_stepsSpinner);

        // Delta-Time area using a JTextField.
        JLabel dtLabel = new JLabel("Delta-Time: ");
        // Add delta time label
        _toolBar.add(dtLabel);
        _deltaT = new JTextField("" + Default_deltaT);
        _simulator.setDeltaTime(Default_deltaT);
        _deltaT.setEditable(true);
        _deltaT.setMaximumSize(new Dimension(80, 50));
        _deltaT.setMinimumSize(new Dimension(80, 50));
        // Add text field for delta time
        _toolBar.add(_deltaT);

        // Add separator
        _toolBar.addSeparator();

        // Exit button
        exitB = new JButton(new ImageIcon("resources/icons/exit.png"));
        exitB.addActionListener((e) -> {
            Object[] text = { "Quit", "No" };
            int option = JOptionPane.showOptionDialog(new JFrame(), "Are sure you want to quit?",
                    "Quitting simulator...", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    new ImageIcon("resources/icons/exit.png"), text, text[0]);

            if (option == 0) // 0 is "Quit"
                System.exit(0);
        });
        exitB.setToolTipText("Exit the simulator");
        _toolBar.add(exitB); // Add exit button

        // Set layout and add toolbar
        this.setLayout(new BorderLayout());
        this.add(_toolBar, BorderLayout.CENTER);
        this.setVisible(true);
    }

    
    //RUN FOR ASSIGNMENT 2:
    private void run_sim(int n) {
        /*
         * the method run_sim guarantees that the interface will not block. It does this
         * by making a series of recursive calls, where each time the method run, runs
         * one iteration and calls itself again with n-1. It also enables all the
         * buttons when the execution is over.
         */

        if (n > 0 && !_stopped) {
            try {
                _ctrl.run(1);
            } catch (Exception e) {
                openErrorDialog(e);
                _stopped = true;
                setAllButtonsTo(true, null);
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
            setAllButtonsTo(true, null);
        }

        // NOTE: the number of steps is never <= 0 because the min value for the
        // JSpinner has been set to 1
    }


    //RUN FOR ASSIGNMENT 3:
    private void run_sim(Long delay, int n) {
        while(n > 0 && !_worker.isCancelled()) {
            // 1. execute the simulator one step, and handle exceptions if any
            try {
                _ctrl.run(1);
            } catch (Exception e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        openErrorDialog(e);
                        return;
                    }
                } );
            }
            // 2. sleep the current thread for ’delay’ milliseconds
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                return;
            }
            n--;
        }

    }



    private void openErrorDialog(Exception e) {
        JOptionPane.showMessageDialog(new JFrame(), "The following error occurred: " + e.getMessage(),
                "Error found while running: ", JOptionPane.ERROR_MESSAGE, null);
    }

    // sets all buttons and spinners to bool, except b
    private void setAllButtonsTo(boolean bool, JButton b) {
        ldBodiesB.setEnabled(bool);
        ldForcesB.setEnabled(bool);
        goB.setEnabled(bool);
        removeB.setEnabled(bool);
        stopB.setEnabled(bool);
        exitB.setEnabled(bool);
        _deltaT.setEnabled(bool);
        _stepsSpinner.setEnabled(bool);
        _delaySpinner.setEnabled(bool);

        if (b != null)
            b.setEnabled(!bool);
    }

    public static boolean getStop() {
        return _stopped;
    }

    // SimulatorObserver methods:

    private void setDeltaT_Text(double dt){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _deltaT.setText(dt + "");
            }
        });
    }

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
        setDeltaT_Text(dt);
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
        setDeltaT_Text(dt);
    }

    @Override
    public void onDeltaTimeChanged(double dt) {
        setDeltaT_Text(dt);
    }

}
