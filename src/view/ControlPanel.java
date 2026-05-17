/*
 * Control Panel
 * Spring 2026
 */
package view;

import javax.swing.*;
import java.awt.*;

/**
 * ControlPanel is a class that contains actions button for player
 * to select during the game play.
 *
 * @author Emily Hernandez
 * @version 1.0 Spring 2026
 */
class ControlPanel {

    /**
     * Control panels for the controls
     */
    private final JPanel myControlPanel;

    /**
     * Button for moving up
     */
    private JButton myUpButton;

    /**
     * Button for moving down
     */
    private JButton myDownButton;

    /**
     * Button for moving right
     */
    private JButton myRightButton;

    /**
     * Button for moving left
     */
    private JButton myLeftButton;

    /**
     * Button for regular attack
     */
    private JButton myRegularAttack;

    /**
     * Button for special attack
     */
    private JButton mySpecialAttack;

    /**
     * Button to activate healing potion
     */
    private JButton myHealingPotion;

    /**
     * Button to activate vision potion
     */
    private JButton myVisionPotion;

    /**
     * Constructs a ControlPanel
     */
    public ControlPanel() {
        myControlPanel = new JPanel();
        initPanel();
    }

    /**
     * Applies layout, panels and initializes listeners for control panel components
     */
    private void initPanel() {
        myControlPanel.setLayout(new BoxLayout(myControlPanel, BoxLayout.Y_AXIS));

        myControlPanel.add(buildDirectionPanel());
        myControlPanel.add(buildActionPanel());
        addListeners();
    }

    /**
     * Build the direction panel by adding buttons for each direction.
     *
     * @return the direction panel
     */
    private JPanel buildDirectionPanel() {
        JPanel directionPanel = new JPanel(new GridLayout(1, 4, 3, 0));
        directionPanel.setSize(directionPanel.getPreferredSize());

        myUpButton = new JButton("▲");
        myDownButton = new JButton("▼");
        myLeftButton = new JButton("◀");
        myRightButton = new JButton("▶");

        directionPanel.add(myUpButton);
        directionPanel.add(myLeftButton);
        directionPanel.add(myRightButton);
        directionPanel.add(myDownButton);

        return directionPanel;
    }

    /**
     * Build the action panel by adding attack buttons or any special ability
     *
     * @return action panel
     */
    private JPanel buildActionPanel() {
        JPanel actionPanel = new JPanel(new GridLayout(2, 4, 2, 2));

        myRegularAttack = new JButton("Attack");
        mySpecialAttack = new JButton("Special Skill");
        actionPanel.add(myRegularAttack);
        actionPanel.add(mySpecialAttack);

        myHealingPotion = new JButton("Healing Potion");
        myVisionPotion = new JButton("Vision Potion");
        actionPanel.add(myHealingPotion);
        actionPanel.add(myVisionPotion);

        return actionPanel;
    }

    /**
     * Registers action listeners for the buttons
     */
    private void addListeners() {
        myUpButton.addActionListener(_ ->
                myUpButton.setEnabled(false));
        myDownButton.addActionListener(_ ->
                myDownButton.setEnabled(false));
        myRightButton.addActionListener(_ ->
                myRightButton.setEnabled(false));
        myLeftButton.addActionListener(_ ->
                myLeftButton.setEnabled(false));

        myRegularAttack.addActionListener(_ ->
                myRegularAttack.setEnabled(false));
        mySpecialAttack.addActionListener(_ ->
                mySpecialAttack.setEnabled(false));
        myHealingPotion.addActionListener(_ ->
                myHealingPotion.setEnabled(false));
        myVisionPotion.addActionListener(_ ->
                myVisionPotion.setEnabled(false));

    }

    /**
     * Return the control panel
     *
     * @return myControlPanel
     */
    public JPanel getPanel() {
        return myControlPanel;
    }



}
