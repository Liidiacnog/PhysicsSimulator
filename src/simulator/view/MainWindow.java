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

    public MainWindow(Controller ctrl) {
        super("Physics Simulator");
        _ctrl = ctrl;
        initGUI();
    }

    private void initGUI() {

        // TODO complete this method to build the GUI
        // ...

        this.setSize(800, 650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        //JPanel controlPanel = new ControlPanel(_ctrl, null); //TODO change
        //mainPanel.add(controlPanel, BorderLayout.PAGE_START);

        JPanel centerPanel = new JPanel();
        JPanel bodiesTable = new BodiesTable(_ctrl);
        bodiesTable.setPreferredSize(new Dimension(800, 200));
        JComponent viewer = new Viewer(_ctrl);
        viewer.setPreferredSize(new Dimension(800, 450));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(bodiesTable);
        centerPanel.add(viewer);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        //TODO check sizes (you can use method setPreferredSize)
        //TODO add status bar

        this.setVisible(true);
    }


}
