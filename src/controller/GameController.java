package controller;

import model.*;
import view.GameView;
import view.StatsPanel;

/**
 * GameController is responsible for starting the first version of the game.
 *
 * This controller creates the hero, creates the starting room, connects the
 * DungeonController to the view, and prepares the game so the player can begin.
 *
 * In this first version, the game automatically starts with a Warrior.
 * Later versions could allow the player to choose between Warrior, Thief,
 * and Priestess.
 *
 * @author Devin Riel
 * @version 1.0
 */
public class GameController {

    /**
     * The hero controlled by the player.
     */
    private Hero myHero;

    /**
     * The first room the hero starts in.
     */
    private Room myStartingRoom;

    /**
     * The controller that handles keyboard input and dungeon actions.
     */
    private DungeonController myDungeonController;

    /**
     * The main game view that displays the game window.
     */
    private GameView myGameView;

    /**
     * Constructs a GameController and immediately starts the game.
     */
    public GameController() {
        startGame();
    }

    /**
     * Starts the game by creating the hero, creating the starting room,
     * and connecting the controller to the game view.
     */
    private void startGame() {
        createHero();
        createStartingRoom();
        connectControllerToGame();
    }

    /**
     * Creates the player's hero.
     *
     * In this first version, the player is automatically assigned
     * a Warrior character.
     */
    private void createHero() {
        myHero = new Warrior("Warrior");
    }

    /**
     * Creates the starting room for the hero.
     *
     * The hero is placed inside this room, and the room is told that
     * the hero has entered it.
     */
    private void createStartingRoom() {
        myStartingRoom = new Room(null);
        myHero.setCurrentRoom(myStartingRoom);
        myStartingRoom.enter(myHero);
    }

    /**
     * Connects the controller, view, and stats panel together.
     *
     * This method creates the stats panel, creates the dungeon controller,
     * creates the game view, and adds the dungeon controller as a key listener
     * so the player can control the hero with the keyboard.
     */
    private void connectControllerToGame() {
        /*
         * Creates the stats panel using the hero's name.
         */
        StatsPanel statsPanel = new StatsPanel(myHero.getMyName());

        /*
         * Creates the dungeon controller and gives it access to the hero
         * and the stats panel so it can update the game state.
         */
        myDungeonController = new DungeonController(myHero, statsPanel);
        /*
         * Creates the main game view.
         */
        myGameView = new GameView();

        myGameView.getFrame().addKeyListener(myDungeonController);
        myGameView.getFrame().setFocusable(true);
        myGameView.getFrame().requestFocusInWindow();
    }

    /**
     * Gets the current hero.
     *
     * @return the hero controlled by the player
     */
    public Hero getHero() {
        return myHero;
    }

    /**
     * Gets the dungeon controller.
     *
     * @return the controller that handles dungeon input and actions
     */
    public DungeonController getDungeonController() {
        return myDungeonController;
    }
}
