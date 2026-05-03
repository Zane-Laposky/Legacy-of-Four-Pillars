package view;

import javax.swing.*;
import java.awt.*;

public class GamePanel {
    private static final int DIMENSION = 3;
    private JPanel myPanel;


    public GamePanel() {
        myPanel = new JPanel();
        initPanel();
    }

    private void initPanel() {
        myPanel.setLayout(new GridLayout(DIMENSION, DIMENSION));
        myPanel.setBackground(Color.BLACK);
    }

    public JPanel getPanel() {
        return myPanel;
    }
}
