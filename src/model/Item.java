package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Item is an abstract base class representing objects that can be found
 * and used within the dungeon such as potions or special objects.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public abstract class Item implements Serializable {

    /**
     * serialVersionUID for load and save game
     */
    @Serial
    private static final long serialVersionUID = 1L;

    private final String myName;

    /**
     * Constructs an Item with a given name.
     *
     * @param theName the name of the item
     */
    public Item(final String theName) {
        myName = theName;
    }

    /**
     * Returns the name of this item.
     *
     * @return item name
     */
    public String getMyName() {
        return myName;
    }

    /**
     * Returns a string representation of this item.
     *
     * @return item name
     */
    @Override
    public String toString() {
        return myName;
    }
}