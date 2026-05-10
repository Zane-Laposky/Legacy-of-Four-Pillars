package model;

import java.util.Arrays;

/**
 * Represents a single room within the dungeon.
 *
 * A room may contain items, monsters, and references to
 * adjacent rooms in the four cardinal directions. Rooms
 * may also be designated as entrance or exit rooms and
 * track whether they have been previously visited.
 *
 * @author Zane Laposky
 * @version 1.2
 */
public class Room {

    /**
     * Items currently contained in this room.
     */
    private Item[] myItems;

    /**
     * True if this room is the dungeon entrance.
     */
    private boolean myIsEntrance;

    /**
     * True if this room is the dungeon exit.
     */
    private boolean myIsExit;

    /**
     * Pillar contained in this room, if present.
     */
    private Pillar myPillar;

    /**
     * Adjacent room located north of this room.
     */
    private Room myNorthRoom;

    /**
     * Adjacent room located south of this room.
     */
    private Room mySouthRoom;

    /**
     * Adjacent room located west of this room.
     */
    private Room myWestRoom;

    /**
     * Adjacent room located east of this room.
     */
    private Room myEastRoom;

    /**
     * Monsters currently contained in this room.
     */
    private Monster[] myMonsters;

    /**
     * Items required to enter this room.
     */
    private Item[] myEntranceRequirements;

    /**
     * True if this room has previously been visited.
     */
    private boolean myHasVisitedBefore;

    /**
     * X-coordinate of this room within the dungeon.
     */
    private int myX;

    /**
     * Y-coordinate of this room within the dungeon.
     */
    private int myY;

    /**
     * Coordinate pair representing this room location.
     */
    private int[] myCords;

    /**
     * Distance from the dungeon entrance.
     */
    private int myDepth;

    /**
     * Constructs a room with optional entrance requirements.
     *
     * The room is initialized with no items, no monsters,
     * no adjacent rooms, and default coordinate values.
     *
     * @param theEntranceRequirements items required to enter the room
     */
    public Room(final Item[] theEntranceRequirements) {

        myItems = new Item[0];
        myMonsters = new Monster[0];
        myDepth = 0;

        myIsEntrance = false;
        myIsExit = false;
        myPillar = null;

        myNorthRoom = null;
        mySouthRoom = null;
        myWestRoom = null;
        myEastRoom = null;

        myEntranceRequirements = theEntranceRequirements;
        myHasVisitedBefore = false;

        myX = -1;
        myY = -1;
    }

    /**
     * Attempts to enter this room with the specified hero.
     *
     * The hero must possess all required entrance items
     * before entering the room successfully.
     *
     * @param theHero the hero attempting to enter the room
     */
    public void enter(final Hero theHero) {

        if (myEntranceRequirements != null) {

            for (Item requiredItem : myEntranceRequirements) {

                boolean found = false;

                for (Item heroItem : theHero.getMyInventory()) {

                    if (heroItem == requiredItem) {
                        found = true;
                        break;
                    }
                }

                if (!found) {

                    System.out.println(
                            "Missing required item: "
                                    + requiredItem
                    );

                    return;
                }
            }
        }

        theHero.setCurrentRoom(this);
        myHasVisitedBefore = true;

        System.out.println("Entered room");
    }

    /**
     * Attempts to move the hero through a doorway
     * in the specified direction.
     *
     * @param theHero the hero attempting movement
     * @param theDirection the movement direction
     */
    public void tryDoor(final Hero theHero,
                        final String theDirection) {

        if (theDirection.equals("North")
                && myNorthRoom != null) {

            myNorthRoom.enter(theHero);

        } else if (theDirection.equals("South")
                && mySouthRoom != null) {

            mySouthRoom.enter(theHero);

        } else if (theDirection.equals("West")
                && myWestRoom != null) {

            myWestRoom.enter(theHero);

        } else if (theDirection.equals("East")
                && myEastRoom != null) {

            myEastRoom.enter(theHero);

        } else {

            System.out.println(
                    "There is no room in that direction"
            );
        }
    }

    /**
     * Removes all items whose names contain the specified text.
     *
     * @param theItemName the item name fragment to remove
     */
    public void removeItems(final String theItemName) {

        myItems = Arrays.stream(myItems)
                .filter(theItem ->
                        !theItem.toString()
                                .toLowerCase()
                                .contains(
                                        theItemName.toLowerCase()
                                )
                )
                .toArray(Item[]::new);
    }

    /**
     * Returns a formatted string representation of this room.
     *
     * The returned string contains wall, doorway,
     * entrance, exit, and item indicators used when
     * rendering the dungeon map.
     *
     * @return the formatted room display string
     */
    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();

        if (myNorthRoom != null) {

            sb.append("*-*");

        } else {

            sb.append("***");
        }

        sb.append("%%");

        if (myWestRoom != null) {

            sb.append("|");

        } else {

            sb.append("*");
        }

        if (myIsEntrance) {

            sb.append("i");

        } else if (myIsExit) {

            sb.append("O");

        } else if (myItems != null
                && myItems.length > 0) {

            Item theItem = myItems[0];

            if (myItems.length > 1) {

                sb.append("M");

            } else if (theItem instanceof VisionPotion) {

                sb.append("V");

            } else if (theItem instanceof HealingPotion) {

                sb.append("H");

            } else if (theItem instanceof Pillar) {

                String[] parts =
                        theItem.getMyName().split(" ");

                sb.append(
                        parts[parts.length - 1].charAt(0)
                );

            } else {

                sb.append(" ");
            }

        } else {

            sb.append(" ");
        }

        if (myEastRoom != null) {

            sb.append("|");

        } else {

            sb.append("*");
        }

        sb.append("%%");

        if (mySouthRoom != null) {

            sb.append("*-*");

        } else {

            sb.append("***");
        }

        return sb.toString();
    }

    /**
     * Adds an item to this room.
     *
     * @param theItem the item to add
     */
    public void addItem(final Item theItem) {

        myItems = Arrays.copyOf(
                myItems,
                myItems.length + 1
        );

        myItems[myItems.length - 1] = theItem;
    }

    /**
     * Returns all items currently contained in this room.
     *
     * @return the room item array
     */
    public Item[] getItems() {
        return myItems;
    }

    /**
     * Returns whether this room is the dungeon entrance.
     *
     * @return true if this room is the entrance
     */
    public boolean getIsEntrance() {
        return myIsEntrance;
    }

    /**
     * Returns whether this room is the dungeon exit.
     *
     * @return true if this room is the exit
     */
    public boolean getIsExit() {
        return myIsExit;
    }

    /**
     * Returns whether this room has previously been visited.
     *
     * @return true if the room has been visited before
     */
    public boolean getHasVisitedBefore() {
        return myHasVisitedBefore;
    }

    /**
     * Returns the room north of this room.
     *
     * @return the north adjacent room
     */
    public Room getNorthRoom() {
        return myNorthRoom;
    }

    /**
     * Returns the room south of this room.
     *
     * @return the south adjacent room
     */
    public Room getSouthRoom() {
        return mySouthRoom;
    }

    /**
     * Returns the room west of this room.
     *
     * @return the west adjacent room
     */
    public Room getWestRoom() {
        return myWestRoom;
    }

    /**
     * Returns the room east of this room.
     *
     * @return the east adjacent room
     */
    public Room getEastRoom() {
        return myEastRoom;
    }

    /**
     * Sets whether this room is the dungeon entrance.
     *
     * @param theIsEntrance true if this room is the entrance
     */
    public void setIsEntrance(final boolean theIsEntrance) {
        myIsEntrance = theIsEntrance;
    }

    /**
     * Sets whether this room is the dungeon exit.
     *
     * @param theIsExit true if this room is the exit
     */
    public void setIsExit(final boolean theIsExit) {
        myIsExit = theIsExit;
    }

    /**
     * Sets the north adjacent room reference.
     *
     * @param theRoom the north room
     */
    public void setNorthRoom(final Room theRoom) {
        myNorthRoom = theRoom;
    }

    /**
     * Sets the south adjacent room reference.
     *
     * @param theRoom the south room
     */
    public void setSouthRoom(final Room theRoom) {
        mySouthRoom = theRoom;
    }

    /**
     * Sets the west adjacent room reference.
     *
     * @param theRoom the west room
     */
    public void setWestRoom(final Room theRoom) {
        myWestRoom = theRoom;
    }

    /**
     * Sets the east adjacent room reference.
     *
     * @param theRoom the east room
     */
    public void setEastRoom(final Room theRoom) {
        myEastRoom = theRoom;
    }

    /**
     * Sets whether this room has been visited previously.
     *
     * @param theHasVisitedBefore the visited state
     */
    public void setHasVisitedBefore(
            final boolean theHasVisitedBefore) {

        myHasVisitedBefore = theHasVisitedBefore;
    }

    /**
     * Returns the x-coordinate of this room.
     *
     * @return the room x-coordinate
     */
    public int getX() {
        return myX;
    }

    /**
     * Returns the y-coordinate of this room.
     *
     * @return the room y-coordinate
     */
    public int getY() {
        return myY;
    }

    /**
     * Sets the x-coordinate of this room.
     *
     * @param theX the x-coordinate
     */
    public void setX(final int theX) {
        myX = theX;
    }

    /**
     * Sets the y-coordinate of this room.
     *
     * @param theY the y-coordinate
     */
    public void setY(final int theY) {
        myY = theY;
    }

    /**
     * Returns all monsters currently contained in this room.
     *
     * @return the monster array
     */
    public Monster[] getMonsters() {
        return myMonsters;
    }

    /**
     * Sets the coordinate position of this room.
     *
     * @param theX the x-coordinate
     * @param theY the y-coordinate
     */
    public void setCords(final int theX,
                         final int theY) {

        myX = theX;
        myY = theY;

        myCords = new int[]{myX, myY};
    }

    /**
     * Returns the coordinate pair of this room.
     *
     * @return the room coordinate array
     */
    public int[] getCords() {
        return myCords;
    }

    /**
     * Adds a monster to this room.
     *
     * @param theMonster the monster to add
     */
    public void addMonster(final Monster theMonster) {

        myMonsters = Arrays.copyOf(
                myMonsters,
                myMonsters.length + 1
        );

        myMonsters[myMonsters.length - 1] =
                theMonster;
    }

    /**
     * Removes all items and monsters from this room.
     */
    public void clearRoom() {

        myMonsters = new Monster[0];
        myItems = new Item[0];
    }

    /**
     * Sets the depth of this room from the dungeon entrance.
     *
     * @param theDepth the room depth
     */
    public void setDepth(final int theDepth) {
        myDepth = theDepth;
    }

    /**
     * Returns the depth of this room from the entrance.
     *
     * @return the room depth
     */
    public int getDepth() {
        return myDepth;
    }
}