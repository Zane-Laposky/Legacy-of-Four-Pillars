package view;

import javax.swing.*;
import java.awt.*;

public class GameView {
    JFrame myFrame;
    private String myHeroName;
    private String myHeroType;

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
        JPanel statsPanel = new StatsPanel(myHeroName).getPanel();
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
            characterTypePrompt();
        } else if (choice == JOptionPane.NO_OPTION) {
            // NEED INFO FROM CONTROLLER to call persistence
        }
    }


    //prompt window for the user to name the character
   private void characterTypePrompt() {
        JPanel chTypePanel = new JPanel();
        chTypePanel.setLayout(new BoxLayout(chTypePanel, BoxLayout.Y_AXIS));

        //User type in name
       JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
       namePanel.add(new JLabel("Hero Name:"));
       JTextField myHeroField = new JTextField(15);
       namePanel.add(myHeroField);

        //select hero type
       JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
       buttonPanel.add(new JLabel("Choose your hero type:"));
       JRadioButton warrior = new JRadioButton("Warrior");
       JRadioButton priestess = new JRadioButton("Priestess");
       JRadioButton thief = new JRadioButton("Thief");

       //checkbox style
       ButtonGroup bGroup = new ButtonGroup();
       bGroup.add(warrior);
       bGroup.add(priestess);
       bGroup.add(thief);
       warrior.setSelected(true);

       buttonPanel.add(warrior);
       buttonPanel.add(priestess);
       buttonPanel.add(thief);

       chTypePanel.add(namePanel);
       chTypePanel.add(buttonPanel);

       int result = JOptionPane.showConfirmDialog(
               myFrame, chTypePanel, "New Hero",  JOptionPane.OK_CANCEL_OPTION);
       if (result == JOptionPane.OK_OPTION) {
           myHeroName = myHeroField.getText().trim();
           if (myHeroName.isEmpty()) {
               characterTypePrompt();
           }
           if (warrior.isSelected()) {
                myHeroType = "Warrior";
           } else if (priestess.isSelected()) {
               myHeroType = "Priestess";
           } else {
               myHeroType = "Thief";
           }
       } else if (result == JOptionPane.CANCEL_OPTION) {
           gameTypePrompt();
       } else {
           System.exit(0);
       }
    }

    public String getHeroName() {
        return myHeroName;
    }

    public String getHeroType() {
        return myHeroType;
    }
}