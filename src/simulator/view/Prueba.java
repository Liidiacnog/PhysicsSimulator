package simulator.view;


import java.awt.Dimension;
import java.util.List;
import javax.swing.*;
import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;


public class Prueba extends JPanel implements SimulatorObserver {
        private static final double Default_deltaT = 2500.0;
    
        private JToolBar _toolBar;
        
        private Controller _ctrl;
        private PhysicsSimulator _simulator;
        private boolean _stopped;
        JButton ldBodiesB, ldForcesB, goB, stopB, exitB;
        JFileChooser fc;
        SelectionDialog _selectionDialog;
        JSpinner _stepsSpinner;
        JTextField _deltaT;
    
        public Prueba(Controller ctrl, PhysicsSimulator simulator) {
            _ctrl = ctrl;
            _simulator = simulator;
            _stopped = true;
            initGUI();
            //_selectionDialog = new SelectionDialog(_ctrl);
            //_ctrl.addObserver(this);
        }
    
        private void initGUI() {
    
            _toolBar = new JToolBar(); //TODO make it resizable
            
            ldBodiesB = new JButton(new ImageIcon("resources/icons/open.png"));
            ldBodiesB.setPreferredSize(new Dimension(50, 50)); 
            ldBodiesB.addActionListener((e) -> {
                // (1) ask the user to select a file using a JFileChooser
                fc = new JFileChooser();
                if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { //TODO see recording 07/04 part 2 : min 44
                                       
                }
            });
            ldBodiesB.setToolTipText("Load a bodies json file");
            _toolBar.add(ldBodiesB);
            
            
            _toolBar.add(new JSeparator(SwingConstants.VERTICAL));
            
    
            ldForcesB = new JButton(new ImageIcon("resources/icons/physics.png"));
            ldForcesB.setPreferredSize(new Dimension(50, 50)); 
            ldForcesB.addActionListener((e) -> {
                // (1) open a dialog box and ask the user to select one of the available force laws – see Figure 2;
                //_selectionDialog.setVisible(true);
                
            });
            ldForcesB.setToolTipText("Select one of the available force laws"); 
            _toolBar.add(ldForcesB);
            
            
            _toolBar.add(new JSeparator(SwingConstants.VERTICAL));
            
            
            goB = new JButton(new ImageIcon("resources/icons/run.png"));
            goB.setPreferredSize(new Dimension(50, 50)); 
            goB.addActionListener((e) -> {
                disableAllButtons(stopB);
                _stopped = false;
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
            stepsLabel.setLabelFor(_stepsSpinner); //TODO consultar
            _toolBar.add(stepsLabel);
            int currentSteps = 0;                           
                                                            //initial value, min, max, step
            SpinnerModel stepsModel = new SpinnerNumberModel(currentSteps, 0, null, 100); 
            _stepsSpinner = new JSpinner(stepsModel);
            _stepsSpinner.setPreferredSize(new Dimension(80, 30)); 
            _toolBar.add(_stepsSpinner);
    
    
            //Make the year be formatted without a thousands separator.
            //_stepsSpinner.setEditor(new JSpinner.NumberEditor(_stepsSpinner, "#")); //TODO check out
             
    
            //Delta-Time area using a JTextField.
            _deltaT = new JTextField("" + Default_deltaT); //TODO ok?
            _deltaT.setEditable(true);
            _deltaT.setPreferredSize(new Dimension(80, 30));
            JLabel dtLabel = new JLabel("Delta-Time: ");
            stepsLabel.setLabelFor(_deltaT); //TODO consultar
            _toolBar.add(dtLabel);
            _toolBar.add(_deltaT);
    
    
            _toolBar.add(new JSeparator(SwingConstants.VERTICAL));
    
            
            exitB = new JButton(new ImageIcon("resources/icons/exit.png"));
            exitB.setPreferredSize(new Dimension(50, 50)); 
            exitB.addActionListener((e) -> {
                //TODO : _ctrl.requestExit(); ?
                System.exit(0); //TODO Ok?
            });
            exitB.setToolTipText("Exit the simulator");
            _toolBar.add(exitB);
                    
    
            this.add(_toolBar);
            this.setVisible(true);
    
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

    //TODO remove:
    public static void main(String[] args){


        //SwingUtilities.invokeLater(new Runnable() {
            //public void run() {
                JFrame f = new JFrame();

                Prueba m = new Prueba(null, null);

                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(400, 400);
                f.setContentPane(m);
                f.setVisible(true);
                
         //   }
        //});
    }
}
