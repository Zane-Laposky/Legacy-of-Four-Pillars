package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Timer;
import java.util.LinkedList;
import java.util.Queue;

import view.GameView;

import model.*;

/**
 * DungeonController is the first version of the game controller for
 * Legacy of Four Pillars.
 * <p>
 * This controller handles keyboard input from the player and connects
 * the view to the model. It allows the player to move between rooms,
 * pick up items, use attacks, use special abilities, and update the
 * stats panel when the hero's hit points change.
 * <p>
 * Since this is the first version of the game, some actions such as
 * healing potions and vision potions are still placeholders and can be
 * expanded in later versions.
 *
 * @author Devin Riel
 * @version 1.0
 */
public class DungeonController implements KeyListener, PropertyChangeListener {

    /**
     * The hero currently being controlled by the player.
     */
    private Hero myHero;

    /**
     * The room in which the hero is currently in.
     */
    private Room myRoom;

    /**
     * The dungeon currently being explored by the player.
     * This is used when saving the current dungeon state.
     */
    private Dungeon myDungeon;

    /**
     * Specific hero references used for special abilities.
     * Only one of these will usually be active depending on
     * which hero type the player chooses.
     */
    private Thief myThief;
    private Warrior myWarrior;
    private Priestess myPriestess;

    /**
     * Wrapper object used to help interact with the hero,
     * especially for inventory-related actions
     */
    private PlayerWrapperController myPlayerWrapper;
    /**
     * The Stats panel from the view.
     * This allows the controller to update the displayed hit points.
     */
    private GameView myGameView;

    /**
     * Handles saving and loading game data.
     * In this controller, it is only used by controller save-related helper code.
     */
    private Persistence myPersistence;

    private final PropertyChangeSupport myChangeSupport;

    /**
     * Tracks whether the next room update should show vision potion rooms.
     */
    private boolean myVisionActive;
    
    /**
     * Tracks whether the game has ended.
     */
    private boolean myGameOver;

    /**
     * Tracks whether the Priestess has already healed this turn.
     */
    private boolean myPriestessHealedThisTurn;

    /**
     * Stores messages waiting to be shown to the player.
     */
    private Queue<String> myMessageQueue;

    /**
     * Displays one queued message at a time.
     */
    private Timer myMessageTimer;

    /**
     * Constructs the first version of the controller.
     * <p>
     * This connects the selected hero and the stats panel to the controller.
     * It also checks what type of hero was chosen so the correct special
     * ability can be used later.
     *
     * @param theHero the hero controlled by the player
     */
    public DungeonController(final Hero theHero, final Dungeon theDungeon) {
        myHero = theHero;
        myPlayerWrapper = new PlayerWrapperController(myHero);
        myPersistence = new Persistence();
        myChangeSupport = new PropertyChangeSupport(this);
        myGameOver = false;
        myDungeon = theDungeon;
        myVisionActive = false;
        myPriestessHealedThisTurn = false;

        myMessageQueue = new LinkedList<>();

        myMessageTimer = new Timer(600, theEvent -> {
            if (!myMessageQueue.isEmpty()) {
                String nextMessage = myMessageQueue.poll();

                myChangeSupport.firePropertyChange("message", null, nextMessage);
            }
        });

        myMessageTimer.start();

        //Store a reference to the specific hero type for special abilities
        if (myHero instanceof Warrior) {
            myWarrior = (Warrior) myHero;
        } else if (myHero instanceof Thief) {
            myThief = (Thief) myHero;
        } else if (myHero instanceof Priestess) {
            myPriestess = (Priestess) myHero;
        }

        //Set the starting room based on the heros current room
        myRoom = myHero.getCurrentRoom();
    }


    /**
     * keyTyped is required by KeyListener, but it is not used
     * in this version of the game.
     *
     * @param e the key event
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Handles keyboard input from the player.
     * Movement, attacks, item pickup, and abilities are controlled here.
     *
     * @param theEvent the key event created when the player presses a key
     */
    @Override
    public void keyPressed(final KeyEvent theEvent) {
        if(myGameOver) {
            return;
        }
        int keyCode = theEvent.getKeyCode();

        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            moveHero("North");
        } else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            moveHero("South");
        } else if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            moveHero("West");
        } else if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            moveHero("East");
        } else if (keyCode == KeyEvent.VK_SPACE) {
            useBasicAttack();
        } else if (keyCode == KeyEvent.VK_Q) {
            useSpecialAbility();
        } else if (keyCode == KeyEvent.VK_E) {
            pickUpItems();
        } else if (keyCode == KeyEvent.VK_H) {
            useHealingPotion();
        } else if (keyCode == KeyEvent.VK_V) {
            useVisionPotion();
        }

        updateView();
    }

    /**
     * Checks whether the current room has any living monsters.
     *
     * @return true if at least one monster is alive, false otherwise
     */
    private boolean roomHasLivingMonsters() {
        if (myRoom.getMonsters() == null) {
            return false;
        }
        for (Monster monster : myRoom.getMonsters()) {
            if (monster.isAlive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the monster's response after the hero attacks.
     * If the monster is defeated, it does not attack back.
     *
     * @param theMonster the monster that was attacked
     */
    private void afterHeroAttacks(final Monster theMonster) {
        /*
         * Reset Priestesses heal ability.
         */
        myPriestessHealedThisTurn = false;
        
        /*
         * If the monster died, end combat for this turn.
         */
        if (!theMonster.isAlive()) {
            sendMessage(myHero.getMyName() + " slayed " + theMonster.getMyName() + "!");
            myRoom.removeMonster(theMonster);
            return;
        }

        /*
         * Monster heals before attacking.
         */
        int monsterPreviousHealth = theMonster.getMyHitPoints();
        theMonster.heal();
        int monsterCurrentHealth = theMonster.getMyHitPoints();
        int monsterHealed = monsterCurrentHealth - monsterPreviousHealth;
        if (monsterHealed > 0) {
            sendMessage(theMonster.getMyName() + " healed " + monsterHealed + " health!");
            sendMessage(theMonster.getMyName() + " now has " + theMonster.getMyHitPoints() + " health!");
        }
        /*
         * Monster attacks the hero.
         */
        int playerPreviousHealth = myHero.getMyHitPoints();
        theMonster.attack(myHero);
        int playerNewHealth = myHero.getMyHitPoints();
        int playerDamageTaken = playerPreviousHealth - playerNewHealth;
        if(playerDamageTaken == 0) {
            sendMessage(myHero.getMyName() + " dodged the attack!");
            return;
        }
        sendMessage(theMonster.getMyName() + " attacked " + myHero.getMyName()
                + " and did " + playerDamageTaken + " damage!");

        /*
         * Checks if the hero has been defeated.
         */
        if (!myHero.isAlive()) {
            sendMessage(myHero.getMyName() + " has been defeated!");
            updateView();
            checkGameEnd();
        }
    }

    /**
     * Finds the first living monster in the current room.
     *
     * @return the first living monster, or null if none are alive
     */
    private Monster activeLivingMonster() {
        Monster[] monsters = myRoom.getMonsters();

        if (monsters == null) {
            return null;
        }

        for (Monster monster : monsters) {
            if (monster != null && monster.isAlive()) {
                return monster;
            }
        }
        return null;
    }

    /**
     * Moves the hero in the given direction if there are no living monsters.
     *
     * @param theDirection the direction the hero is trying to move
     */
    private void moveHero(final String theDirection) {
        /*
         * The player cannot leave the room while monsters are alive.
         */
        if (roomHasLivingMonsters()) {
            sendMessage("There are monsters in the way!");
        } else {

            Room oldRoom = myHero.getCurrentRoom();

            /*
             * Attempts to move through the door in the chosen direction.
             */
            oldRoom.tryDoor(myHero, theDirection);

            Room newRoom = myHero.getCurrentRoom();

            /*
             * Updates the controller's room reference after movement.
             */
            updateCurrentRoom();
            if (newRoom != oldRoom) {
                sendMessage("You moved " + theDirection + ".");
            } else {
                sendMessage("You cannot move " + theDirection + ".");
            }

            if (roomHasLivingMonsters()) {
                sendMessage("There is a " + activeLivingMonster().getMyName() + " in this room!");
            }
        }
        checkGameEnd();
    }


    /**
     * Picks up all items from the current room and adds them
     * to the player's inventory.
     */
    private void pickUpItems() {
        Item[] roomItems = myRoom.getItems();

        /*
         * If the room has no items, there is nothing to pick up.
         */
        if (roomItems == null || roomItems.length == 0) {
            sendMessage("There are no items in the room!");
            return;
        }
        /*
         * Adds the room's items to the player's inventory.
         */
        myPlayerWrapper.addItemtoInventory(myRoom.getItems());

        sendMessage(myHero.getMyName() + " picked up " + roomItems[0].getMyName() + "!");

        /*
         * Removes each picked-up item from the room.
         */
        for (Item roomItem : roomItems) {
            myRoom.removeItems(roomItem.getMyName());
        }
    }


    /**
     * keyReleased is required by KeyListener, but it is not used
     * in this version of the game.
     *
     * @param e the key event
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Allows view panels to listen to controller updates.
     *
     * @param theListener the listener being added
     */
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        myChangeSupport.addPropertyChangeListener(theListener);
    }

    /**
     * Receives button/menu events from the view.
     *
     * @param theEvent the property change event
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if(myGameOver) {
            return;
        }
        String propertyName = theEvent.getPropertyName();
        boolean needUpdate = true;

        if ("move".equals(propertyName)) {
            moveHero((String) theEvent.getNewValue());
        } else if ("attack".equals(propertyName)) {
            handleAttack((String) theEvent.getNewValue());
        } else if ("grab".equals(propertyName)) {
            pickUpItems();
        } else if ("potion".equals(propertyName)) {
            handlePotion((String) theEvent.getNewValue());
        } else if ("menu".equals(propertyName)) {
            handleMenu((String) theEvent.getNewValue());
            needUpdate = false;
        }

        if (needUpdate) {
            updateView();
            checkGameEnd();
        }
    }

    /**
     * Handles attack button events from the view.
     * The view sends either "Basic" or "Special", and the controller
     * calls the correct attack method.
     *
     * @param theAttackType the type of attack chosen by the player
     */
    private void handleAttack(final String theAttackType) {
        if ("Basic".equals(theAttackType)) {
            useBasicAttack();
        } else if ("Special".equals(theAttackType)) {
            useSpecialAbility();
        }
    }

    /**
     * Uses the selected hero's special ability.
     * Warriors and Thieves need a monster target.
     * Priestess can heal herself.
     */
    private void useSpecialAbility() {
        Monster monster = activeLivingMonster();

        /*
         * Priestess special ability heals herself.
         */
        if (myHero instanceof Priestess) {
            if(myPriestessHealedThisTurn){
                sendMessage(myHero.getMyName() + " has already healed!");
                return;
            }
            int heroPreviousHealth = myHero.getMyHitPoints();
            ((Priestess) myHero).healSelf();
            int heroNewHealth = myHero.getMyHitPoints();
            myHero.setMyHitPoints(Math.min(heroNewHealth, getHeroMaxHP()));
            int heroHealed = heroNewHealth - heroPreviousHealth;
            sendMessage(myHero.getMyName() + " healed " + heroHealed + " health!");
            myPriestessHealedThisTurn = true;
            return;
        }

        /*
         * If there is no monster, attack-based abilities cannot be used.
         */
        if (monster == null) {
            sendMessage("There is no living monster.");
            return;
        }

        /*
         * Uses the correct special ability depending on hero type.
         */
        if (myHero instanceof Warrior) {
            int monsterPreviousHealth = monster.getMyHitPoints();
            ((Warrior) myHero).crushingBlow(monster);
            int monsterNewHealth = monster.getMyHitPoints();
            int monsterDamageTaken = monsterPreviousHealth - monsterNewHealth;
            if(monsterDamageTaken == 0) {
                sendMessage(monster.getMyName() + " dodged the attack!");
                afterHeroAttacks(monster);
                return;
            }
            sendMessage(myHero.getMyName() + " used Crushing Blow and did " + monsterDamageTaken + " damage!");
            sendMessage(monster.getMyName() + " has " + monster.getMyHitPoints() + " health!");

        } else if (myHero instanceof Thief) {
            int monsterPreviousHealth = monster.getMyHitPoints();
            ((Thief) myHero).surpriseAttack(monster);
            int monsterNewHealth = monster.getMyHitPoints();
            int monsterDamageTaken = monsterPreviousHealth - monsterNewHealth;
            if(monsterDamageTaken == 0) {
                sendMessage(monster.getMyName() + " dodged the attack!");
                afterHeroAttacks(monster);
                return;
            }
            sendMessage(myHero.getMyName() + " used Sneak Attack and did " + monsterDamageTaken + " damage!");
            sendMessage(monster.getMyName() + " has " + monster.getMyHitPoints() + " health!");

        }
        afterHeroAttacks(monster);
    }

    /**
     * Performs a normal hero attack against the first living monster
     * in the current room.
     */
    private void useBasicAttack() {
        Monster monster = activeLivingMonster();

        if (monster == null) {
            sendMessage("There is no living monster.");
            return;
        }
        int monsterPreviousHealth = monster.getMyHitPoints();
        myHero.attack(monster);
        int monsterNewHealth = monster.getMyHitPoints();
        int monsterDamageTaken = monsterPreviousHealth - monsterNewHealth;
        if(monsterDamageTaken == 0) {
            sendMessage(monster.getMyName() + " has dodged the attack!");
            afterHeroAttacks(monster);
            return;
        }
        sendMessage(myHero.getMyName() + " attacks " + monster.getMyName() + " for " + monsterDamageTaken + " damage!");
        sendMessage(monster.getMyName() + " has " + monster.getMyHitPoints() + " health!");

        afterHeroAttacks(monster);
    }

    /**
     * Handles potion button events from the view.
     * The view sends the potion type, and the controller chooses
     * which potion method to call.
     *
     * @param thePotionType the type of potion the player wants to use
     */
    private void handlePotion(final String thePotionType) {
        if ("Heal".equals(thePotionType)) {
            useHealingPotion();
        } else if ("Vision".equals(thePotionType)) {
            useVisionPotion();
        }
    }

    /**
     * Uses one Healing Potion from the hero's inventory.
     * If a potion is found, the hero gains health and the potion is removed.
     */
    private void useHealingPotion() {
        Item[] inventory = myHero.getMyInventory();

        for (Item item : inventory) {
            if (item instanceof HealingPotion) {
                HealingPotion potion = (HealingPotion) item;
                int gainedHealthPoints = potion.getMyHealAmount();
                int healedHitPoints = myHero.getMyHitPoints() + gainedHealthPoints;

                myHero.setMyHitPoints(Math.min(healedHitPoints, getHeroMaxHP()));
                myHero.removeItem(item);
                sendMessage("Gained " + gainedHealthPoints + " health points.");

                return;
            }
        }

        sendMessage("You do not have a Healing Potion.");
    }

    /**
     * Uses one Vision Potion from the hero's inventory.
     * If a potion is found, the controller sends the current room
     * to the view so nearby room information can be displayed.
     */
    private void useVisionPotion() {
        Item[] inventory = myHero.getMyInventory();

        for (Item item : inventory) {
            if (item instanceof VisionPotion) {

                myHero.removeItem(item);

                /*
                 * Tells updateView to show the surrounding rooms instead
                 * of only showing the current room.
                 */
                myVisionActive = true;

                sendMessage("Used Vision Potion.");



                /*
                 * Tells updateView to show the surrounding rooms instead
                 * of only showing the current room.
                 */
                myVisionActive = true;

                sendMessage("Used Vision Potion.");

                return;
            }
        }
        sendMessage("You do not have a Vision Potion.");
    }

    /**
     * Handles menu messages received by the dungeon controller.
     * The actual save and load work is handled by GameController.
     * This method only sends feedback messages to the player.
     *
     * @param theMenuOption the menu option selected by the player
     */
    private void handleMenu(final String theMenuOption) {
        if ("NewGame".equals(theMenuOption)) {
            sendMessage("New game selected.");
        } else if ("LoadGame".equals(theMenuOption)) {
            sendMessage("Load game selected.");
        } else if ("SaveGame".equals(theMenuOption)) {
            sendMessage("Game saved.");
        }
    }

    /**
     * Updates the controller's current room reference to match
     * the hero's current room.
     */
    private void updateCurrentRoom() {
        myRoom = myHero.getCurrentRoom();
    }

    /**
     * Sends the current game state to the view.
     * This updates the room display, hero stats, inventory counts,
     * item pickup button, monster status, and hero name.
     */
    void updateView() {
        updateMessageTimerDelay();

        if (myVisionActive) {
            myChangeSupport.firePropertyChange("vision", null, myRoom);
            myVisionActive = false;
        } else {
            myChangeSupport.firePropertyChange("room", null, myRoom);
        }

        myChangeSupport.firePropertyChange("HP", null, myHero.getMyHitPoints());
        myChangeSupport.firePropertyChange("MaxHP", null, getHeroMaxHP());
        myChangeSupport.firePropertyChange("HealingPotion", null, countHealingPotions());
        myChangeSupport.firePropertyChange("VisionPotion", null, countVisionPotions());
        myChangeSupport.firePropertyChange("Pillar", null, countPillars());
        myChangeSupport.firePropertyChange("grab", null, myRoom.getItems().length > 0);
        myChangeSupport.firePropertyChange("Monster", null, roomHasLivingMonsters());
        myChangeSupport.firePropertyChange("name", null, myHero.getMyName());
    }

    /**
     * Adds a message to the message queue.
     * The timer displays queued messages one at a time.
     *
     * @param theMessage the message to show to the player
     */
    private void sendMessage(final String theMessage) {
        myMessageQueue.add(theMessage);
    }

    /**
     * Counts the number of Healing Potions in the hero's inventory.
     *
     * @return the number of Healing Potions
     */
    private int countHealingPotions() {
        int count = 0;

        for (Item item : myHero.getMyInventory()) {
            if (item instanceof HealingPotion) {
                count++;
            }
        }

        return count;
    }

    /**
     * Counts the number of Vision Potions in the hero's inventory.
     *
     * @return the number of Vision Potions
     */
    private int countVisionPotions() {
        int count = 0;

        for (Item item : myHero.getMyInventory()) {
            if (item instanceof VisionPotion) {
                count++;
            }
        }

        return count;
    }

    /**
     * Counts how many Pillars the hero has collected.
     *
     * @return the number of Pillars in the hero's inventory
     */
    private int countPillars() {
        int count = 0;

        for (Item item : myHero.getMyInventory()) {
            if (item instanceof Pillar) {
                count++;
            }
        }

        return count;
    }

    /**
     * Gets the maximum hit points for the current hero type.
     *
     * @return the hero's maximum hit points
     */
    private int getHeroMaxHP() {
        if (myHero instanceof Warrior) {
            return 125;
        } else if (myHero instanceof Priestess) {
            return 75;
        } else if (myHero instanceof Thief) {
            return 75;
        }

        return myHero.getMyHitPoints();
    }

    /**
     * Checks whether the player has won or lost the game.
     */
    private void checkGameEnd() {
        if (!myGameOver) {
            /*
             * Lose condition:
             * The hero dies.
             */
            if (!myHero.isAlive()) {
                myGameOver = true;
                sendMessage(myHero.getMyName() + " has been defeated!");
                myChangeSupport.firePropertyChange("lost", false, true);
                return;
            }

            /*
             * Win condition:
             * The hero is in the exit room and has all four pillars.
             */
            if (myRoom.getIsExit() && countPillars() == 4) {
                myGameOver = true;
                sendMessage(myHero.getMyName() + " escaped with all four pillars!");
                myChangeSupport.firePropertyChange("won", false, true);
            }
        }
    }

    /**
     * Stop the message timer and clear the message when starting new game
     */
    public void clearMessage() {
        myMessageTimer.stop();
        myMessageQueue.clear();
    }

    /**
     * Remove a listener from the controller
     *
     * @param theListener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener theListener) {
        myChangeSupport.removePropertyChangeListener(theListener);
    }

    /**
     * Changes the message timer delay depending on whether the hero is in combat.
     */
    private void updateMessageTimerDelay() {
        if (roomHasLivingMonsters()) {
            myMessageTimer.setDelay(800);
        } else {
            myMessageTimer.setDelay(600);
        }
    }
}
