package model;

import java.util.Arrays;

/**
 * Hero is an abstract subclass of DungeonCharacter that represents
 * the player-controlled character in the dungeon adventure game.
 *
 * Heroes have additional abilities such as blocking attacks,
 * maintaining an inventory, tracking their current room,
 * and potentially using special abilities.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public abstract class Hero extends DungeonCharacter {

    private double myChanceToBlock;
    private SpecialAbility mySpecialAbility;
    private Item[] myInventory;
    private Room myCurrentRoom;

    /**
     * Constructs a Hero with the specified attributes.
     *
     * @param theName the name of the hero
     * @param theHP starting hit points
     * @param theMinDamage minimum damage output
     * @param theMaxDamage maximum damage output
     * @param theAttackSpeed attack speed value
     * @param theChanceToHit chance to successfully hit an enemy
     */
    public Hero(final String theName,
                final int theHP,
                final int theMinDamage,
                final int theMaxDamage,
                final int theAttackSpeed,
                final double theChanceToHit) {

        super(theName, theHP, theMinDamage, theMaxDamage, theAttackSpeed, theChanceToHit);

        myInventory = new Item[0];
    }

    /**
     * Returns the chance for this Hero to block incoming attacks.
     *
     * @return chance to block (0.0 to 1.0)
     */
    public double getMyChanceToBlock() {
        return myChanceToBlock;
    }

    /**
     * Sets the chance for this Hero to block incoming attacks.
     *
     * @param theChanceToBlock new block chance value
     */
    public void setMyChanceToBlock(final double theChanceToBlock) {
        myChanceToBlock = theChanceToBlock;
    }

    /**
     * Returns the current room the Hero is in.
     *
     * @return current Room
     */
    public Room getCurrentRoom() {
        return myCurrentRoom;
    }

    /**
     * Sets the current room of the Hero.
     *
     * @param theCurrentRoom the room to set as current
     */
    public void setCurrentRoom(final Room theCurrentRoom) {
        myCurrentRoom = theCurrentRoom;
    }

    /**
     * Returns the Hero's inventory of items.
     *
     * @return array of Item objects
     */
    public Item[] getMyInventory() {
        return myInventory;
    }

    /**
     * Returns the Hero's special ability.
     *
     * @return special ability object
     */
    public SpecialAbility getMySpecialAbility() {
        return mySpecialAbility;
    }

    /**
     * Represents a placeholder for a Hero's special ability.
     * This is intended to be expanded by subclasses.
     */
    private class SpecialAbility {

    }

    public void addItem(final Item[] theItem) {
        if(theItem == null || theItem.length == 0) {
            return;
        }

        Item[] newInventory = Arrays.copyOf(myInventory, myInventory.length + theItem.length);

        for(int i = 0; i < myInventory.length; i++) {
            newInventory[myInventory.length + i] = theItem[i];
        }
        myInventory = newInventory;
    }
}