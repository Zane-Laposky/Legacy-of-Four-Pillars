package view;

import javax.swing.*;
import java.awt.*;

public class GameView {
    JFrame myFrame;
    private String characterName;

    public GameView() {

        //set up general window dimension
        myFrame = new JFrame("Legacy of Four Pillars");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(500, 600);
        myFrame.setMinimumSize(new Dimension(300, 400));
        myFrame.setResizable(true);

        gameTypePrompt();

        //initial GUI
        initGuiComponent();

        // must be last to make everything visible
        myFrame.setVisible(true);
    }

    //GUI initial component
    private void initGuiComponent() {
        JPanel myGamePanel = new DungeonPanel().getPanel();
        JPanel statsPanel = new StatsPanel(characterName).getPanel();
        JPanel inventoryPanel = new InventoryPanel().getPanel();

        JSplitPane myMainPanel = getJSplitPane(statsPanel, inventoryPanel, myGamePanel);

        myFrame.add(myMainPanel,  BorderLayout.CENTER);
        myFrame.setJMenuBar(new GameMenuBar().getMenuBar());

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

    //start of the game ask if it is new or load game
    private void gameTypePrompt() {
        Object[] options = {"New Game", "Load Game"};
        int choice = JOptionPane.showOptionDialog(myFrame,
                "Would you like to start a new game or load a game?",
                "Welcome to Legacy of Four Pillars!", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        if (choice == JOptionPane.YES_OPTION) {
            //prompt for character name in popup window
            characterName = namePrompt();
        }
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

    public Component getFrame() {
        return myFrame;
    }
}