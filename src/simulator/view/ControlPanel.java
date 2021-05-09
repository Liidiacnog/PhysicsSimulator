package simulator.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;
import javax.swing.*;
import simulator.control.Controller;
import simulator.factories.Factory;
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
    private boolean _stopped;
    JButton ldBodiesB, ldForcesB, goB, stopB, exitB;
    JFileChooser fc;
    //SelectionDialog<Body> _selDialogB; //TODO dialog for bodies' selection 
    SelectionDialog<ForceLaw> _selDialogFL; 
    JSpinner _stepsSpinner;
    JTextField _deltaT;

/*TODO In addition, catch all exceptions thrown by the controller/simulator and show a corresponding
message using a dialog box (e.g., using JOptionPane.showMessageDialog). In the
observer methods modify the value of delta-time in the corresponding JTextField when
needed (i.e., in onRegister, onReset, and onDeltaTimeChanged). */

    /*public ControlPanel(Controller ctrl,  PhysicsSimulator simulator, Factory<ForceLaw> fFL, Factory<Body> fB) {
        _ctrl = ctrl;
        _simulator = simulator;
        _stopped = false;
        _selDialogFL = new SelectionDialog<>(_ctrl, fFL, (Frame) SwingUtilities.getWindowAncestor(this));
        _selDialogB = new SelectionDialog<>(_ctrl, fB, (Frame) SwingUtilities.getWindowAncestor(this)); //TODO
        _ctrl.addObserver(this);
        initGUI();
    }*/

    public ControlPanel(Controller ctrl,  PhysicsSimulator simulator, Factory<ForceLaw> f) {
        _ctrl = ctrl;
        _simulator = simulator;
        _stopped = false;
        _selDialogFL = new SelectionDialog<>(_ctrl, f, (Frame) SwingUtilities.getWindowAncestor(this));
        _ctrl.addObserver(this);
        initGUI();
    }


    private void initGUI() {

        _toolBar = new JToolBar(); //TODO make it resizable
        
        //Load button
        ldBodiesB = new JButton(new ImageIcon("resources/icons/open.png"));
        ldBodiesB.setPreferredSize(new Dimension(50, 50)); 
        ldBodiesB.addActionListener((e) -> {
            // (1) ask the user to select a file using a JFileChooser
            fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { //TODO see recording 07/04 part 2 : min 44
                // (2) reset the simulator
                _ctrl.reset();
                // (3) load the selected file into the simulator
                try (InputStream inChar = new FileInputStream(fc.getSelectedFile().toPath().toString())) { //TODO is "try" necessary?
                    _ctrl.loadBodies(inChar);
                } catch (IOException ioe) {
                    throw new IllegalArgumentException("Input file could not be opened"); // TODO okay?
                }
            }
        } );
        ldBodiesB.setToolTipText("Load a bodies json file");
        _toolBar.add(ldBodiesB);
            
        _toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        
        //Change force button
        ldForcesB = new JButton(new ImageIcon("resources/icons/physics.png"));
        ldForcesB.setPreferredSize(new Dimension(50, 50)); 
        /* ldForcesB.addActionListener((e) -> {
            // (1) open a dialog box and ask the user to select one of the available force laws;
            int status = _selectionDialog.open(); 
            if(status == 1){
                // (2) once selected, change the force laws of the simulator to the chosen one
                _ctrl.setForceLaws(_selectionDialog.getData());
            } 
              
        });
         */ //TODO do we need the status attribute in selectionDialog?
        
        ldForcesB.addActionListener((e) -> {
            // (1) open a dialog box and ask the user to select one of the available force laws;
            _selDialogFL.open();              
        });
        ldForcesB.setToolTipText("Select one of the available force laws"); 
        _toolBar.add(ldForcesB);
        
        _toolBar.add(new JSeparator(SwingConstants.VERTICAL));
            
        //start button
        goB = new JButton(new ImageIcon("resources/icons/run.png"));
        goB.setPreferredSize(new Dimension(50, 50)); 
        goB.addActionListener((e) -> {
            setAllButtons(stopB, false);
            _stopped = false;
            /* (2) set the current delta-time of the simulator to the one specified in the corresponding text field;*/
            try{
                _simulator.setDeltaTime(Double.parseDouble(_deltaT.getText()));
            }catch(NullPointerException ex){
                _simulator.setDeltaTime(Default_deltaT);
            }
            
            /*(3) call method run_sim with the current value of steps as specified in the JSpinner*/
            run_sim((Integer) _stepsSpinner.getValue());  //TODO okay?
        });
        goB.setToolTipText("Start the simulation"); 
        _toolBar.add(goB);

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
        _deltaT.setEditable(true);
        _deltaT.setPreferredSize(new Dimension(80, 30));
        JLabel dtLabel = new JLabel("Delta-Time: ");
        stepsLabel.setLabelFor(_deltaT); //TODO consultar
        _toolBar.add(dtLabel);
        _toolBar.add(_deltaT);

        _toolBar.add(new JSeparator(SwingConstants.VERTICAL));

        //Exit button
        exitB = new JButton(new ImageIcon("resources/icons/exit.png"));
        exitB.setPreferredSize(new Dimension(50, 50)); 
        exitB.addActionListener((e) -> {
            //TODO ask for the user’s confirmation and then exit using System.exit(0)
            //: _ctrl.requestExit(); ?
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
                // TODO show the error in a dialog box
                _stopped = true;
                setAllButtons(null, true);
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
            setAllButtons(null, true);
        }
    }

    // disables/enables all buttons except b
    private void setAllButtons(JButton b, boolean bool) {
        ldBodiesB.setEnabled(bool);
        ldForcesB.setEnabled(bool);
        goB.setEnabled(bool);
        stopB.setEnabled(bool);
        exitB.setEnabled(bool);
        _deltaT.setEnabled(bool);
        _stepsSpinner.setEnabled(bool);

        if (b != null) // TODO too dirty?
            b.setEnabled(!bool);
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
