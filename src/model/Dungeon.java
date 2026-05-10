/*
 * Dungeon Adventure Game
 * Spring 2026
 */
package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * Dungeon represents a randomly generated collection of interconnected
 * rooms used within the dungeon adventure game.
 *
 * The dungeon is generated recursively beginning from a single entrance
 * room. Rooms are connected through directional references representing
 * north, south, east, and west movement. The class is also responsible
 * for determining the dungeon entrance, the farthest room used as the
 * exit, and generating a formatted visual representation of the dungeon.
 *
 * @author Zane Laposky
 * @version 1.1
 */
public class Dungeon {

    /**
     * The starting room of the dungeon.
     */
    private Room myEntrance;

    /**
     * The exit room of the dungeon.
     */
    private Room myExit;

    /**
     * The collection of pillars that must be placed in the dungeon.
     */
    private Pillar[] myPillarsToPlace;

    /**
     * Tracks the total number of generated rooms.
     */
    private int myIterationCount;

    /**
     * Stores the greatest room depth discovered during generation.
     */
    private int myMaxDistance;

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
    private static final Pillar PILLAR_OF_INHERITENCE =
            new Pillar("Pillar of Inheritance");

    /**
     * Pillar representing polymorphism.
     */
    private static final Pillar PILLAR_OF_POLYMORPHISM =
            new Pillar("Pillar of Polymorphism");

    /**
     * Probability used when generating items.
     */
    private static final double CHANCE_TO_GENERATE_ITEM = .05;

    /**
     * Probability used when generating monsters.
     */
    private static final double CHANCE_TO_GENERATE_MONSTER = .3;

    /**
     * The hero's current horizontal position.
     */
    private int myHeroX;

    /**
     * The hero's current vertical position.
     */
    private int myHeroY;

    /**
     * Determines the overall dungeon size.
     */
    private int myDifficulty;

    /**
     * Constructs a dungeon using the specified difficulty level.
     *
     * The difficulty determines the approximate number of rooms
     * generated within the dungeon.
     *
     * @param theDifficulty the dungeon difficulty level
     */
    public Dungeon(final int theDifficulty) {

        myHeroX = 0;
        myHeroY = 0;
        myDifficulty = theDifficulty;

        myEntrance = new Room(null);
        myEntrance.setIsEntrance(true);
        myEntrance.setCords(0, 0);
        myEntrance.setDepth(0);

        myMaxDistance = 0;

        myPillarsToPlace = new Pillar[]{
                PILLAR_OF_ENCAPSULATION,
                PILLAR_OF_ABSTRACTION,
                PILLAR_OF_INHERITENCE,
                PILLAR_OF_POLYMORPHISM
        };

        myIterationCount = 0;

        generateDungeon(myEntrance, new Random());

        myExit.setIsExit(true);
        myExit.clearRoom();

        generateItems();
    }

    /**
     * Entry point used for testing dungeon generation.
     */
    public static void main() {

        final Dungeon dungeon = new Dungeon(3);

        System.out.println(dungeon.toString());
    }

    /**
     * Randomly generates items and monsters throughout the dungeon.
     */
    private void generateItems() {

    }

    /**
     * Recursively generates connected dungeon rooms.
     *
     * Each recursive call either creates a new adjacent room or
     * continues traversal into an existing neighboring room.
     * The room with the greatest depth from the entrance becomes
     * the dungeon exit.
     *
     * @param theCurrentRoom the current room being processed
     * @param theRandom the random number generator
     */
    public void generateDungeon(final Room theCurrentRoom,
                                final Random theRandom) {

        if (theCurrentRoom == null) {
            return;
        }

        if (!theCurrentRoom.getIsEntrance()
                && theCurrentRoom.getDepth() > myMaxDistance) {

            myMaxDistance = theCurrentRoom.getDepth();
            myExit = theCurrentRoom;
        }

        if (myIterationCount >= myDifficulty * myDifficulty) {
            return;
        }

        final int tempNum = theRandom.nextInt(4);

        if (tempNum == 0) {

            if (theCurrentRoom.getNorthRoom() == null) {

                myIterationCount++;

                final Room newRoom = new Room(null);

                newRoom.setDepth(theCurrentRoom.getDepth() + 1);
                newRoom.setSouthRoom(theCurrentRoom);

                newRoom.setCords(
                        theCurrentRoom.getX(),
                        theCurrentRoom.getY() + 1
                );

                theCurrentRoom.setNorthRoom(newRoom);

                generateDungeon(newRoom, theRandom);

            } else {

                generateDungeon(
                        theCurrentRoom.getNorthRoom(),
                        theRandom
                );
            }

        } else if (tempNum == 1) {

            if (theCurrentRoom.getSouthRoom() == null) {

                myIterationCount++;

                final Room newRoom = new Room(null);

                newRoom.setDepth(theCurrentRoom.getDepth() + 1);
                newRoom.setNorthRoom(theCurrentRoom);

                newRoom.setCords(
                        theCurrentRoom.getX(),
                        theCurrentRoom.getY() - 1
                );

                theCurrentRoom.setSouthRoom(newRoom);

                generateDungeon(newRoom, theRandom);

            } else {

                generateDungeon(
                        theCurrentRoom.getSouthRoom(),
                        theRandom
                );
            }

        } else if (tempNum == 2) {

            if (theCurrentRoom.getWestRoom() == null) {

                myIterationCount++;

                final Room newRoom = new Room(null);

                newRoom.setDepth(theCurrentRoom.getDepth() + 1);
                newRoom.setEastRoom(theCurrentRoom);

                newRoom.setCords(
                        theCurrentRoom.getX() - 1,
                        theCurrentRoom.getY()
                );

                theCurrentRoom.setWestRoom(newRoom);

                generateDungeon(newRoom, theRandom);

            } else {

                generateDungeon(
                        theCurrentRoom.getWestRoom(),
                        theRandom
                );
            }

        } else {

            if (theCurrentRoom.getEastRoom() == null) {

                myIterationCount++;

                final Room newRoom = new Room(null);

                newRoom.setWestRoom(theCurrentRoom);
                newRoom.setDepth(theCurrentRoom.getDepth() + 1);

                newRoom.setCords(
                        theCurrentRoom.getX() + 1,
                        theCurrentRoom.getY()
                );

                theCurrentRoom.setEastRoom(newRoom);

                generateDungeon(newRoom, theRandom);

            } else {

                generateDungeon(
                        theCurrentRoom.getEastRoom(),
                        theRandom
                );
            }
        }
    }

    /**
     * Returns a formatted visual representation of the dungeon.
     *
     * The dungeon is rendered row by row using each room's
     * individual string representation.
     *
     * @return the formatted dungeon layout
     */
    @Override
    public String toString() {

        if (myEntrance == null) {
            return "Dungeon not generated.";
        }

        final HashMap<String, Room> roomMap = new HashMap<>();
        final HashSet<Room> visitedRooms = new HashSet<>();

        populateRoomMap(myEntrance, roomMap, visitedRooms);

        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (final String key : roomMap.keySet()) {

            final String[] split = key.split(",");

            final int x = Integer.parseInt(split[0]);
            final int y = Integer.parseInt(split[1]);

            if (x > maxX) {
                maxX = x;
            }

            if (x < minX) {
                minX = x;
            }

            if (y > maxY) {
                maxY = y;
            }

            if (y < minY) {
                minY = y;
            }
        }

        final StringBuilder sb = new StringBuilder();

        for (int y = maxY; y >= minY; y--) {

            final StringBuilder top = new StringBuilder();
            final StringBuilder mid = new StringBuilder();
            final StringBuilder bot = new StringBuilder();

            for (int x = minX; x <= maxX; x++) {

                final Room room = roomMap.get(x + "," + y);

                if (room == null) {

                    top.append("    ");
                    mid.append("    ");
                    bot.append("    ");

                } else {

                    final String[] parts =
                            room.toString().split("%%");

                    if (parts.length == 3) {

                        top.append(parts[0]).append(" ");
                        mid.append(parts[1]).append(" ");
                        bot.append(parts[2]).append(" ");

                    } else {

                        top.append("*** ");
                        mid.append(" ?  ");
                        bot.append("*** ");
                    }
                }
            }

            sb.append(top).append("\n");
            sb.append(mid).append("\n");
            sb.append(bot).append("\n\n");
        }

        return sb.toString();
    }

    /**
     * Traverses all connected rooms and stores them in a map
     * using their coordinates as keys.
     *
     * @param theRoom the current room being processed
     * @param theMap the map storing discovered rooms
     * @param theVisitedRooms the set of previously visited rooms
     */
    private void populateRoomMap(final Room theRoom,
                                 final HashMap<String, Room> theMap,
                                 final HashSet<Room> theVisitedRooms) {

        if (theRoom == null
                || theVisitedRooms.contains(theRoom)) {

            return;
        }

        theVisitedRooms.add(theRoom);

        final String key =
                theRoom.getX() + "," + theRoom.getY();

        theMap.put(key, theRoom);

        populateRoomMap(
                theRoom.getNorthRoom(),
                theMap,
                theVisitedRooms
        );

        populateRoomMap(
                theRoom.getSouthRoom(),
                theMap,
                theVisitedRooms
        );

        populateRoomMap(
                theRoom.getWestRoom(),
                theMap,
                theVisitedRooms
        );

        populateRoomMap(
                theRoom.getEastRoom(),
                theMap,
                theVisitedRooms
        );
    }

    /**
     * Validates the integrity of dungeon room connections.
     */
    private void validateDungeon() {

    }

    /**
     * Moves the hero within the dungeon.
     *
     * @param theXChange the horizontal movement amount
     * @param theYChange the vertical movement amount
     */
    public void moveHero(final int theXChange,
                         final int theYChange) {

    }

    /**
     * Returns the hero's current horizontal position.
     *
     * @return the hero x-coordinate
     */
    public int getX() {
        return myHeroX;
    }

    /**
     * Returns the hero's current vertical position.
     *
     * @return the hero y-coordinate
     */
    public int getY() {
        return myHeroY;
    }
}