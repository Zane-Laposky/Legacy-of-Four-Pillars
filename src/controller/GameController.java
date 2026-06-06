package controller;

import model.*;
import view.GameView;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.Optional;

/**
 * GameController is responsible for starting the first version of the game.
 * <p>
 * This controller creates the hero, creates the starting room, connects the
 * DungeonController to the view, and prepares the game so the player can begin.
 * <p>
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

    /**
     * Handles saving and loading player and dungeon data.
     */
    private Persistence myPersistence;

    /**
     * The name entered by the player during hero creation.
     */
    private String myHeroName;

    /**
     * The hero class chosen by the player during hero creation.
     */
    private String myHeroType;

    /**
     * Constructs a GameController and immediately starts the game.
     */
    public GameController() {
        myPersistence = new Persistence();
        startGame();
    }


    /**
     * Starts the game by creating the view, creating the hero,
     * creating the starting room, and connecting the controller.
     */
    private void startGame() {
        myGameView = new GameView(this);
        myGameView.connectMenuBar(this);
        myGameView.startGamePrompt();
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
        if (myDungeonController != null) {
            myDungeonController.clearMessage();
            myGameView.deleteOldController();
        }
        myDungeonController = new DungeonController(myHero, myDungeon);

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

    /**
     * Loads the saved game if one exists.
     */
    private void loadGame() {
        Optional<PlayerWrapper> saveData = myPersistence.loadPlayerData();

        if (saveData.isEmpty()) {
            myGameView.propertyChange(new PropertyChangeEvent(
                    this, "noSaveFile", null, null));
            return;
        }

        PlayerWrapper loadedData = saveData.get();

        myHero = loadedData.toHero();

        Optional<Dungeon> savedDungeon = myPersistence.loadDungeon();

        if (savedDungeon.isEmpty()) {
            System.out.println("No saved dungeon found.");
            return;
        }

        myDungeon = savedDungeon.get();
        Room loadedRoom = findRoomByCoordinates(
                loadedData.getRoomX(),
                loadedData.getRoomY()
        );

        myStartingRoom = loadedRoom;

        myHero.setCurrentRoom(loadedRoom);
        loadedRoom.enter(myHero);

        createDungeonController();
        System.out.println("Game loaded.");

    }

    /**
     * Finds a room in the dungeon using its saved coordinates.
     *
     * @param theX saved x-coordinate
     * @param theY saved y-coordinate
     * @return matching room, or the entrance if the room cannot be found
     */
    private Room findRoomByCoordinates(final int theX, final int theY) {
        Object roomsObject = myDungeon.getRooms();

        if (roomsObject instanceof java.util.HashMap) {
            java.util.HashMap<String, Room> rooms =
                    (java.util.HashMap<String, Room>) roomsObject;

            String roomKey = theX + "," + theY;

            return rooms.get(roomKey);
        }

        return null;
    }


    /**
     * Receives events from the view.
     * This handles hero creation and menu actions such as save and load.
     *
     * @param theEvent the event sent from the view
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if ("Hero".equals(theEvent.getPropertyName())) {
            String[] heroInfo = (String[]) theEvent.getNewValue();

            myHeroName = heroInfo[0];
            myHeroType = heroInfo[1];

            createHero();
            createStartingRoom();
            createDungeonController();
        }
        if ("menu".equals(theEvent.getPropertyName())
                && "LoadGame".equals(theEvent.getNewValue())) {
            loadGame();
        }
        if ("menu".equals(theEvent.getPropertyName())
                && "SaveGame".equals(theEvent.getNewValue())) {
            myPersistence.savePlayer(myHero);
            myPersistence.saveDungeon(myDungeon);
        }
    }
}
