package model;

/**
 * Represents a Pillar item in the dungeon.
 * Pillars are special collectible items required for game completion.
 * Each pillar is a type of Item with a specific name identifying its role.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class Pillar extends Item {

    /**
     * Constructs a Pillar with a specified name.
     *
     * @param theName the name of the pillar
     */
    public Pillar(final String theName) {
        super(theName);
    }
}