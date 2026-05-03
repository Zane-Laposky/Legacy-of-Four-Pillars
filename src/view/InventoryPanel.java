package view;

import javax.swing.*;
import java.awt.*;

public class InventoryPanel {
    private static final int INITIAL_POINT = 0;
    private final JPanel myPanel;
    private final JLabel myHP;
    private final JLabel myVP;
    private final JLabel myPillar;

    public InventoryPanel() {
        myPanel = new JPanel();

        myHP = new JLabel(String.valueOf(INITIAL_POINT));
        myVP = new JLabel(String.valueOf(INITIAL_POINT));
        myPillar = new JLabel(String.valueOf(INITIAL_POINT));

        initialPanel();
    }

    private void initialPanel() {
        myPanel.setBackground(Color.WHITE);
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
        JLabel myTitle = new JLabel("INVENTORY");
        myTitle.setFont(myTitle.getFont().deriveFont(Font.BOLD));
        myPanel.add(myTitle);

        JPanel myHPPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        myHPPanel.add(new JLabel("Healing Point: "));
        myHPPanel.add(myHP);

        JPanel myVPPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        myVPPanel.add(new JLabel("Vision Potion: "));
        myVPPanel.add(myVP);

        JPanel myPillarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        myPillarPanel.add(new JLabel("Pillar: "));
        myPillarPanel.add(myPillar);

        myPanel.add(myHPPanel);
        myPanel.add(myVPPanel);
        myPanel.add(myPillarPanel);
    }
    public JPanel getPanel() {
        return myPanel;
    }

    //NEED CONTROLLER TO PASS THE UPDATE
    private void updateInventory(int theHP, int theVP, int thePillar) {
        myHP.setText(String.valueOf(theHP));
        myVP.setText(String.valueOf(theVP));
        myPillar.setText(String.valueOf(thePillar));
    }
}
