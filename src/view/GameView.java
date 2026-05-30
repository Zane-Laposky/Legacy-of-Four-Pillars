/*
 * Main GUI of the Game.
 * Spring 2026
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import controller.DungeonController;

/**
 * GameView represents the main GUI for the game.
 * It creates and organizes the panels, dialogs, menus, and window layout.
 *
 * @author Emily Hernandez updated by Zane Laposky
 * @version 1.1 Spring 2026
 */
public class GameView implements PropertyChangeListener {

    /**
     * Window title.
     */
    private static final String WINDOW_TITLE = "Legacy of Four Pillars";

    /**
     * Default frame width.
     */
    private static final int FRAME_WIDTH = 600;

    /**
     * Default frame height.
     */
    private static final int FRAME_HEIGHT = 700;

    /**
     * Minimum frame width.
     */
    private static final int MIN_FRAME_WIDTH = 550;

    /**
     * Minimum frame height.
     */
    private static final int MIN_FRAME_HEIGHT = 600;

    /**
     * Top-level divider location.
     */
    private static final double MAIN_DIVIDER_LOCATION = 0.75d;

    /**
     * Bottom divider location.
     */
    private static final double BOTTOM_DIVIDER_LOCATION = 0.40d;

    /**
     * Resize weight for the left bottom split pane.
     */
    private static final double LEFT_BOTTOM_RESIZE_WEIGHT = 0.30d;

    /**
     * Resize weight for the top split pane.
     */
    private static final double TOP_RESIZE_WEIGHT = 0.90d;

    /**
     * Resize weight for the bottom split pane.
     */
    private static final double BOTTOM_RESIZE_WEIGHT = 0.50d;

    /**
     * Resize weight for the main split pane.
     */
    private static final double MAIN_RESIZE_WEIGHT = 0.95d;

    /**
     * Property name for message updates.
     */
    private static final String PROPERTY_MESSAGE = "message";

    /**
     * Property name for menu actions.
     */
    private static final String PROPERTY_MENU = "menu";

    /**
     * Property name for win events.
     */
    private static final String PROPERTY_WON = "won";

    /**
     * Property name for loss events.
     */
    private static final String PROPERTY_LOST = "lost";

    /**
     * Property name for hero creation events.
     */
    private static final String PROPERTY_HERO = "Hero";

    /**
     * Property name for health point updates.
     */
    private static final String PROPERTY_HP = "HP";

    /**
     * Property name for maximum health point updates.
     */
    private static final String PROPERTY_MAX_HP = "MaxHP";

    /**
     * Property name for healing potion updates.
     */
    private static final String PROPERTY_HEALING_POTION = "HealingPotion";

    /**
     * Property name for vision potion updates.
     */
    private static final String PROPERTY_VISION_POTION = "VisionPotion";

    /**
     * Property name for pillar updates.
     */
    private static final String PROPERTY_PILLAR = "Pillar";

    /**
     * Property name for room updates.
     */
    private static final String PROPERTY_ROOM = "room";

    /**
     * Menu value for a new game.
     */
    private static final String NEW_GAME_VALUE = "NewGame";

    /**
     * Menu value for loading a game.
     */
    private static final String LOAD_GAME_VALUE = "LoadGame";

    /**
     * Default hero type.
     */
    private static final String DEFAULT_HERO_TYPE = "Warrior";

    /**
     * Message displayed when the player wins.
     */
    private static final String WIN_MESSAGE = "Congratulations! You won!\n";

    /**
     * Message displayed when the player loses.
     */
    private static final String LOSE_MESSAGE = "Good Game\n";

    /**
     * Prompt title shown when the game ends.
     */
    private static final String GAME_OVER_TITLE = "Game Over";

    /**
     * Prompt title shown when selecting a new hero.
     */
    private static final String NEW_HERO_TITLE = "New Hero";

    /**
     * Prompt title shown when choosing game type.
     */
    private static final String WELCOME_TITLE = "Welcome to Legacy of Four Pillars!";

    /**
     * Main application window.
     */
    private JFrame myFrame;

    /**
     * Hero name entered by the player.
     */
    private String myHeroName = "";

    /**
     * Hero type selected by the player.
     */
    private String myHeroType = DEFAULT_HERO_TYPE;

    /**
     * Main split pane for the full window.
     */
    private JSplitPane myMainPanel;

    /**
     * Bottom split pane containing status and controls.
     */
    private JSplitPane myBottomPanel;

    /**
     * Panel that displays messages to the player.
     */
    private JPanel myMessagePanel;

    /**
     * Label used to show player messages.
     */
    private JLabel myMessageLabel;

    /**
     * Menu bar wrapper for the game menu.
     */
    private GameMenuBar myGameMenuBar;

    /**
     * Main dungeon view panel.
     */
    private DungeonPanel myDungeonPanel;

    /**
     * Statistics panel.
     */
    private StatsPanel myStatsPanel;

    /**
     * Inventory panel.
     */
    private InventoryPanel myInventoryPanel;

    /**
     * Control panel containing action buttons.
     */
    private ControlPanel myControlPanel;

    /**
     * Indicates whether the player has won.
     */
    private boolean myPlayerWon;

    /**
     * Property change support used to notify listeners.
     */
    private final PropertyChangeSupport myChangeSupport = new PropertyChangeSupport(this);

    /**
     * Constructs the game window using no controller listener.
     */
    public GameView() {
        this(null);
    }

    /**
     * Constructs the game window and optionally connects the controller.
     *
     * @param theController the controller to register with this view
     */
    public GameView(final PropertyChangeListener theController) {
        myPlayerWon = false;

        if (theController != null) {
            myChangeSupport.addPropertyChangeListener(theController);
        }

        initFrameLayout();
        gameTypePrompt();
        initGuiComponent();

        myFrame.setVisible(true);
        myMainPanel.setDividerLocation(MAIN_DIVIDER_LOCATION);
        myBottomPanel.setDividerLocation(BOTTOM_DIVIDER_LOCATION);
    }

    /**
     * Sets up the game window's frame properties.
     */
    private void initFrameLayout() {
        myFrame = new JFrame(WINDOW_TITLE);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        myFrame.setMinimumSize(new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT));
        myFrame.setResizable(true);
        myFrame.setLocationRelativeTo(null);
    }

    /**
     * Creates the panels and attaches them to the main window.
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

        splitLayout(myStatsPanel.getPanel(), myInventoryPanel.getPanel(),
                myDungeonPanel.getPanel(), myControlPanel.getPanel());

        myFrame.add(myMainPanel, BorderLayout.CENTER);

        myGameMenuBar = new GameMenuBar();
        myGameMenuBar.addPropertyChangeListener(this);
        myFrame.setJMenuBar(myGameMenuBar.getMenuBar());
    }

    /**
     * Arranges the game window into the requested split-pane layout.
     *
     * @param theStatsPanel the player status panel
     * @param theInventoryPanel the inventory panel
     * @param theGamePanel the main game panel
     * @param theControlPanel the control panel
     */
    private void splitLayout(final JPanel theStatsPanel,
                             final JPanel theInventoryPanel,
                             final JPanel theGamePanel,
                             final JPanel theControlPanel) {
        final JSplitPane leftBottomPanel = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, theStatsPanel, theInventoryPanel);
        leftBottomPanel.setDividerSize(1);
        leftBottomPanel.setResizeWeight(LEFT_BOTTOM_RESIZE_WEIGHT);
        leftBottomPanel.setMinimumSize(new Dimension(200, 0));
        leftBottomPanel.setEnabled(false);

        final JSplitPane topMainPanel = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, theGamePanel, myMessagePanel);
        topMainPanel.setDividerSize(1);
        topMainPanel.setResizeWeight(TOP_RESIZE_WEIGHT);
        topMainPanel.setEnabled(false);

        myBottomPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftBottomPanel, theControlPanel);
        myBottomPanel.setDividerSize(1);
        myBottomPanel.setResizeWeight(BOTTOM_RESIZE_WEIGHT);
        myBottomPanel.setEnabled(false);

        myMainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        myMainPanel.setTopComponent(topMainPanel);
        myMainPanel.setBottomComponent(myBottomPanel);
        myMainPanel.setDividerSize(1);
        myMainPanel.setResizeWeight(MAIN_RESIZE_WEIGHT);
        myMainPanel.setEnabled(false);
    }

    /**
     * Prompts the player to start a new game or load a saved game.
     */
    private void gameTypePrompt() {
        final Object[] options = {"New Game", "Load Game"};
        final int choice = JOptionPane.showOptionDialog(myFrame,
                "Would you like to start a new game or load a game?",
                WELCOME_TITLE,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            characterTypePrompt();
        } else if (choice == JOptionPane.NO_OPTION) {
            myChangeSupport.firePropertyChange(PROPERTY_MENU, "", LOAD_GAME_VALUE);
        }
    }

    /**
     * Prompts the player for a hero name and hero type.
     */
    private void characterTypePrompt() {
        final JPanel characterTypePanel = new JPanel();
        characterTypePanel.setLayout(new BoxLayout(characterTypePanel, BoxLayout.Y_AXIS));

        final JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Hero Name:"));
        final JTextField heroField = new JTextField(15);
        namePanel.add(heroField);
        heroField.requestFocusInWindow();

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(new JLabel("Choose your hero type:"));
        final JRadioButton warriorButton = new JRadioButton("Warrior");
        final JRadioButton priestessButton = new JRadioButton("Priestess");
        final JRadioButton thiefButton = new JRadioButton("Thief");

        final ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(warriorButton);
        buttonGroup.add(priestessButton);
        buttonGroup.add(thiefButton);
        warriorButton.setSelected(true);

        buttonPanel.add(warriorButton);
        buttonPanel.add(priestessButton);
        buttonPanel.add(thiefButton);

        characterTypePanel.add(namePanel);
        characterTypePanel.add(buttonPanel);

        final int result = JOptionPane.showConfirmDialog(
                myFrame, characterTypePanel, NEW_HERO_TITLE, JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            myHeroName = heroField.getText().trim();

            if (myHeroName.isEmpty()) {
                characterTypePrompt();
            } else {
                if (warriorButton.isSelected()) {
                    myHeroType = "Warrior";
                } else if (priestessButton.isSelected()) {
                    myHeroType = "Priestess";
                } else {
                    myHeroType = "Thief";
                }

                myChangeSupport.firePropertyChange(PROPERTY_HERO, null,
                        new String[] {myHeroName, myHeroType});
            }
        } else if (result == JOptionPane.CANCEL_OPTION) {
            gameTypePrompt();
        } else {
            System.exit(0);
        }
    }

    /**
     * Responds to property changes from the controller or menu bar.
     *
     * @param theEvent the property change event
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        final String propertyName = theEvent.getPropertyName();

        if (PROPERTY_MESSAGE.equals(propertyName)) {
            myMessageLabel.setText((String) theEvent.getNewValue());
        } else if (PROPERTY_MENU.equals(propertyName)
                && NEW_GAME_VALUE.equals(theEvent.getNewValue())) {
            resetGame();
        } else if (PROPERTY_WON.equals(propertyName)) {
            myPlayerWon = true;
            endGame();
        } else if (PROPERTY_LOST.equals(propertyName)) {
            myPlayerWon = false;
            endGame();
        }
    }

    /**
     * Allows other objects to listen for view-generated events.
     *
     * @param theListener the listener to register
     */
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        myChangeSupport.addPropertyChangeListener(theListener);
    }

    /**
     * Connects the controller to the view and registers the map panel.
     *
     * @param theController the dungeon controller
     */
    public void connectController(final DungeonController theController) {
        myControlPanel.addPropertyChangeListener(theController);
        theController.addPropertyChangeListener(myStatsPanel);
        theController.addPropertyChangeListener(myInventoryPanel);
        theController.addPropertyChangeListener(myDungeonPanel);
        theController.addPropertyChangeListener(myControlPanel);
        theController.addPropertyChangeListener(this);

        final MapPanel mapPanel = new MapPanel();
        mapPanel.getPanel().setPreferredSize(new Dimension(220, 0));
        theController.addPropertyChangeListener(mapPanel);
        myFrame.add(mapPanel.getPanel(), BorderLayout.EAST);
        myFrame.revalidate();
        myFrame.repaint();

        myFrame.addKeyListener(theController);
        myFrame.setFocusable(true);
        myFrame.requestFocusInWindow();
    }

    /**
     * Resets the game state and prompts the player to create a new hero.
     */
    private void resetGame() {
        myMessageLabel.setText("");
        myChangeSupport.firePropertyChange(PROPERTY_HP, null, 0);
        myChangeSupport.firePropertyChange(PROPERTY_MAX_HP, null, 0);
        myChangeSupport.firePropertyChange(PROPERTY_HEALING_POTION, null, 0);
        myChangeSupport.firePropertyChange(PROPERTY_VISION_POTION, null, 0);
        myChangeSupport.firePropertyChange(PROPERTY_PILLAR, null, 0);
        myChangeSupport.firePropertyChange(PROPERTY_ROOM, null, null);
        characterTypePrompt();
    }

    /**
     * Displays the end-game prompt and either restarts or exits.
     */
    private void endGame() {
        final String message = myPlayerWon ? WIN_MESSAGE : LOSE_MESSAGE;
        final Object[] options = {"Yes", "No"};
        final int choice = JOptionPane.showOptionDialog(myFrame,
                message + "Play Again",
                GAME_OVER_TITLE,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            gameTypePrompt();
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    /**
     * Returns the main game window.
     *
     * @return the game frame
     */
    public JFrame getFrame() {
        return myFrame;
    }

    /**
     * Testing helper that forwards a property change event.
     *
     * @param theEventName the event name
     * @param theOldValue the old value
     * @param theNewValue the new value
     */
    public void testFireEvent(final String theEventName,
                              final Object theOldValue,
                              final Object theNewValue) {
        myChangeSupport.firePropertyChange(theEventName, theOldValue, theNewValue);
    }
}