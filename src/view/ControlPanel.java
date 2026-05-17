/*
 * Control Panel
 * Spring 2026
 */
package view;

import model.Room;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ControlPanel is a class that contains actions button for player
 * to select during the game play.
 *
 * @author Emily Hernandez
 * @version 1.0 Spring 2026
 */
class ControlPanel implements PropertyChangeListener {

    private final PropertyChangeSupport myChangeSupport;
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
        myChangeSupport = new PropertyChangeSupport(this);
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

        myUpButton.setEnabled(false);
        myDownButton.setEnabled(false);
        myLeftButton.setEnabled(false);
        myRightButton.setEnabled(false);

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
        myHealingPotion.setEnabled(false);
        myVisionPotion.setEnabled(false);

        actionPanel.add(myHealingPotion);
        actionPanel.add(myVisionPotion);

        return actionPanel;
    }

    /**
     * set up button with action listener
     */
    private void addListeners() {
        myUpButton.addActionListener(_ ->
                myChangeSupport.firePropertyChange("move", "", "North"));
        myDownButton.addActionListener(_ ->
                myChangeSupport.firePropertyChange("move", "", "South"));
        myRightButton.addActionListener(_ ->
                myChangeSupport.firePropertyChange("move", "", "East"));
        myLeftButton.addActionListener(_ ->
                myChangeSupport.firePropertyChange("move", "", "West"));

        myRegularAttack.addActionListener(_ ->
                myChangeSupport.firePropertyChange("attack", "", "Basic"));
        mySpecialAttack.addActionListener(_ ->
                myChangeSupport.firePropertyChange("attack", "", "Special"));
        myHealingPotion.addActionListener(_ ->
                myChangeSupport.firePropertyChange("potion", "", "Heal"));
        myVisionPotion.addActionListener(_ ->
                myChangeSupport.firePropertyChange("potion", "", "Vision"));

    }

    /**
     * receive events from others to set up the buttons between update
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (theEvent.getPropertyName().equals("room")) {
            Room theNewRoom = (Room) theEvent.getNewValue();
            myUpButton.setEnabled(theNewRoom.getNorthRoom() != null);
            myDownButton.setEnabled(theNewRoom.getSouthRoom() != null);
            myRightButton.setEnabled(theNewRoom.getEastRoom() != null);
            myLeftButton.setEnabled(theNewRoom.getWestRoom() != null);
        }

        if (theEvent.getPropertyName().equals("HealingPotion")) {
            myHealingPotion.setEnabled((int) theEvent.getNewValue() > 0);
        }

        if (theEvent.getPropertyName().equals("VisionPotion")) {
            myVisionPotion.setEnabled((int) theEvent.getNewValue() > 0);
        }
    }

    //allowing controller or other class to listen in on action changes
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        myChangeSupport.addPropertyChangeListener(listener);
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
