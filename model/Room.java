package model;

import java.util.Arrays;

/**
 * Represents a single room in the dungeon grid.
 * A room may contain items, monsters, and connections to adjacent rooms.
 * It also manages entrance/exit restrictions and player movement.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class Room {

    private Item[] myItems;
    private boolean myIsEntrance;
    private boolean myIsExit;
    private Pillar myPillar;

    private Room myNorthRoom;
    private Room mySouthRoom;
    private Room myWestRoom;
    private Room myEastRoom;

    private Monster[] myMonsters;
    private boolean myMonsterChecker;

    private Item[] myEntranceRequirements;
    private boolean myHasVisitedBefore;

    private int myX;
    private int myY;

    /**
     * Constructs a Room with optional entrance requirements.
     *
     * @param theEntranceRequirements items required to enter this room
     */
    public Room(final Item[] theEntranceRequirements) {
        myItems = new Item[0];
        myMonsters = new Monster[0];

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
     * Attempts to enter this room with the given hero.
     * Checks entrance requirements before allowing entry.
     *
     * @param theHero hero attempting to enter
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
                    System.out.println("Missing required item: " + requiredItem);
                    return;
                }
            }
        }

        theHero.setCurrentRoom(this);
        myHasVisitedBefore = true;

        System.out.println("Entered room");
    }

    /**
     * Attempts to move the hero through a directional door.
     *
     * @param theHero hero moving
     * @param theDirection direction (North, South, East, West)
     */
    public void tryDoor(final Hero theHero, final String theDirection) {

        if (theDirection.equals("North") && myNorthRoom != null) {
            myNorthRoom.enter(theHero);
        } else if (theDirection.equals("South") && mySouthRoom != null) {
            mySouthRoom.enter(theHero);
        } else if (theDirection.equals("West") && myWestRoom != null) {
            myWestRoom.enter(theHero);
        } else if (theDirection.equals("East") && myEastRoom != null) {
            myEastRoom.enter(theHero);
        } else {
            System.out.println("There is no room in that direction");
        }
    }

    /**
     * Removes items whose name matches the given string.
     *
     * @param theItemName name fragment to remove
     */
    public void removeItems(final String theItemName) {

        myItems = Arrays.stream(myItems)
                .filter(theItem -> !theItem.toString().toLowerCase().contains(theItemName.toLowerCase()))
                .toArray(Item[]::new);
    }

    /**
     * Returns a formatted string representation of the room,
     * including walls, doors, and item indicators.
     *
     * @return formatted room string
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

        if (myItems.length > 0) {

            Item theItem = myItems[0];

            if (myItems.length > 1) {
                sb.append("M");
            } else if (theItem instanceof VisionPotion) {
                sb.append("V");
            } else if (theItem instanceof HealingPotion) {
                sb.append("H");
            } else if (theItem instanceof Pillar) {
                String[] parts = theItem.getMyName().split(" ");
                sb.append(parts[parts.length - 1].charAt(0));
            } else {
                sb.append(" ");
            }

        } else if (myIsExit) {
            sb.append("O");
        } else if (myIsEntrance) {
            sb.append("i");
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
     * Adds an item to the room.
     *
     * @param theItem item to add
     */
    public void addItem(final Item theItem) {
        myItems = Arrays.copyOf(myItems, myItems.length + 1);
        myItems[myItems.length - 1] = theItem;
    }

    /**
     * @return items in the room
     */
    public Item[] getItems() {
        return myItems;
    }

    /**
     * Adds a monster to the room.
     *
     * @param theMonster item to add
     */
    public void addMonster(final Monster theMonster) {
        myMonsters = Arrays.copyOf(myMonsters, myMonsters.length + 1);
        myMonsters[myMonsters.length - 1] = theMonster;
    }

    /**
     * @return monsters in the room
     */
    public Monster [] getMonsters() {
        return myMonsters;
    }

    /**
     * @return true if this is the entrance room
     */
    public boolean getIsEntrance() {
        return myIsEntrance;
    }

    /**
     * @return true if this is the exit room
     */
    public boolean getIsExit() {
        return myIsExit;
    }

    /**
     * @return true if the room has been visited
     */
    public boolean getHasVisitedBefore() {
        return myHasVisitedBefore;
    }

    /**
     * @return north adjacent room
     */
    public Room getNorthRoom() {
        return myNorthRoom;
    }

    /**
     * @return south adjacent room
     */
    public Room getSouthRoom() {
        return mySouthRoom;
    }

    /**
     * @return west adjacent room
     */
    public Room getWestRoom() {
        return myWestRoom;
    }

    /**
     * @return east adjacent room
     */
    public Room getEastRoom() {
        return myEastRoom;
    }

    /**
     * Sets whether this is the entrance room.
     *
     * @param theIsEntrance entrance flag
     */
    public void setIsEntrance(final boolean theIsEntrance) {
        myIsEntrance = theIsEntrance;
    }

    /**
     * Sets whether this is the exit room.
     *
     * @param theIsExit exit flag
     */
    public void setIsExit(final boolean theIsExit) {
        myIsExit = theIsExit;
    }

    /**
     * Sets the north room reference.
     *
     * @param theRoom north room
     */
    public void setNorthRoom(final Room theRoom) {
        myNorthRoom = theRoom;
    }

    /**
     * Sets the south room reference.
     *
     * @param theRoom south room
     */
    public void setSouthRoom(final Room theRoom) {
        mySouthRoom = theRoom;
    }

    /**
     * Sets the west room reference.
     *
     * @param theRoom west room
     */
    public void setWestRoom(final Room theRoom) {
        myWestRoom = theRoom;
    }

    /**
     * Sets the east room reference.
     *
     * @param theRoom east room
     */
    public void setEastRoom(final Room theRoom) {
        myEastRoom = theRoom;
    }

    /**
     * Sets whether the room has been visited.
     *
     * @param theHasVisitedBefore visited flag
     */
    public void setHasVisitedBefore(final boolean theHasVisitedBefore) {
        myHasVisitedBefore = theHasVisitedBefore;
    }

    /**
     * @return x-coordinate in dungeon grid
     */
    public int getX() {
        return myX;
    }

    /**
     * @return y-coordinate in dungeon grid
     */
    public int getY() {
        return myY;
    }

    /**
     * Sets x-coordinate.
     *
     * @param theX x position
     */
    public void setX(final int theX) {
        myX = theX;
    }

    /**
     * Sets y-coordinate.
     *
     * @param theY y position
     */
    public void setY(final int theY) {
        myY = theY;
    }
}