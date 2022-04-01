package graphics;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {

    InfoPanel(ChessFrame parent) {
        super();

        setSize(300, 600);


        JPanel container = new JPanel();
        container.setLayout(new GridLayout(10, 1));

        add(container);

        container.add(new JLabel("INFO Panel" +
                ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ."));
        JButton jb = new JButton();
        jb.setText("Test");
        container.add(jb);

    }

}
