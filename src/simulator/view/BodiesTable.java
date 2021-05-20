package simulator.view;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import simulator.control.Controller;
import java.awt.Color;
import java.awt.Dimension;
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
        JTable table = new JTable(new BodiesTableModel(ctr));
        table.setPreferredSize(new Dimension(900, 200)); 
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                                                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane);
    }
}