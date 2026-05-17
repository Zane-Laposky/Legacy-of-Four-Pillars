/**
 * The Stats Panel
 * Spring 2026
 */
package view;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This class represents the stats panel in the game. This class displays
 * the character name and hit points.
 *
 * @author Emily Hernandez
 * @version 1.0, Spring 2026
 */
class StatsPanel implements PropertyChangeListener {

    /**
     * Maximum hit point character can take.
     */
    private final JLabel myMaxHP;

    /**
     * The stats panel for the game
     */
    private final JPanel myPanel;

    /**
     * The count of available hit points
     */
    private final JLabel myHitPt;

    /**
     * Constructs a StatsPanel object and initializes all components.
     *
     * @param theCharacterName the character's name
     */
    public StatsPanel(final String theCharacterName) {
        myPanel = new JPanel();
        myMaxHP = new JLabel("0");
        myHitPt = new JLabel("0");
        initialPanel(theCharacterName);
    }

    /**
     * Initializes the StatsPanel and other components such as myNamePanel
     * and myHitPt panel.
     *
     * @param theCharacterName the character's name
     */
    private void initialPanel(final String theCharacterName) {
        myPanel.setBackground(Color.WHITE);
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
        JLabel myTitle = new JLabel(theCharacterName);
        myTitle.setFont(myTitle.getFont().deriveFont(Font.BOLD));
        myPanel.add(myTitle);

        JPanel myHitPtPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        myHitPtPanel.add(new JLabel("HP: "));
        myHitPtPanel.add(myHitPt);
        myHitPtPanel.add(new JLabel("/"));
        myHitPtPanel.add(myMaxHP);

        myPanel.add(myHitPtPanel);
    }

    /**
     * Set up the buttons between update according to receive events from others
     *
     * @param theEvent Property Change Event
     */
    @Override
    public void propertyChange(PropertyChangeEvent theEvent) {
        if (theEvent.getPropertyName().equals("MaxHP")) {
            myMaxHP.setText(String.valueOf((int) theEvent.getNewValue()));
        }
        if (theEvent.getPropertyName().equals("HP")) {
            myHitPt.setText(String.valueOf(theEvent.getNewValue()));
        }
    }

    /**
     * Return the Stats Panel
     *
     * @return the Stats Panel
     */
    public JPanel getPanel() {
        return myPanel;
    }
}
