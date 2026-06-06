/*
 * Dungeon Adventure Game
 * Spring 2026
 */
package model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Room represents a single location within the dungeon.
 *
 * A room may contain items, monsters, and directional
 * connections to adjacent rooms. Rooms may also act as
 * the dungeon entrance or exit and track whether the
 * hero has previously visited them.
 *
 * Each room stores its coordinate position within the
 * dungeon as well as its distance from the entrance.
 *
 * @author Zane Laposky
 * @version 1.3 - 6/5/2026
 */
public class Room implements Serializable
{

    /**
     * serialVersionUID for load and save game
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Symbol used to separate room render rows.
     */
    private static final String ROOM_ROW_SEPARATOR = "%%";

    /**
     * Symbol used for healing potions.
     */
    private static final String HEALING_POTION_SYMBOL = "♥";

    /**
     * Symbol used for vision potions.
     */
    private static final String VISION_POTION_SYMBOL = "⚇";

    /**
     * Symbol used for ogres.
     */
    private static final String OGRE_SYMBOL = "o";

    /**
     * Symbol used for gremlins.
     */
    private static final String GREMLIN_SYMBOL = "g";

    /**
     * Symbol used for skeletons.
     */
    private static final String SKELETON_SYMBOL = "s";

    /**
     * Symbol used for locked doors.
     */
    private static final String LOCKED_DOOR_SYMBOL = "⚿";

    /**
     * Items currently contained in this room.
     */
    private Item[] myItems;

    /**
     * Monsters currently contained in this room.
     */
    private Monster[] myMonsters;

    /**
     * Items required to enter this room.
     */
    private Item[] myEntranceRequirements;

    /**
     * True if this room is the dungeon entrance.
     */
    private boolean myIsEntrance;

    /**
     * True if this room is the dungeon exit.
     */
    private boolean myIsExit;

    /**
     * True if this room has been visited previously.
     */
    private boolean myHasVisitedBefore;

    /**
     * Pillar stored within this room.
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
     * X-coordinate of this room.
     */
    private int myX;

    /**
     * Y-coordinate of this room.
     */
    private int myY;

    /**
     * Coordinate pair representing this room.
     */
    private int[] myCords;

    /**
     * Distance from the dungeon entrance.
     */
    private int myDepth;

    /**
     * Constructs a room with optional entrance requirements.
     *
     * The room initially contains no items, monsters,
     * or adjacent room connections.
     *
     * @param theEntranceRequirements the items required
     *                                to enter the room
     */
    public Room(final Item[] theEntranceRequirements)
    {

        myItems = new Item[0];
        myMonsters = new Monster[0];

        myEntranceRequirements = theEntranceRequirements;

        myIsEntrance = false;
        myIsExit = false;
        myHasVisitedBefore = false;

        myPillar = null;

        myNorthRoom = null;
        mySouthRoom = null;
        myWestRoom = null;
        myEastRoom = null;

        myX = -1;
        myY = -1;

        myCords = new int[] {myX, myY};

        myDepth = 0;
    }

    /**
     * Attempts to enter this room with the specified hero.
     *
     * The hero must possess all required entrance items
     * before entry is permitted.
     *
     * @param theHero the hero attempting to enter
     */
    public void enter(final Hero theHero)
    {

        if (myEntranceRequirements != null)
        {

            for (final Item requiredItem
                    : myEntranceRequirements)
            {

                boolean found = false;

                for (final Item heroItem
                        : theHero.getMyInventory())
                {

                    if (heroItem == requiredItem)
                    {
                        found = true;
                        break;
                    }
                }

                if (!found)
                {

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

    }

    /**
     * Attempts to move the hero through a doorway
     * in the specified direction.
     *
     * @param theHero the hero attempting movement
     * @param theDirection the movement direction
     */
    public void tryDoor(final Hero theHero,
                        final String theDirection)
    {

        if (theDirection.equals("North")
                && myNorthRoom != null)
        {

            myNorthRoom.enter(theHero);

        }
        else if (theDirection.equals("South")
                && mySouthRoom != null)
        {

            mySouthRoom.enter(theHero);

        }
        else if (theDirection.equals("West")
                && myWestRoom != null)
        {

            myWestRoom.enter(theHero);

        }
        else if (theDirection.equals("East")
                && myEastRoom != null)
        {

            myEastRoom.enter(theHero);

        }
        else
        {
        }
    }

    /**
     * Removes all items whose names contain the specified text.
     *
     * @param theItemName the item name fragment to remove
     */
    public void removeItems(final String theItemName)
    {

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
     * Returns a formatted visual representation of this room.
     *
     * The returned string contains three rows separated by
     * a delimiter so the dungeon renderer can reconstruct
     * the full dungeon map.
     *
     * @return the formatted room display string
     */
    @Override
    public String toString()
    {

        final StringBuilder sb = new StringBuilder();

        final boolean northExists = myNorthRoom != null;
        final boolean southExists = mySouthRoom != null;
        final boolean westExists = myWestRoom != null;
        final boolean eastExists = myEastRoom != null;

        // Top row.

        sb.append(getTopLeftCorner());

        if (northExists)
        {

            if (myNorthRoom.getEntranceRequirements() != null)
            {
                sb.append(LOCKED_DOOR_SYMBOL);
            }
            else
            {
                sb.append("-");
            }
        }
        else
        {
            sb.append("═");
        }

        sb.append(getTopRightCorner());

        sb.append(ROOM_ROW_SEPARATOR);

        // Middle row.

        sb.append(westExists ? "|" : "║");

        sb.append(getRoomSymbol());

        sb.append(eastExists ? "|" : "║");

        sb.append(ROOM_ROW_SEPARATOR);

        // Bottom row.

        sb.append(getBottomLeftCorner());

        if (southExists)
        {

            if (mySouthRoom.getEntranceRequirements() != null)
            {
                sb.append(LOCKED_DOOR_SYMBOL);
            }
            else
            {
                sb.append("-");
            }
        }
        else
        {
            sb.append("═");
        }

        sb.append(getBottomRightCorner());

        return sb.toString();
    }

    /**
     * Returns the top-left corner character used when rendering
     * this room.
     *
     * The returned character is based on the existence of
     * adjacent rooms to the north and west. This allows the
     * room border to connect correctly with neighboring rooms
     * when displayed as part of the dungeon map.
     *
     * @return the top-left corner character for the room
     */
    private char getTopLeftCorner() {

        if (myNorthRoom != null && myWestRoom != null) {
            return '╬';
        }
        if (myNorthRoom != null) {
            return '╠';
        }
        if (myWestRoom != null) {
            return '╦';
        }
        return '╔';
    }

    /**
     * Returns the top-right corner character used when rendering
     * this room.
     *
     * The returned character is based on the existence of
     * adjacent rooms to the north and east. This allows the
     * room border to connect correctly with neighboring rooms
     * when displayed as part of the dungeon map.
     *
     * @return the top-right corner character for the room
     */
    private char getTopRightCorner() {

        if (myNorthRoom != null && myEastRoom != null) {
            return '╬';
        }
        if (myNorthRoom != null) {
            return '╣';
        }
        if (myEastRoom != null) {
            return '╦';
        }
        return '╗';
    }

    /**
     * Returns the bottom-left corner character used when rendering
     * this room.
     *
     * The returned character is based on the existence of
     * adjacent rooms to the south and west. This allows the
     * room border to connect correctly with neighboring rooms
     * when displayed as part of the dungeon map.
     *
     * @return the bottom-left corner character for the room
     */
    private char getBottomLeftCorner() {

        if (mySouthRoom != null && myWestRoom != null) {
            return '╬';
        }
        if (mySouthRoom != null) {
            return '╠';
        }
        if (myWestRoom != null) {
            return '╩';
        }
        return '╚';
    }

    /**
     * Returns the bottom-right corner character used when rendering
     * this room.
     *
     * The returned character is based on the existence of
     * adjacent rooms to the south and east. This allows the
     * room border to connect correctly with neighboring rooms
     * when displayed as part of the dungeon map.
     *
     * @return the bottom-right corner character for the room
     */
    private char getBottomRightCorner() {

        if (mySouthRoom != null && myEastRoom != null) {
            return '╬';
        }
        if (mySouthRoom != null) {
            return '╣';
        }
        if (myEastRoom != null) {
            return '╩';
        }
        return '╝';
    }
    /**
     * Returns the display symbol used for this room.
     *
     * @return the room display symbol
     */
    private String getRoomSymbol()
    {

        String roomSymbol = " ";

        if (myIsEntrance)
        {
            roomSymbol = "#";
        }
        else if (myIsExit)
        {
            roomSymbol = "X";
        }
        else if (myItems.length > 0)
        {

            final Item item = myItems[0];

            if (item instanceof VisionPotion)
            {
                roomSymbol = VISION_POTION_SYMBOL;
            }
            else if (item instanceof HealingPotion)
            {
                roomSymbol = HEALING_POTION_SYMBOL;
            }
            else if (item instanceof Pillar)
            {

                final String[] parts =
                        item.getMyName().split(" ");

                roomSymbol = String.valueOf(
                        parts[parts.length - 1].charAt(0)
                );
            }
        }
        else if (myMonsters.length > 0)
        {

            final Monster monster = myMonsters[0];

            if (monster instanceof Ogre)
            {
                roomSymbol = OGRE_SYMBOL;
            }
            else if (monster instanceof Gremlin)
            {
                roomSymbol = GREMLIN_SYMBOL;
            }
            else if (monster instanceof Skeleton)
            {
                roomSymbol = SKELETON_SYMBOL;
            }
        }

        return roomSymbol;
    }

    /**
     * Adds an item to this room.
     *
     * @param theItem the item to add
     */
    public void addItem(final Item theItem)
    {

        myItems = Arrays.copyOf(
                myItems,
                myItems.length + 1
        );

        myItems[myItems.length - 1] = theItem;
    }

    /**
     * Adds a monster to this room.
     *
     * @param theMonster the monster to add
     */
    public void addMonster(final Monster theMonster)
    {

        myMonsters = Arrays.copyOf(
                myMonsters,
                myMonsters.length + 1
        );

        myMonsters[myMonsters.length - 1] =
                theMonster;
    }

    
    /**
     * Removes a monster from this room.
     * Very similar to removeItem in model.Hero
     *
     * @param theMonster the monster to remove
     */
    public void removeMonster(final Monster theMonster) {
        if (theMonster == null || myMonsters == null || myMonsters.length == 0) {
            return;
        }

        Monster[] newMonsters = new Monster[myMonsters.length - 1];
        boolean removed = false;
        int index = 0;

        for (Monster monster : myMonsters) {
            if (!removed && monster == theMonster) {
                removed = true;
            } else {
                if (index < newMonsters.length) {
                    newMonsters[index] = monster;
                    index++;
                }
            }
        }

        if (removed) {
            myMonsters = newMonsters;
        }
    }
    
    /**
     * Removes all items and monsters from this room.
     */
    public void clearRoom()
    {

        myItems = new Item[0];
        myMonsters = new Monster[0];
    }

    /**
     * Returns all items currently contained in this room.
     *
     * @return the room item array
     */
    public Item[] getItems()
    {
        return myItems;
    }

    /**
     * Returns all monsters currently contained in this room.
     *
     * @return the monster array
     */
    public Monster[] getMonsters()
    {
        return myMonsters;
    }

    /**
     * Returns the entrance requirements for this room.
     *
     * @return the required entrance items
     */
    public Item[] getEntranceRequirements()
    {
        return myEntranceRequirements;
    }

    /**
     * Returns the north adjacent room.
     *
     * @return the north room
     */
    public Room getNorthRoom()
    {
        return myNorthRoom;
    }

    /**
     * Returns the south adjacent room.
     *
     * @return the south room
     */
    public Room getSouthRoom()
    {
        return mySouthRoom;
    }

    /**
     * Returns the west adjacent room.
     *
     * @return the west room
     */
    public Room getWestRoom()
    {
        return myWestRoom;
    }

    /**
     * Returns the east adjacent room.
     *
     * @return the east room
     */
    public Room getEastRoom()
    {
        return myEastRoom;
    }

    /**
     * Returns whether this room is the entrance.
     *
     * @return true if this room is the entrance
     */
    public boolean getIsEntrance()
    {
        return myIsEntrance;
    }

    /**
     * Returns whether this room is the exit.
     *
     * @return true if this room is the exit
     */
    public boolean getIsExit()
    {
        return myIsExit;
    }

    /**
     * Returns whether this room has been visited before.
     *
     * @return true if this room has been visited
     */
    public boolean getHasVisitedBefore()
    {
        return myHasVisitedBefore;
    }

    /**
     * Returns the x-coordinate of this room.
     *
     * @return the x-coordinate
     */
    public int getX()
    {
        return myX;
    }

    /**
     * Returns the y-coordinate of this room.
     *
     * @return the y-coordinate
     */
    public int getY()
    {
        return myY;
    }

    /**
     * Returns the coordinate pair of this room.
     *
     * @return the room coordinates
     */
    public int[] getCords()
    {
        return myCords;
    }

    /**
     * Returns the room depth from the dungeon entrance.
     *
     * @return the room depth
     */
    public int getDepth()
    {
        return myDepth;
    }

    /**
     * Sets whether this room is the entrance.
     *
     * @param theIsEntrance true if this room is the entrance
     */
    public void setIsEntrance(
            final boolean theIsEntrance)
    {
        myIsEntrance = theIsEntrance;
    }

    /**
     * Sets whether this room is the exit.
     *
     * @param theIsExit true if this room is the exit
     */
    public void setIsExit(final boolean theIsExit)
    {
        myIsExit = theIsExit;
    }

    /**
     * Sets whether this room has been visited before.
     *
     * @param theHasVisitedBefore the visited state
     */
    public void setHasVisitedBefore(
            final boolean theHasVisitedBefore)
    {
        myHasVisitedBefore = theHasVisitedBefore;
    }

    /**
     * Sets the north adjacent room reference.
     *
     * @param theRoom the north room
     */
    public void setNorthRoom(final Room theRoom)
    {
        myNorthRoom = theRoom;
    }

    /**
     * Sets the south adjacent room reference.
     *
     * @param theRoom the south room
     */
    public void setSouthRoom(final Room theRoom)
    {
        mySouthRoom = theRoom;
    }

    /**
     * Sets the west adjacent room reference.
     *
     * @param theRoom the west room
     */
    public void setWestRoom(final Room theRoom)
    {
        myWestRoom = theRoom;
    }

    /**
     * Sets the east adjacent room reference.
     *
     * @param theRoom the east room
     */
    public void setEastRoom(final Room theRoom)
    {
        myEastRoom = theRoom;
    }

    /**
     * Sets the x-coordinate of this room.
     *
     * @param theX the x-coordinate
     */
    public void setX(final int theX)
    {
        myX = theX;
    }

    /**
     * Sets the y-coordinate of this room.
     *
     * @param theY the y-coordinate
     */
    public void setY(final int theY)
    {
        myY = theY;
    }

    /**
     * Sets the coordinate position of this room.
     *
     * @param theX the x-coordinate
     * @param theY the y-coordinate
     */
    public void setCords(final int theX,
                         final int theY)
    {

        myX = theX;
        myY = theY;

        myCords = new int[] {myX, myY};
    }

    /**
     * Sets the room depth from the dungeon entrance.
     *
     * @param theDepth the room depth
     */
    public void setDepth(final int theDepth)
    {
        myDepth = theDepth;
    }

    /**
     * Returns the adjacent room in the specified direction.
     *
     * @param theDirection the direction to search
     * @return the adjacent room, or null if none exists
     */
    public Room getDirection(
            final Direction theDirection)
    {

        Room room = null;

        switch (theDirection)
        {

            case NORTH ->
                    room = myNorthRoom;

            case SOUTH ->
                    room = mySouthRoom;

            case WEST ->
                    room = myWestRoom;

            case EAST ->
                    room = myEastRoom;

            default ->
            {
                room = null;
            }
        }

        return room;
    }

    /**
     * Creates a bidirectional connection between
     * this room and another room.
     *
     * @param theDirection the connection direction
     * @param theRoom the room to connect
     */
    public void setConnection(
            final Direction theDirection,
            final Room theRoom)
    {

        switch (theDirection)
        {

            case NORTH ->
            {
                myNorthRoom = theRoom;
                theRoom.setSouthRoom(this);
            }

            case SOUTH ->
            {
                mySouthRoom = theRoom;
                theRoom.setNorthRoom(this);
            }

            case WEST ->
            {
                myWestRoom = theRoom;
                theRoom.setEastRoom(this);
            }

            case EAST ->
            {
                myEastRoom = theRoom;
                theRoom.setWestRoom(this);
            }

            default ->
            {

            }
        }
    }

    /**
     * Returns whether this room is empty.
     *
     * A room is considered empty if it contains
     * no items, monsters, entrance marker,
     * exit marker, or pillar.
     *
     * @return true if the room is empty
     */
    public boolean isEmpty()
    {

        return myItems.length == 0
                && myMonsters.length == 0
                && !myIsEntrance
                && !myIsExit
                && myPillar == null;
    }

    /**
     * Returns whether this room is a valid exit room.
     *
     * A valid exit room must connect to at least one
     * adjacent empty room.
     *
     * @return true if this room is a valid exit
     */
    public boolean isValidExit()
    {

        boolean returnValue = false;

        for (final Direction direction
                : Direction.values())
        {

            final Room room =
                    getDirection(direction);

            if (room != null && room.isEmpty())
            {
                returnValue = true;
            }
        }

        return returnValue;
    }
}
