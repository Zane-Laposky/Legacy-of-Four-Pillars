/**
 * The Stats Panel
 * Spring 2026
 */
package view;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the stats panel in the game. This class displays
 * the character name and hit points.
 *
 * @author Emily Hernandez
 * @version 1.0, Spring 2026
 */
public class StatsPanel {

    /**
     * Maximum hit point character can take.
     */
    private static final int MAX_HITPOINT = 100; // NEED UPDATE

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
    //NEED UPDATE FROM CONTROLLER ON THE INITIAL VALUE
    public StatsPanel(final String theCharacterName/*, final int theHitPoint*/) {
        myPanel = new JPanel();
        myHitPt = new JLabel(String.valueOf(MAX_HITPOINT)); // NEED UPDATE FROM CONTROLLER
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
        myHitPtPanel.add(new JLabel("/" + MAX_HITPOINT));


        myPanel.add(myHitPtPanel);
    }


    /**
     * Return the Stats Panel
     *
     * @return the Stats Panel
     */
    public JPanel getPanel() {
        return myPanel;
    }


    /**
     * Update the available hit point during the game.
     *
     * @param theHP available hit point
     */
    //NEED CONTROLLER TO PASS THE UPDATE
    public void updateHP(final int theHP) {
        myHitPt.setText(String.valueOf(theHP));
    }

    public void updateHitPoint(int myHitPoints) {
    }
}
