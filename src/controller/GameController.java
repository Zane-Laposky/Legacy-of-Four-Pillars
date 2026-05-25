package controller;

import model.*;
import view.GameView;

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
public class GameController implements PropertyChangeListener {

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
     * The randomly generated dungeon map.
     */
    private Dungeon myDungeon;


    private String myHeroName;
    private String myHeroType;

    /**
     * Constructs a GameController and immediately starts the game.
     */
    public GameController() {
        startGame();
    }


    /**
     * Starts the game by creating the view, creating the hero,
     * creating the starting room, and connecting the controller.
     */
    private void startGame() {
        myGameView = new GameView(this);

        createStartingRoom();

        myHero.setCurrentRoom(myStartingRoom);

        myDungeonController = new DungeonController(myHero);

        myGameView.connectController(myDungeonController);

        myDungeonController.propertyChange(
                new PropertyChangeEvent(this, "startup", null, null)
        );
    }


    /**
     * Creates the player's hero based on the type chosen in GameView.
     */
  private void createHero() {
        if (myHeroName == null || myHeroName.isBlank()) {
            myHeroName = "Hero";
        }

        if ("Priestess".equals(myHeroType)) {
            myHero = new Priestess(myHeroName);
        } else if ("Thief".equals(myHeroType)) {
            myHero = new Thief(myHeroName);
        } else {
            myHero = new Warrior(myHeroName);
        }
    }


    /**
     * Creates the starting room using the randomly generated dungeon.
     */
    private void createStartingRoom() {
        /*
         * Creates a random dungeon.
         * The number controls the size/difficulty of the dungeon.
         */
        myDungeon = new Dungeon(5);

        /*
         * The hero starts at the dungeon entrance.
         */
        myStartingRoom = myDungeon.getEntrance();

        myHero.setCurrentRoom(myStartingRoom);
        myStartingRoom.enter(myHero);
    }




    /**
     * Connects the dungeon controller to the game view.
     */
    private void createDungeonController() {
        myDungeonController = new DungeonController(myHero);

        myGameView.connectController(myDungeonController);

        /*
         * Sends the first update so the view displays the starting room,
         * HP, inventory, movement buttons, and combat buttons correctly.
         */
        myDungeonController.updateView();
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
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if ("Hero".equals(theEvent.getPropertyName())) {
            String[] heroInfo = (String[]) theEvent.getNewValue();

            myHeroName = heroInfo[0];
            myHeroType = heroInfo[1];

            createHero();
        }
    }
}
