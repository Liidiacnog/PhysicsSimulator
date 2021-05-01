package simulator.view;

import simulator.control.Controller;
import simulator.model.PhysicsSimulator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;

public class MainWindow extends JFrame {

    private Controller _ctrl;

    public MainWindow(Controller ctrl) {
        super("Physics Simulator");
        _ctrl = ctrl;
        initGUI();
    }

    private void initGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);
        // TODO complete this method to build the GUI
        // ...

        JPanel controlPanel = new ControlPanel(_ctrl, null); //TODO change
        mainPanel.add(controlPanel, BorderLayout.PAGE_START);

        JPanel centerPanel = new JPanel();
        JPanel bodiesTable = new BodiesTable(_ctrl);
        JComponent viewer = new Viewer(_ctrl);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(bodiesTable);
        centerPanel.add(viewer);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        //TODO check sizes (you can use method setPreferredSize)
        //TODO add status bar

        this.setVisible(true);
    }


}
