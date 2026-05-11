/*
 * Main GUI of the Game.
 * Spring 2026
 */
package view;

import javax.swing.*;
import java.awt.*;

/**
 * GameView is the class which represents main GUI of the game.
 * It creates and organizes panels, menus, dialogs and  window layout for the game.
 *
 * @author Emily Hernandez
 * @version 1.0 Spring 2026
 */
public class GameView {

    /**
     * Main window
     */
    private JFrame myFrame;

    private StatsPanel myStatsPanel;
    private InventoryPanel myInventoryPanel;
    private DungeonPanel myDungeonPanel;
    private ControlPanel myControlPanel;
    /**
     * Hero's name
     */
    private String myHeroName;

    /**
     * Selected Hero's type
     */
    private String myHeroType;

    /**
     * Main view of the Game window
     */
    private JSplitPane myMainPanel;

    /**
     * Bottom view of the Game window
     */
    private JSplitPane myBottomPanel;

    /**
     * Constructs the game window and initialize GUI components
     */
    public GameView() {
        initFrameLayout();
        gameTypePrompt();
        initGuiComponent();

        myFrame.setVisible(true);
        myMainPanel.setDividerLocation(0.75);
        myBottomPanel.setDividerLocation(0.4);
    }

    /**
     * Return the main game frame
     *
     * @return the game frame
     */
    public JFrame getFrame() {
        return myFrame;
    }

    /**
     * Return the player's input hero's name
     *
     * @return the hero name
     */
    public String getHeroName() {
        return myHeroName;
    }


    /**
     * Return the selected hero type
     *
     * @return the hero type
     */
    public String getHeroType() {
        return myHeroType;
    }


    /**
     * Set up the game window's properties such as title, size and close behavior.
     */
    private void initFrameLayout() {
        myFrame = new JFrame("Legacy of Four Pillars");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(500, 600);
        myFrame.setMinimumSize(new Dimension(400, 600));
        myFrame.setResizable(true);
    }

    /**
     * Create all the game panels and adds them to the window.
     */
    private void initGuiComponent() {
        myDungeonPanel = new DungeonPanel();
        myStatsPanel = new StatsPanel(myHeroName);
        myInventoryPanel = new InventoryPanel();
        myControlPanel = new ControlPanel();

        splitLayout(myStatsPanel.getPanel(), myInventoryPanel.getPanel(), myDungeonPanel.getPanel(),
                myControlPanel.getPanel());

        myFrame.add(myMainPanel, BorderLayout.CENTER);
        myFrame.setJMenuBar(new GameMenuBar().getMenuBar());
    }

    /**
     * Arrange the game window into section by putting game map on top, with stats,
     * inventory, and controls split across the bottom.
     *
     * @param statsPanel player status
     * @param inventoryPanel player's inventory
     * @param myGamePanel the game main area
     * @param controlPanel game controls
     */
    private void splitLayout(final JPanel statsPanel, final JPanel inventoryPanel,
                             final JPanel myGamePanel, final JPanel controlPanel) {
        JSplitPane myLeftBottomPanel = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, statsPanel, inventoryPanel);
        myLeftBottomPanel.setDividerSize(1);
        myLeftBottomPanel.setResizeWeight(0.3);
        myLeftBottomPanel.setMinimumSize(new Dimension(200, 0));
        myLeftBottomPanel.setEnabled(false);

        myBottomPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, myLeftBottomPanel, controlPanel);
        myBottomPanel.setDividerSize(1);
        myBottomPanel.setResizeWeight(0.5);
        myBottomPanel.setEnabled(false);

        myMainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        myMainPanel.setTopComponent(myGamePanel);
        myMainPanel.setBottomComponent(myBottomPanel);
        myMainPanel.setDividerSize(1);
        myMainPanel.setResizeWeight(0.95);
        myMainPanel.setEnabled(false);
    }

    /**
     * Create the prompt that lets the player choose between starting a new game or
     * loading a saved one.
     */
    private void gameTypePrompt() {
        Object[] options = {"New Game", "Load Game"};
        int choice = JOptionPane.showOptionDialog(myFrame,
                "Would you like to start a new game or load a game?",
                "Welcome to Legacy of Four Pillars!", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        if (choice == JOptionPane.YES_OPTION) {
            characterTypePrompt();
        } else if (choice == JOptionPane.NO_OPTION) {
            // NEED INFO FROM CONTROLLER to call persistence
        }
    }

    /**
     * Creates popup where the player can enter their hero's name and select
     * the hero type such as warrior, priestess or thief.
     */
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
                myFrame, chTypePanel, "New Hero", JOptionPane.OK_CANCEL_OPTION);
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

    public void updateHP(final int theHP) {
        myStatsPanel.updateHP(theHP);
    }

    public void updateInventory(final int theHealingPotion, final int theVisionPotion,
                                final int thePillars) {
        myInventoryPanel.updateInventory(theHealingPotion, theVisionPotion, thePillars);
    }

    public void updateRoom(final Room theRoom) {
        myDungeonPanel.displayCurrentRoom(theRoom);
    }

    public void enableVisionRooms(final Room theCurrentRoom) {
        myDungeonPanel.enableVisionRooms(theCurrentRoom);
    }
}