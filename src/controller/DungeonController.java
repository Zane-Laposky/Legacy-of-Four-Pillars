package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;

import view.GameView;
import java.util.Arrays;

import view.StatsPanel;
import model.*;

/**
 * DungeonController is the first version of the game controller for
 * Legacy of Four Pillars.
 *
 * This controller handles keyboard input from the player and connects
 * the view to the model. It allows the player to move between rooms,
 * pick up items, use attacks, use special abilities, and update the
 * stats panel when the hero's hit points change.
 *
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
    private PlayerWrapperController playerWrapper;
    /**
     * The Stats panel from the view.
     * This allows the controller to update the displayed hit points.
     */
    private GameView myGameView;


    private final PropertyChangeSupport myChangeSupport;

    /**
     * Constructs the first version of the controller.
     *
     * This connects the selected hero and the stats panel to the controller.
     * It also checks what type of hero was chosen so the correct special
     * ability can be used later.
     *
     * @param theHero the hero controlled by the player
     */
    public DungeonController(final Hero theHero) {
        myHero = theHero;
        playerWrapper = new PlayerWrapperController(myHero);
        myChangeSupport = new PropertyChangeSupport(this);

        //Store a reference to the specific hero type for special abilities
        if(myHero instanceof Warrior){
            myWarrior = (Warrior)myHero;
        } else  if(myHero instanceof Thief){
            myThief = (Thief)myHero;
        }  else  if(myHero instanceof Priestess){
            myPriestess = (Priestess)myHero;
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
        if(myRoom.getMonsters() == null){
            return false;
        }
        for(Monster monster : myRoom.getMonsters()){
            if(monster.isAlive()){
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
         * If the monster died, end combat for this turn.
         */
        if(!theMonster.isAlive()){
            System.out.println(theMonster.getMyName() + " has been vanquished!");

            return;
        }

        /*
         * Monster heals before attacking.
         */
        theMonster.heal();

        /*
         * Monster attacks the hero.
         */
        theMonster.attack(myHero);
        System.out.println(theMonster.getMyName() + " attacks " + myHero.getMyName());

        /*
         * Checks if the hero has been defeated.
         */
        if(!myHero.isAlive()) {
            System.out.println(myHero.getMyName() + " has been defeated!");
        }
    }

    /**
     * Finds the first living monster in the current room.
     *
     * @return the first living monster, or null if none are alive
     */
    private Monster activeLivingMonster() {
        Monster[] monsters = myRoom.getMonsters();

        if(monsters == null){
            return null;
        }

        for(Monster monster : monsters){
            if(monster != null && monster.isAlive()) {
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
        if (roomHasLivingMonsters()){
            System.out.println("There are monsters in the way!");
        } else {
            /*
             * Attempts to move through the door in the chosen direction.
             */
            myRoom.tryDoor(myHero, theDirection);
            /*
             * Updates the controller's room reference after movement.
             */
            updateCurrentRoom();
            System.out.println("An update has occurred");
            //updateView();
        }
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
        if(roomItems == null || roomItems.length == 0) {
            System.out.println("There are no room items to pick up");
            return;
        }
        /*
         * Adds the room's items to the player's inventory.
         */
        playerWrapper.addItemtoInventory(myRoom.getItems());

        System.out.println("Picked up: " + Arrays.toString(roomItems));

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
        String propertyName = theEvent.getPropertyName();

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
        }

        updateView();
    }

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
        if(myHero instanceof Priestess){
            ((Priestess) myHero).healSelf();
        }

        /*
         * If there is no monster, attack-based abilities cannot be used.
         */
        if(monster == null) {
            System.out.println("There is no living monster");
            return;
        }

        /*
         * Uses the correct special ability depending on hero type.
         */
        if(myHero instanceof Warrior){
            ((Warrior) myHero).crushingBlow(monster);
        } else if(myHero instanceof Thief){
            ((Thief) myHero).surpriseAttack(monster);
        }
    }

    private void useBasicAttack() {
        Monster monster = activeLivingMonster();

        if (monster == null) {
            sendMessage("There is no living monster.");
            return;
        }

        myHero.attack(monster);
        sendMessage(myHero.getMyName() + " attacks " + monster.getMyName() + ".");

        afterHeroAttacks(monster);
    }

    private void handlePotion(final String thePotionType) {
        if ("Heal".equals(thePotionType)) {
            useHealingPotion();
        } else if ("Vision".equals(thePotionType)) {
            useVisionPotion();
        }
    }

    private void useHealingPotion() {
        Item[] inventory = myHero.getMyInventory();

        for (Item item : inventory) {
            if (item instanceof HealingPotion) {
                HealingPotion potion = (HealingPotion) item;
                myHero.setMyHitPoints(myHero.getMyHitPoints() + potion.getMyHealAmount());
                sendMessage("Used Healing Potion.");

                myHero.removeItem(item);

                return;
            }
        }

        sendMessage("You do not have a Healing Potion.");
    }

    private void useVisionPotion() {
        Item[] inventory = myHero.getMyInventory();

        for (Item item : inventory) {
            if (item instanceof VisionPotion) {
                myChangeSupport.firePropertyChange("vision", null, myRoom);
                sendMessage("Used Vision Potion.");

                myHero.removeItem(item);

                return;
            }
        }
    }

    private void handleMenu(final String theMenuOption) {
        if ("NewGame".equals(theMenuOption)) {
            sendMessage("New game selected.");
        } else if ("LoadGame".equals(theMenuOption)) {
            sendMessage("Load game selected.");
        }
    }

    private void updateCurrentRoom() {
        myRoom = myHero.getCurrentRoom();
    }

    void updateView() {
        myChangeSupport.firePropertyChange("room", null, myRoom);
        myChangeSupport.firePropertyChange("HP", null, myHero.getMyHitPoints());
        myChangeSupport.firePropertyChange("MaxHP", null, getHeroMaxHP());
        myChangeSupport.firePropertyChange("HealingPotion", null, countHealingPotions());
        myChangeSupport.firePropertyChange("VisionPotion", null, countVisionPotions());
        myChangeSupport.firePropertyChange("Pillar", null, countPillars());
        myChangeSupport.firePropertyChange("grab", null, myRoom.getItems().length > 0);
        myChangeSupport.firePropertyChange("Monster", null, roomHasLivingMonsters());
    }

    private void sendMessage(final String theMessage) {
        myChangeSupport.firePropertyChange("message", null, theMessage);
    }

    private int countHealingPotions() {
        int count = 0;

        for (Item item : myHero.getMyInventory()) {
            if (item instanceof HealingPotion) {
                count++;
            }
        }

        return count;
    }

    private int countVisionPotions() {
        int count = 0;

        for (Item item : myHero.getMyInventory()) {
            if (item instanceof VisionPotion) {
                count++;
            }
        }

        return count;
    }

    private int countPillars() {
        int count = 0;

        for (Item item : myHero.getMyInventory()) {
            if (item instanceof Pillar) {
                count++;
            }
        }

        return count;
    }

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

}
