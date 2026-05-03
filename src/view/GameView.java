package view;

import javax.swing.*;
import java.awt.*;

public class GameView {
    JFrame myFrame;
    private final String characterName;

    public GameView() {

        //set up general window dimension
        myFrame = new JFrame("Legacy of Four Pillars");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(500, 500);
        myFrame.setMinimumSize(new Dimension(300, 300));
        myFrame.setResizable(true);

        //prompt for character name in popup window
        characterName = namePrompt();

        //initial GUI
        initGuiComponent();

        // must be last to make everything visible
        myFrame.setVisible(true);
    }

    //GUI initial component
    private void initGuiComponent() {
        JPanel myGamePanel = new JPanel();
        JPanel statsPanel = new StatsPanel(characterName).getPanel();
        JPanel inventoryPanel = new InventoryPanel().getPanel();

        JSplitPane myMainPanel = getJSplitPane(statsPanel, inventoryPanel, myGamePanel);

        myFrame.add(myMainPanel,  BorderLayout.CENTER);
        myFrame.setJMenuBar(new MenuBar().getMenuBar());

    }

    //split the window so 3/4 is gamePanel and bottom split between stats and inventory
    private JSplitPane getJSplitPane(JPanel statsPanel, JPanel inventoryPanel, JPanel myGamePanel) {
        JSplitPane myBottomPanel = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, statsPanel, inventoryPanel);
        myBottomPanel.setDividerSize(1);
        myBottomPanel.setResizeWeight(0.5);
        myBottomPanel.setEnabled(false);

        JSplitPane myMainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        myMainPanel.setTopComponent(myGamePanel);
        myMainPanel.setBottomComponent(myBottomPanel);
        myMainPanel.setDividerSize(1);
        myMainPanel.setResizeWeight(0.9);
        myMainPanel.setEnabled(false);
        return myMainPanel;
    }


    //prompt window for the user to name the character
    public String namePrompt() {
        String myCharacterName = null;
        while (myCharacterName == null || myCharacterName.trim().isEmpty()) {
            myCharacterName = JOptionPane.showInputDialog(
                    myFrame,
                    "Name your character: ",
                    "Character Name",
                    JOptionPane.QUESTION_MESSAGE
            );
        }
        return myCharacterName;
    }

}