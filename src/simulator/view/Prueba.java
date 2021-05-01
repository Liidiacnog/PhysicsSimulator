package simulator.view;


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
    
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); // TODO okay?
            
            _toolBar = new JToolBar();
    
            // TODO build the tool bar by adding buttons, etc.
    
            ldBodiesB = new JButton("resources/icons/open.png");
            ldBodiesB.setSize(120, 30); // TODO okay?
            ldBodiesB.addActionListener((e) -> {
                // (1) ask the user to select a file using a JFileChooser
                fc = new JFileChooser();
                if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { //TODO okay? see recording 07/04 part 2 : min 44
                                       
                }
            });
            _toolBar.add(ldBodiesB);
            _toolBar.add(new JSeparator(SwingConstants.VERTICAL));
            
    
            ldForcesB = new JButton("resources/icons/physics.png");
            ldForcesB.setSize(120, 30); // TODO okay?
            ldForcesB.addActionListener((e) -> {
                // (1) open a dialog box and ask the user to select one of the available force laws – see Figure 2;
                //_selectionDialog.setVisible(true);
                
            });
            _toolBar.add(ldForcesB);
            _toolBar.add(new JSeparator(SwingConstants.VERTICAL));
            
            
            goB = new JButton("resources/icons/run.png");
            goB.setSize(120, 30); // TODO okay?
            goB.addActionListener((e) -> {
                disableAllButtons(stopB);
                _stopped = false;
                
                
            });
            _toolBar.add(goB);
    
            stopB = new JButton("resources/icons/stop.png");
            stopB.addActionListener( (e) -> _stopped = true  );
            _toolBar.add(stopB);
    
    
            //JSpinner for steps:
    
            _toolBar.add(new JLabel("Steps: "));
            int currentSteps = 0;                           
                                                            //initial value, min, max, step
            SpinnerModel stepsModel = new SpinnerNumberModel(currentSteps, 0, null, 100); 
            _stepsSpinner = new JSpinner(stepsModel);
            //l.setLabelFor(spinner);
             _toolBar.add(_stepsSpinner);
    
    
            //Make the year be formatted without a thousands separator.
            //_stepsSpinner.setEditor(new JSpinner.NumberEditor(_stepsSpinner, "#")); //TODO check out
             
    
            //Delta-Time area using a JTextField.
            _deltaT = new JTextField("" + Default_deltaT); //TODO ok?
            _deltaT.setEditable(true);
            _toolBar.add(new JLabel("Delta-Time: "));
            _toolBar.add(_deltaT);
    
    
            _toolBar.add(new JSeparator(SwingConstants.VERTICAL));
    
            exitB = new JButton("resources/icons/exit.png");
            exitB.addActionListener((e) -> {
                //TODO : _ctrl.requestExit(); ?
                System.exit(0); //TODO Ok?
            });
            _toolBar.add(exitB);
                    
    
            this.add(_toolBar);
            this.setVisible(true);
    
            /*
             * TODO All buttons should have tooltips to describe the corresponding operations,
             */
    
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
