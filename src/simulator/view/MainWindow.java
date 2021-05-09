package simulator.view;

import simulator.control.Controller;
import simulator.model.PhysicsSimulator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;

public class MainWindow extends JFrame {

    private Controller _ctrl;
    private PhysicsSimulator _sim;

    public MainWindow(Controller ctrl, PhysicsSimulator simulator) {
        super("Physics Simulator");
        _sim = simulator;
        _ctrl = ctrl;
        initGUI();
    }

    private void initGUI() {

        this.setSize(900, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        JPanel controlPanel = new ControlPanel(_ctrl,  _sim);
        mainPanel.add(controlPanel, BorderLayout.PAGE_START);

        JPanel centerPanel = new JPanel();
        JPanel bodiesTable = new BodiesTable(_ctrl);
        bodiesTable.setPreferredSize(new Dimension(900, 200));
        JComponent viewer = new Viewer(_ctrl);
        viewer.setPreferredSize(new Dimension(900, 450));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(bodiesTable);
        centerPanel.add(viewer);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel statusBar = new StatusBar(_ctrl);
        statusBar.setPreferredSize(new Dimension(900, 30));
        mainPanel.add(statusBar, BorderLayout.PAGE_END);

        //TODO check sizes (you can use method setPreferredSize)

        this.setVisible(true);
    }


}
