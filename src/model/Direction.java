package model;

/**
 * Represents the four possible movement directions in the dungeon.
 * <p>
 * Each direction can calculate the new x or y coordinate that a room would
 * move to if the player travels in that direction.
 *
 * @author Zane Laposky
 */
public enum Direction{
    /**
     * Represents movement upward/north in the dungeon.
     */
    NORTH,

    /**
     * Represents movement right/east in the dungeon.
     */
    EAST,

    /**
     * Represents movement downward/south in the dungeon.
     */
    SOUTH,
    
    /**
     * Represents movement left/west in the dungeon.
     */
    WEST;

    
    /**
     * Calculates the new x-coordinate after moving in this direction.
     * <p>
     * Moving east increases the x-coordinate by 1.
     * Moving west decreases the x-coordinate by 1.
     * Moving north or south does not change the x-coordinate.
     *
     * @param theRoom the current room before movement
     * @return the new x-coordinate after movement
     */
    public int newX(final Room theRoom){
        int toReturn = 0;
        // Change the x-coordinate depending on the direction.
        switch (this){
            case EAST -> toReturn = theRoom.getX() + 1;
            case WEST ->  toReturn = theRoom.getX() - 1;
            default -> toReturn = theRoom.getX();
        }
        return toReturn;
    }

    /**
     * Calculates the new y-coordinate after moving in this direction.
     * <p>
     * Moving north increases the y-coordinate by 1.
     * Moving south decreases the y-coordinate by 1.
     * Moving east or west does not change the y-coordinate.
     *
     * @param theRoom the current room before movement
     * @return the new y-coordinate after movement
     */
    public int newY(final Room theRoom){
        int toReturn = 0;

        // Change the y-coordinate depending on the direction.
        switch (this){
            case NORTH -> toReturn = theRoom.getY() + 1;
            case SOUTH ->  toReturn = theRoom.getY() - 1;
            default -> toReturn = theRoom.getY();
        }
        return toReturn;
    }
}
