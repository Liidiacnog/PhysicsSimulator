package simulator.view;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.Body;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;

public class BodiesTable extends JPanel {

    BodiesTable(Controller ctr) {
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 2), 
            "Bodies", 
            TitledBorder.LEFT,
            TitledBorder.RIGHT
            )
        );
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(new JTable(new BodiesTableModel(ctr)));
        this.add(scrollPane);
    }


    private static void test() {
        List<Body> b = new ArrayList<>();
        b.add(new Body("1", new Vector2D(), new Vector2D(1, 1), 100));
        b.add(new Body("2", new Vector2D(1, 1), new Vector2D(), 100));
        b.add(new Body("3", new Vector2D(1, 1), new Vector2D(1, 0), 500));

        //BodiesTable bt = new BodiesTable(b);

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 400);
        //f.setContentPane(bt);
        f.setVisible(true);
    }
    public static void main(String[] args) {
        test();
    }
}