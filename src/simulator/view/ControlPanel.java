package simulator.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import simulator.factories.Factory;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;
import javax.swing.*;
import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.ForceLaw;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver {
    private static final double Default_deltaT = 2500.0;
    private static final int Default_steps = 10000;
    private JToolBar _toolBar;
    private Controller _ctrl;
    private PhysicsSimulator _simulator;
    private static boolean _stopped;
    JButton ldBodiesB, ldForcesB, goB, stopB, exitB, removeB;
    JFileChooser fc;
    SelectionDialog _selectionDialog;
    JSpinner _stepsSpinner;
    JTextField _deltaT;


    private static String ForcesSelectionDialogTitle = "Force Laws Selection"; 
    private static String ForcesSelectionDialogInstr = 
            "Select a force law and provide values for the parameters in the 'Value' column"
		 		 + " (default values are used for parameters with no user defined value)"; 
    


    public ControlPanel(Controller ctrl,  PhysicsSimulator simulator, Factory<ForceLaw> fFL) {
        _ctrl = ctrl;
        _simulator = simulator;
        _stopped = true;
        _selectionDialog = new SelectionDialog( (Frame) SwingUtilities.getWindowAncestor(this), fFL,
                            ForcesSelectionDialogTitle,  ForcesSelectionDialogInstr);
        initGUI();
        _ctrl.addObserver(this);
    }

    private void initGUI() {

        _toolBar = new JToolBar();
        
        //Load button
        ldBodiesB = new JButton(new ImageIcon("resources/icons/open.png"));
        ldBodiesB.setPreferredSize(new Dimension(50, 50)); 
        ldBodiesB.addActionListener((e) -> {
            // (1) ask the user to select a file using a JFileChooser
            fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                // (2) reset the simulator
                _ctrl.reset();
                // (3) load the selected file into the simulator
                try (InputStream inChar = new FileInputStream(fc.getSelectedFile().toPath().toString())) { //TODO is "try" necessary?
                    _ctrl.loadBodies(inChar);
                } catch (IOException ioe) {
                    throw new IllegalArgumentException("Input file could not be opened");
                }
            }
        } );
        ldBodiesB.setToolTipText("Load a bodies json file");
        _toolBar.add(ldBodiesB);
            
        _toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        

        //Change force button
        ldForcesB = new JButton(new ImageIcon("resources/icons/physics.png"));
        ldForcesB.setPreferredSize(new Dimension(50, 50)); 
        ldForcesB.addActionListener((e) -> {
            // (1) open a dialog box and ask the user to select one of the available force laws;
            //user has clicked OK, and wants to continue changing the force law:
            if( _selectionDialog.open() == 1 ) {
                /* once selected, change the force laws of the simulator to the chosen one  */
			    try{
                    _ctrl.setForceLaws(_selectionDialog.getCBoxSelection());
                }catch (IllegalArgumentException iae){
                    JOptionPane.showMessageDialog(new JFrame(),
                                                    "The following error occurred: " + iae.getMessage(),
                                                    "The change of force laws did not succeed:", 
                                                    JOptionPane.ERROR_MESSAGE, 
                                                    new ImageIcon("resources/icons/caution.jpg"));
                }
            }
            //else : User has clicked Cancel (option nr 0) and we do nothing
        });
        ldForcesB.setToolTipText("Select one of the available force laws"); 
        _toolBar.add(ldForcesB);
        
        _toolBar.add(new JSeparator(SwingConstants.VERTICAL));
            
        
        //Start button
        goB = new JButton(new ImageIcon("resources/icons/run.png"));
        goB.setPreferredSize(new Dimension(50, 50)); 
        goB.addActionListener((e) -> {
            setAllButtonsTo(false, stopB);
            _stopped = false;
            /* (2) set the current delta-time of the simulator to the one specified in the corresponding text field;*/
            try{
                _simulator.setDeltaTime(Double.parseDouble(_deltaT.getText()));
            }catch(NullPointerException ex){
                _simulator.setDeltaTime(Default_deltaT);
            }
            
            /*(3) call method run_sim with the current value of steps as specified in the JSpinner*/
            run_sim((Integer) _stepsSpinner.getValue());  
        });
        goB.setToolTipText("Start the simulation"); 
        _toolBar.add(goB);

        
        //Stop button
        stopB = new JButton(new ImageIcon("resources/icons/stop.png"));
        stopB.addActionListener( (e) -> _stopped = true  );
        stopB.setToolTipText("Stop the simulation"); 
        stopB.setPreferredSize(new Dimension(50, 50));
        _toolBar.add(stopB);
    

        //JSpinner for steps:
        JLabel stepsLabel = new JLabel("Steps: ");
        stepsLabel.setLabelFor(_stepsSpinner); //TODO consultar si es necesario
        _toolBar.add(stepsLabel);
        SpinnerModel stepsModel = new SpinnerNumberModel(Default_steps, 0, null, 100); //initial value, min, max, step
        _stepsSpinner = new JSpinner(stepsModel);
        _stepsSpinner.setPreferredSize(new Dimension(80, 30));
        _toolBar.add(_stepsSpinner);
    

        //Delta-Time area using a JTextField.
        _deltaT = new JTextField("" + Default_deltaT);
        _simulator.setDeltaTime(Default_deltaT);
        _deltaT.setEditable(true);
        _deltaT.setPreferredSize(new Dimension(80, 30));
        JLabel dtLabel = new JLabel("Delta-Time: ");
        stepsLabel.setLabelFor(_deltaT); //TODO consultar
        _toolBar.add(dtLabel);
        _toolBar.add(_deltaT);

        _toolBar.add(new JSeparator(SwingConstants.VERTICAL));

        //Remove body button
        removeB = new JButton("Remove Body");
        removeB.setPreferredSize(new Dimension(100, 50));
        removeB.addActionListener((e) -> {
            String op = (String) JOptionPane.showInputDialog(new JFrame(),
                                    "Select a body", "Remove body", JOptionPane.PLAIN_MESSAGE,
                                    null, _ctrl.getBodiesId(), _ctrl.getBodiesId()[0]);
            _ctrl.removeBody(op);
        });
        _toolBar.add(removeB);


        //Exit button
        exitB = new JButton(new ImageIcon("resources/icons/exit.png"));
        exitB.setPreferredSize(new Dimension(50, 50)); 
        exitB.addActionListener( (e) -> {
            Object[] text = {"Quit", "No"};
            int option = JOptionPane.showOptionDialog(new JFrame(),
                            "Are sure you want to quit?", "Quitting simulator...",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("resources/icons/exit.png"),
                            text, text[0]);

            if (option == 0) //0 is "Quit"
                System.exit(0);
        });
        exitB.setToolTipText("Exit the simulator");
        _toolBar.add(exitB);

        this.add(_toolBar);
        this.setVisible(true);
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
                /*TODO In addition, catch all exceptions thrown by the controller/simulator and show a corresponding
                 message using a dialog box (e.g., using JOptionPane.showMessageDialog).*/
                // which errors can occur?
                JOptionPane.showMessageDialog(new JFrame(),
                                                "The following error occurred: " + e.getMessage(),
                                                "Error found while running: ",
                                                JOptionPane.ERROR_MESSAGE,
                                                null);
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
    }

    // sets all buttons to bool, except b
    private void setAllButtonsTo(boolean bool, JButton b) {
        ldBodiesB.setEnabled(bool);
        ldForcesB.setEnabled(bool);
        goB.setEnabled(bool);
        stopB.setEnabled(bool);
        exitB.setEnabled(bool);
        _deltaT.setEnabled(bool);
        _stepsSpinner.setEnabled(bool);

        if (b != null)
            b.setEnabled(!bool);
    }

    public static boolean getStop() {
        return _stopped;
    }

    
    // SimulatorObserver methods:

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _deltaT.setText(dt + "");
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc){
        _deltaT.setText(dt + "");
    }

    @Override
    public void onDeltaTimeChanged(double dt) {
        _deltaT.setText(dt + "");
    }

}
