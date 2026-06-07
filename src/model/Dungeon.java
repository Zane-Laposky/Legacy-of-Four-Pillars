/*
 * Dungeon Adventure Game
 * Spring 2026
 */
package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.io.Serializable;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * Dungeon represents a randomly generated collection of
 * interconnected rooms used within the dungeon adventure game.
 *
 * <p>
 * The dungeon begins with a single entrance room and expands
 * recursively by creating connected rooms in random directions.
 * The dungeon also tracks the entrance, exit, hero position,
 * generated rooms, and special pillar objects required for
 * game completion.
 * </p>
 *
 * <p>
 * This class additionally provides functionality for:
 * </p>
 *
 * <ul>
 *     <li>Dungeon generation</li>
 *     <li>Exit room validation</li>
 *     <li>Item generation</li>
 *     <li>Monster generation</li>
 *     <li>Dungeon visualization</li>
 * </ul>
 *
 * @author Zane Laposky
 * @version 1.3 - 5/15/2026
 */
public class Dungeon implements Serializable
{
    /**
     * serialVersionUID for load and save game
     */
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Pillar representing encapsulation.
     */
    private static final Pillar PILLAR_OF_ENCAPSULATION =
            new Pillar("Pillar of Encapsulation");

    /**
     * Pillar representing abstraction.
     */
    private static final Pillar PILLAR_OF_ABSTRACTION =
            new Pillar("Pillar of Abstraction");

    /**
     * Pillar representing inheritance.
     */
    private static final Pillar PILLAR_OF_INHERITANCE =
            new Pillar("Pillar of Inheritance");

    /**
     * Pillar representing polymorphism.
     */
    private static final Pillar PILLAR_OF_POLYMORPHISM =
            new Pillar("Pillar of Polymorphism");

    /**
     * Probability used when generating items.
     */
    private static final double CHANCE_TO_GENERATE_ITEM = 0.1;

    /**
     * Probability used when generating monsters.
     */
    private static final double CHANCE_TO_GENERATE_MONSTER =
            0.1;

    /**
     * Dungeon entrance room.
     */
    private Room myEntrance;

    /**
     * Candidate exit rooms discovered during generation.
     */
    private Room[] myPossibleExits;

    /**
     * Final validated dungeon exit room.
     */
    private Room myExit;

    /**
     * Pillars remaining to be placed in the dungeon.
     */
    private Pillar[] myPillarsToPlace;

    /**
     * Collection of generated dungeon rooms indexed by
     * coordinate key.
     */
    private HashMap<String, Room> myRooms;

    /**
     * Number of rooms generated during dungeon creation.
     */
    private int myIterationCount;

    /**
     * Maximum room depth discovered during generation.
     */
    private int myMaxDistance;

    /**
     * Hero horizontal position.
     */
    private int myHeroX;

    /**
     * Hero vertical position.
     */
    private int myHeroY;

    /**
     * Approximate dungeon size and complexity.
     */
    private int myDifficulty;

    /**
     * Constructs a dungeon using the specified difficulty.
     *
     * <p>
     * The difficulty value controls the approximate number
     * of generated rooms.
     * </p>
     *
     * @param theDifficulty the dungeon difficulty
     */
    public Dungeon(final int theDifficulty)
    {

        myHeroX = 0;
        myHeroY = 0;

        myMaxDistance = 0;
        myIterationCount = 0;

        if (theDifficulty < 1){
            myDifficulty = 1;
        } else {
            myDifficulty = theDifficulty;
        }


        myPillarsToPlace = new Pillar[]
                {
                        PILLAR_OF_POLYMORPHISM,
                        PILLAR_OF_ENCAPSULATION,
                        PILLAR_OF_ABSTRACTION,
                        PILLAR_OF_INHERITANCE
                };

        myRooms = new HashMap<>();

        myEntrance = new Room(null);

        myPossibleExits = new Room[0];

        myRooms.put("0,0", myEntrance);

        myEntrance.setIsEntrance(true);
        myEntrance.setCords(0, 0);
        myEntrance.setDepth(0);

        while (myRooms.keySet().size() < 5)
        {
            generateDungeon(myEntrance, new Random());
        }

        validateExit(new Random());

        generateItems(new Random());
    }

    /**
     * Executes a simple dungeon generation test.
     *
     * @param theArgs command-line arguments
     *
     * @throws IOException if file writing fails
     */
    public static void main(final String[] theArgs)
            throws IOException
    {
        final Dungeon dungeon = new Dungeon(3);

        final Writer writer =
                new PrintWriter("Dungeon.txt");

        writer.write(dungeon.toString());

        writer.close();

        System.out.println(dungeon.toString());
    }

    /**
     * Randomly generates items and monsters throughout
     * the dungeon.
     *
     * <p>
     * Pillars are placed first in valid empty rooms.
     * Remaining empty rooms may then receive either items
     * or monsters based on generation probabilities.
     * </p>
     *
     * @param theRandom random number generator
     */
    private void generateItems(final Random theRandom)
    {
        while (myPillarsToPlace.length > 0
                && myRooms.size() > 6)
        {
            final Room room =
                    (Room) myRooms.values().toArray()
                            [theRandom.nextInt(myRooms.size())];

            final Pillar pillar =
                    myPillarsToPlace[
                            theRandom.nextInt(
                                    myPillarsToPlace.length)];

            if (room.isEmpty() && room.getDepth() < myExit.getDepth())
            {
                room.addItem(pillar);

                myPillarsToPlace =
                        Arrays.stream(myPillarsToPlace)
                                .filter(thePillar ->
                                        thePillar != pillar)
                                .toArray(Pillar[]::new);
            }
        }

        for (final Room room : myRooms.values())
        {
            if (room.isEmpty()
                    && theRandom.nextDouble(0, 1)
                    <= CHANCE_TO_GENERATE_ITEM)
            {
                room.addItem(
                        chooseItem(theRandom));

            }
            if (room.isEmpty()
                    && theRandom.nextDouble(0, 1)
                    <= CHANCE_TO_GENERATE_MONSTER)
            {
                room.addMonster(
                        chooseMonster(theRandom));
            }
        }
    }

    /**
     * Randomly selects a monster type.
     *
     * @param theRandom random number generator
     *
     * @return randomly selected monster
     */
    private Monster chooseMonster(
            final Random theRandom)
    {
        final Monster[] monsters =
                {
                        new Ogre(),
                        new Gremlin(),
                        new Skeleton()
                };

        return monsters[
                theRandom.nextInt(0, monsters.length)];
    }

    /**
     * Randomly selects an item type.
     *
     * @param theRandom random number generator
     *
     * @return randomly selected item
     */
    private Item chooseItem(final Random theRandom)
    {
        final Item[] items =
                {
                        new VisionPotion(),
                        new HealingPotion()
                };

        return items[
                theRandom.nextInt(0, items.length)];
    }

    /**
     * Randomizes the order of all movement directions.
     *
     * @param theRandom random number generator
     *
     * @return shuffled direction array
     */
    private Direction[] shuffleDirections(
            final Random theRandom)
    {
        final Direction[] directions =
                Direction.values();

        for (int i = directions.length - 1;
             i > 0;
             i--)
        {
            final int randomIndex =
                    theRandom.nextInt(i + 1);

            final Direction temporaryDirection =
                    directions[i];

            directions[i] =
                    directions[randomIndex];

            directions[randomIndex] =
                    temporaryDirection;
        }

        return directions;
    }

    /**
     * Selects a valid dungeon exit room from the list
     * of possible exits.
     *
     * <p>
     * A valid exit room must satisfy the room exit
     * validation rules defined within the Room class.
     * </p>
     *
     * @param theRandom random number generator
     */
    private void validateExit(final Random theRandom)
    {
        for (int i = myPossibleExits.length - 1;
             i >= 0;
             i--)
        {
            if (myPossibleExits[i].isValidExit())
            {
                myPossibleExits[i].setIsExit(true);

                myPossibleExits[i].clearRoom();

                myExit = myPossibleExits[i];

                return;
            }
        }
    }

    /**
     * Recursively generates connected dungeon rooms.
     *
     * <p>
     * The generation process creates adjacent rooms
     * in randomized directions while tracking the
     * deepest rooms discovered.
     * </p>
     *
     * @param theCurrentRoom current room being processed
     * @param theRandom random number generator
     */
    private void generateDungeon(
            final Room theCurrentRoom,
            final Random theRandom)
    {
        if (theCurrentRoom == null
                || theCurrentRoom.getDepth() > 1900
                || myIterationCount
                >= myDifficulty * 8)
        {
            return;
        }

        if (!theCurrentRoom.getIsEntrance()
                && theCurrentRoom.getDepth()
                > myMaxDistance)
        {
            myMaxDistance =
                    theCurrentRoom.getDepth();

            myPossibleExits =
                    Arrays.copyOf(
                            myPossibleExits,
                            myPossibleExits.length + 1);

            myPossibleExits[
                    myPossibleExits.length - 1]
                    = theCurrentRoom;
        }

        Direction[] shuffledRooms =
                shuffleDirections(theRandom);

        if (theRandom.nextInt(1, 3) == 1)
        {
            shuffledRooms =
                    Arrays.copyOf(
                            shuffledRooms,
                            theRandom.nextInt(
                                    2,
                                    shuffledRooms.length));
        }
        else
        {
            shuffledRooms =
                    Arrays.copyOf(
                            shuffledRooms,
                            theRandom.nextInt(
                                    1,
                                    shuffledRooms.length));
        }

        for (final Direction direction
                : shuffledRooms)
        {
            final int newX =
                    direction.newX(theCurrentRoom);

            final int newY =
                    direction.newY(theCurrentRoom);

            Room newRoom =
                    theCurrentRoom.getDirection(
                            direction);

            if (newRoom == null
                    && !myRooms.containsKey(
                    newX + "," + newY))
            {
                myIterationCount++;

                newRoom = new Room(null);

                myRooms.put(
                        newX + "," + newY,
                        newRoom);

                theCurrentRoom.setConnection(
                        direction,
                        newRoom);

                newRoom.setCords(newX, newY);

                newRoom.setDepth(
                        theCurrentRoom.getDepth() + 1);

                generateDungeon(
                        newRoom,
                        theRandom);
            }
        }
    }

    /**
     * Returns a formatted visual representation of
     * the dungeon layout.
     *
     * <p>
     * Each room is rendered row-by-row using the
     * string representation provided by the Room class.
     * </p>
     *
     * @return formatted dungeon map
     */
    @Override
    public String toString()
    {
        if (myEntrance == null)
        {
            return "Dungeon not generated.";
        }

        final HashSet<Room> visitedRooms =
                new HashSet<>();

        populateRoomMap(
                myEntrance,
                myRooms,
                visitedRooms);

        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (final String key : myRooms.keySet())
        {
            final String[] splitKey =
                    key.split(",");

            final int x =
                    Integer.parseInt(splitKey[0]);

            final int y =
                    Integer.parseInt(splitKey[1]);

            if (x > maxX)
            {
                maxX = x;
            }

            if (x < minX)
            {
                minX = x;
            }

            if (y > maxY)
            {
                maxY = y;
            }

            if (y < minY)
            {
                minY = y;
            }
        }

        final StringBuilder stringBuilder =
                new StringBuilder();

        for (int y = maxY; y >= minY; y--)
        {
            final StringBuilder top =
                    new StringBuilder();

            final StringBuilder middle =
                    new StringBuilder();

            final StringBuilder bottom =
                    new StringBuilder();

            for (int x = minX; x <= maxX; x++)
            {
                final Room room =
                        myRooms.get(x + "," + y);

                if (room == null)
                {
                    top.append("   ");
                    middle.append("   ");
                    bottom.append("   ");
                }
                else
                {
                    final String[] roomParts =
                            room.toString().split("%%");

                    if (roomParts.length == 3)
                    {
                        top.append(roomParts[0]);
                        middle.append(roomParts[1]);
                        bottom.append(roomParts[2]);
                    }
                    else
                    {
                        top.append("***");
                        middle.append(" ? ");
                        bottom.append("***");
                    }
                }
            }

            stringBuilder.append(top).append("\n");
            stringBuilder.append(middle).append("\n");
            stringBuilder.append(bottom).append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Traverses all connected rooms and stores them
     * within the provided room map.
     *
     * @param theRoom current room being processed
     * @param theMap map storing discovered rooms
     * @param theVisitedRooms previously visited rooms
     */
    private void populateRoomMap(
            final Room theRoom,
            final HashMap<String, Room> theMap,
            final HashSet<Room> theVisitedRooms)
    {
        if (theRoom == null
                || theVisitedRooms.contains(theRoom))
        {
            return;
        }

        theVisitedRooms.add(theRoom);

        final String key =
                theRoom.getX()
                        + ","
                        + theRoom.getY();

        theMap.put(key, theRoom);

        populateRoomMap(
                theRoom.getNorthRoom(),
                theMap,
                theVisitedRooms);

        populateRoomMap(
                theRoom.getSouthRoom(),
                theMap,
                theVisitedRooms);

        populateRoomMap(
                theRoom.getWestRoom(),
                theMap,
                theVisitedRooms);

        populateRoomMap(
                theRoom.getEastRoom(),
                theMap,
                theVisitedRooms);
    }

    /**
     * Moves the hero within the dungeon.
     *
     * @param theXChange horizontal movement amount
     * @param theYChange vertical movement amount
     */
    public void moveHero(
            final int theXChange,
            final int theYChange)
    {
        myHeroX = theXChange + myHeroX;
        myHeroY = theYChange + myHeroY;
    }

    /**
     * Returns the hero horizontal position.
     *
     * @return hero x-coordinate
     */
    public int getX()
    {
        return myHeroX;
    }

    /**
     * Returns the hero vertical position.
     *
     * @return hero y-coordinate
     */
    public int getY()
    {
        return myHeroY;
    }

    
    /**
     * Gets the entrance room of the dungeon.
     *
     * @return the starting entrance room
     */
    public Room getEntrance() {
        return myEntrance;
    }

    /**
     * Return's the dungeon's rooms
     *
     * @return Dungeon's Rooms in a hash map
     */
    public Object getRooms() {
        return myRooms;
    }
}
