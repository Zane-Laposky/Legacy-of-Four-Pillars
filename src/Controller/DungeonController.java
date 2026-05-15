package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
public class DungeonController implements KeyListener {

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
    private PlayerWrapper playerWrapper;
    /**
     * The Stats panel from the view.
     * This allows the controller to update the displayed hit points.
     */
    private StatsPanel myStatsPanel;

    /**
     * Constructs the first version of the DungeonController.
     *
     * This connects the selected hero and the stats panel to the controller.
     * It also checks what type of hero was chosen so the correct special
     * ability can be used later.
     *
     * @param theHero the hero controlled by the player
     * @param theStatsPanel the stats panel that displays hero information
     */
    public DungeonController(final Hero theHero, final StatsPanel theStatsPanel) {
        myHero = theHero;
        playerWrapper = new PlayerWrapper(myHero);
        myStatsPanel = theStatsPanel;

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
     * @param e the key event created when the player presses a key
     */
    @Override
    public void keyPressed(KeyEvent e) {

        /*
         * In this first version, the player cannot leave the room
         * while there are still living monsters inside.
         */
        if(roomHasLivingMonsters()){
            return;
        }

        /*
         * Move west using A or the left arrow key.
         */
        if (e.getKeyCode() == KeyEvent.VK_A ||  e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveHero("West");
            updateStatsPanel();
        }

        /*
         * Move east using D or the right arrow key.
         */
        if (e.getKeyCode() == KeyEvent.VK_D ||  e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveHero("East");
            updateStatsPanel();
        }

        /*
         * Move north using W or the up arrow key.
         */
        if (e.getKeyCode() == KeyEvent.VK_W ||  e.getKeyCode() == KeyEvent.VK_UP) {
            moveHero("North");
            updateStatsPanel();
        }

        /*
         * Move south using S or the down arrow key.
         */
        if (e.getKeyCode() == KeyEvent.VK_S ||  e.getKeyCode() == KeyEvent.VK_DOWN) {
            moveHero("South");
            updateStatsPanel();
        }

        /*
         * Pick up all items in the current room.
         */
        if (e.getKeyCode() == KeyEvent.VK_E) {
            pickUpItems();
            updateStatsPanel();
        }

        /*
         * Use the hero's special ability.
         */
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            useSpecialAbility();
            updateStatsPanel();
        }

        /*
         * Use the hero's basic attack.
         */
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            useBasicAttack();
            updateStatsPanel();
        }

        /*
         * Placeholder for using a healing item.
         * This can be implemented in a later version.
         */
        if(e.getKeyCode() == KeyEvent.VK_H){
            //HEAL
        }

        /*
         * Placeholder for using a vision potion.
         * This can be implemented in a later version.
         */
        if(e.getKeyCode() == KeyEvent.VK_V){
            //INCREASED VISION
        }

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

    /**
     * Makes the hero attack the first living monster in the room.
     */
    private void useBasicAttack() {
        Monster monster = activeLivingMonster();
        /*
         * The hero cannot attack if there are no living monsters.
         */
        if(monster == null) {
            System.out.println("There is no living monster");
            return;
        }

        /*
         * Hero attacks the monster.
         */
        myHero.attack(monster);
        updateStatsPanel();
        System.out.println(myHero.getMyName() + " attacks " + monster.getMyName());

        /*
         * Handles what happens after the hero attacks.
         */
        afterHeroAttacks(monster);
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
        }
    }

    /**
     * Updates the current room reference after the hero moves.
     */
    private void updateCurrentRoom() {
        if(myHero != null) {
            myRoom = myHero.getCurrentRoom();
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
     * Updates the stats panel with the hero's current hit points.
     */
    private void updateStatsPanel() {
        if(myStatsPanel != null && playerWrapper != null) {
            myStatsPanel.updateHitPoint(myHero.getMyHitPoints());
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
}