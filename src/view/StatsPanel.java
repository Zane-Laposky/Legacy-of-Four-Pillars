package view;

import javax.swing.*;
import java.awt.*;

public class StatsPanel {
    private static final int MAX_HITPOINT = 100; // NEED UPDATE
    private final JPanel myPanel;
    private final JLabel myHitPt;

    //NEED UPDATE FROM CONTROLLER ON THE INITIAL VALUE
    public StatsPanel(final String theCharacterName/*, final int theHitPoint*/) {
        myPanel = new JPanel();
        myHitPt = new JLabel(String.valueOf(MAX_HITPOINT)); // NEED UPDATE FROM CONTROLLER
        initialPanel(theCharacterName);
    }

    private void initialPanel(final String theCharacterName) {
        myPanel.setBackground(Color.WHITE);
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
        JLabel myTitle = new JLabel("STATS");
        myTitle.setFont(myTitle.getFont().deriveFont(Font.BOLD));
        myPanel.add(myTitle);


        JPanel myNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        myNamePanel.add(new JLabel("Name: " + theCharacterName));

        JPanel myHitPtPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        myHitPtPanel.add(new JLabel("Hit Points: "));
        myHitPtPanel.add(myHitPt);
        myHitPtPanel.add(new JLabel("/" + MAX_HITPOINT));


        myPanel.add(myNamePanel);
        myPanel.add(myHitPtPanel);
    }

    public JPanel getPanel() {
        return myPanel;
    }

    //NEED CONTROLLER TO PASS THE UPDATE
    private void updateHitPoint(final int theHitPoint) {
        myHitPt.setText(String.valueOf(theHitPoint));
    }
}
