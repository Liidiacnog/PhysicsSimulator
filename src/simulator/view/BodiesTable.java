package simulator.view;

import javax.swing.*;
import simulator.control.Controller;
import java.awt.Color;

public class BodiesTable extends JPanel {

    /*
    You need to complete the code to (1) create an instance of BodiesTableModel and pass
    it to a corresponding JTable; and (2) add the JTable to the panel (this) with a JScrollPane.
    */
    
    BodiesTable(Controller ctrl) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2), 
            "Bodies", 
            TitledBorder.LEFT,
            TitledBorder.RIGHT
            )
        );
        JTable table = new JTable(new BodiesTableModel(ctrl));
        //TODO de aquí para abajo no tengo ni idea de lo que estoy haciendo
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);
    }
}