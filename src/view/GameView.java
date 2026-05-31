/*
 * Main GUI of the Game.
 * Spring 2026
 */
package view;

import controller.DungeonController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * GameView is the class which represents main GUI of the game.
 * It creates and organizes panels, menus, dialogs and  window layout for the game.
 *
 * @author Emily Hernandez
 * @version 1.0 Spring 2026
 */
public class GameView implements PropertyChangeListener {

    /**
     * Main window
     */
    private JFrame myFrame;

    /**
     * Hero's name
     */
    private String myHeroName;

     /**
     * Hero type selected by the player.
     */
    private String myHeroType = "Warrior";

    /**
     * Main view of the Game window
     */
    private JSplitPane myMainPanel;

    /**
     * Bottom view of the Game window
     */
    private JSplitPane myBottomPanel;

    private JPanel myMessagePanel;
    private JLabel myMessageLabel;
    private GameMenuBar myGameMenuBar;
    private DungeonPanel myDungeonPanel;
    private StatsPanel myStatsPanel;
    private InventoryPanel myInventoryPanel;
    private ControlPanel myControlPanel;
    private Boolean playerWon;

    private final PropertyChangeSupport myChangeSupport;
    /**
     * Constructs the game window and initialize GUI components
     */
    public GameView() {
        this(null);
    }

    public GameView(final PropertyChangeListener theController) {
        myChangeSupport = new PropertyChangeSupport(this);
        playerWon = false;

        if (theController != null) {
            myChangeSupport.addPropertyChangeListener(theController);
        }

        initFrameLayout();
        initGuiComponent();
    }

    /**
     * Set up the game window's properties such as title, size and close behavior.
     */
    private void initFrameLayout() {
        myFrame = new JFrame("Legacy of Four Pillars");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(600, 700);
        myFrame.setMinimumSize(new Dimension(550, 600));
        myFrame.setResizable(true);
        myFrame.setLocationRelativeTo(null);
    }

    public void startGamePrompt() {
        gameTypePrompt();
        myFrame.setVisible(true);
        myMainPanel.setDividerLocation(0.75);
        myBottomPanel.setDividerLocation(0.4);
    }

    /**
     * Create all the game panels and adds them to the window.
     */
    private void initGuiComponent() {
        myDungeonPanel = new DungeonPanel();
        myStatsPanel = new StatsPanel(myHeroName);
        myInventoryPanel = new InventoryPanel();
        myControlPanel = new ControlPanel();
        myMessagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        myMessageLabel = new JLabel("");
        myMessagePanel.add(myMessageLabel);

        myChangeSupport.addPropertyChangeListener(myControlPanel);
        myChangeSupport.addPropertyChangeListener(myStatsPanel);
        myChangeSupport.addPropertyChangeListener(myInventoryPanel);
        myChangeSupport.addPropertyChangeListener(myDungeonPanel);
        myChangeSupport.addPropertyChangeListener(this);

        splitLayout(myStatsPanel.getPanel(), myInventoryPanel.getPanel(), myDungeonPanel.getPanel(),
                myControlPanel.getPanel());

        myFrame.add(myMainPanel, BorderLayout.CENTER);
        myGameMenuBar = new GameMenuBar();
        myGameMenuBar.addPropertyChangeListener(this);
        myFrame.setJMenuBar(myGameMenuBar.getMenuBar());
    }

    /**
     * Arrange the game window into section by putting game map on top, with stats,
     * inventory, and controls split across the bottom.
     *
     * @param statsPanel     player status
     * @param inventoryPanel player's inventory
     * @param myGamePanel    the game main area
     * @param controlPanel   game controls
     */
    private void splitLayout(final JPanel statsPanel, final JPanel inventoryPanel,
                             final JPanel myGamePanel, final JPanel controlPanel) {
        JSplitPane myLeftBottomPanel = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, statsPanel, inventoryPanel);
        myLeftBottomPanel.setDividerSize(1);
        myLeftBottomPanel.setResizeWeight(0.3);
        myLeftBottomPanel.setMinimumSize(new Dimension(200, 0));
        myLeftBottomPanel.setEnabled(false);

        JSplitPane myTopMainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, myGamePanel, myMessagePanel);
        myTopMainPanel.setDividerSize(1);
        myTopMainPanel.setResizeWeight(0.9);
        myTopMainPanel.setEnabled(false);

        myBottomPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, myLeftBottomPanel, controlPanel);
        myBottomPanel.setDividerSize(1);
        myBottomPanel.setResizeWeight(0.5);
        myBottomPanel.setEnabled(false);

        myMainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        myMainPanel.setTopComponent(myTopMainPanel);
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
            //NEED FIXING
            myChangeSupport.firePropertyChange("menu", "", "LoadGame");
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
        myHeroField.requestFocusInWindow(); //ready to type

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
            } else {
                String myHeroType;
                if (warrior.isSelected()) {
                    myHeroType = "Warrior";
                } else if (priestess.isSelected()) {
                    myHeroType = "Priestess";
                } else {
                    myHeroType = "Thief";
                }

                myChangeSupport.firePropertyChange("Hero", null,
                        new String[]{myHeroName, myHeroType});
            }
        } else if (result == JOptionPane.CANCEL_OPTION) {
            gameTypePrompt();
        } else {
            System.exit(0);
        }
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param theEvent A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (theEvent.getPropertyName().equals("message")) {
            myMessageLabel.setText((String) theEvent.getNewValue());
        }
        if (theEvent.getPropertyName().equals("menu")
                && theEvent.getNewValue().equals("NewGame")) {
            resetGame();
        }
        if (theEvent.getPropertyName().equals("won")) {
            playerWon = true;
            endGame();
        }
        if (theEvent.getPropertyName().equals("lost")) {
            playerWon = false;
            endGame();
        }
    }

    /**
     * Connect the controller to the menu bar
     */
    public void connectMenuBar(final PropertyChangeListener theController) {
        myGameMenuBar.addPropertyChangeListener(theController);
    }

    /**
     * Allow controller or other class to listen in on action changes
     */
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        myChangeSupport.addPropertyChangeListener(listener);
    }


    
        /**
     * Connects the main dungeon controller to the view.
     *
     * This method allows the controller to listen to button events
     * from the control panel and allows all view panels to listen
     * to updates from the controller.
     *
     * @param theController the dungeon controller for the game
     */
    public void connectController(final DungeonController theController) {
        /*
         * Allows the controller to receive button actions from the control panel.
         *
         * ControlPanel sends events such as:
         * "move", "attack", "grab", and "potion".
         */
        myControlPanel.addPropertyChangeListener(theController);

        /*
         * Allows the view panels to receive game updates from the controller.
         *
         * StatsPanel listens for:
         * "HP" and "MaxHP".
         *
         * InventoryPanel listens for:
         * "HealingPotion", "VisionPotion", and "Pillar".
         *
         * DungeonPanel listens for:
         * "room" and "vision".
         *
         * ControlPanel listens for:
         * "room", "grab", "HealingPotion", "VisionPotion", and "Monster".
         *
         * GameView listens for:
         * "message".
         */
        theController.addPropertyChangeListener(myStatsPanel);
        theController.addPropertyChangeListener(myInventoryPanel);
        theController.addPropertyChangeListener(myDungeonPanel);
        theController.addPropertyChangeListener(myControlPanel);
        theController.addPropertyChangeListener(this);

        /*
         * Allows keyboard controls to work.
         */
        myFrame.addKeyListener(theController);
        myFrame.setFocusable(true);
        myFrame.requestFocusInWindow();
    }

    /**
     * Reset the game state and allow user to create new hero
     */
    private void resetGame() {
        myMessageLabel.setText("");
        myChangeSupport.firePropertyChange("HP", null, 0);
        myChangeSupport.firePropertyChange("MaxHP", null, 0);
        myChangeSupport.firePropertyChange("HealingPotion", null, 0);
        myChangeSupport.firePropertyChange("VisionPotion", null, 0);
        myChangeSupport.firePropertyChange("Pillar", null, 0);
        characterTypePrompt();
    }

    private void endGame() {
        String message = "";
        if (playerWon) {
            message = "Congratulations! You won!\n";
        } else {
            message = "Good Game\n";
        }
        Object[] options = {"Yes", "No"};
        int choice = JOptionPane.showOptionDialog(myFrame,
                message + "Play Again",
                "Game Over", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        if (choice == JOptionPane.YES_OPTION) {
            gameTypePrompt();
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }

    }


    /**
     * Return the main game frame
     *
     * @return the game frame
     */
    public JFrame getFrame() {
        return myFrame;
    }

    //TESTING PURPOSE ONLY
    public void testFireEvent(String eventName, Object oldValue, Object newValue) {
        myChangeSupport.firePropertyChange(eventName, oldValue, newValue);
    }
}
