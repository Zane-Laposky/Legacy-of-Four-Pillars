/**
 * Inventory Panel
 * Spring 2026
 */
package view;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the inventory panel in the game. It displays
 * the player's collection of healing points, vision points, and pillars.
 *
 * @author Emily Hernandez
 * version 1.0, Spring 2026
 */
class InventoryPanel {

    /**
     * Initial value for all inventory counts.
     */
    private static final int INITIAL_POINT = 0;

    /**
     * Inventory panel for the game
     */
    private final JPanel myPanel;

    /**
     * Available Healing points label
     */
    private final JLabel myHP;

    /**
     * Available Vision points label
     */
    private final JLabel myVP;

    /**
     * Amount of collected pillars
     */
    private final JLabel myPillar;


    /**
     * Constructs an InventoryPanel object and initialize items count.
     */
    public InventoryPanel() {
        myPanel = new JPanel();

        myHP = new JLabel(String.valueOf(INITIAL_POINT));
        myVP = new JLabel(String.valueOf(INITIAL_POINT));
        myPillar = new JLabel(String.valueOf(INITIAL_POINT));

        initialPanel();
    }


    /**
     * Initializes the Inventory panel and other components such as panels for
     * healing points, vision points and pillars.
     */
    private void initialPanel() {
        myPanel.setBackground(Color.WHITE);
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
        //JLabel myTitle = new JLabel("INVENTORY");
        //myTitle.setFont(myTitle.getFont().deriveFont(Font.BOLD));
        //myPanel.add(myTitle);

        JPanel myHPPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        myHPPanel.add(new JLabel("Healing Potion: "));
        myHPPanel.add(myHP);

        JPanel myVPPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        myVPPanel.add(new JLabel("Vision Potion: "));
        myVPPanel.add(myVP);

        JPanel myPillarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        myPillarPanel.add(new JLabel("Pillar: "));
        myPillarPanel.add(myPillar);
        myPillarPanel.add(new JLabel("/ 4"));

        myPanel.add(myHPPanel);
        myPanel.add(myVPPanel);
        myPanel.add(myPillarPanel);

        myPanel.setMinimumSize(myPanel.getPreferredSize());
    }

    /**
     * Return the inventory panel
     *
     * @return the inventory panel
     */
    public JPanel getPanel() {
        return myPanel;
    }


    /**
     * Update any chances in inventory count as the game play
     *
     * @param theHP     the healing point
     * @param theVP     the vision point
     * @param thePillar the count of collected pillar
     */
    //NEED CONTROLLER TO PASS THE UPDATE
    public void updateInventory(int theHP, int theVP, int thePillar) {
        myHP.setText(String.valueOf(theHP));
        myVP.setText(String.valueOf(theVP));
        myPillar.setText(String.valueOf(thePillar));
    }
}
