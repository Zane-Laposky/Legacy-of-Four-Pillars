package model;

import java.util.Arrays;
import java.util.Random;

/**
 * Dungeon represents a grid-based world made up of interconnected rooms.
 * It is responsible for generating the dungeon layout, placing special items
 * (pillars), and providing a string representation of the full dungeon map.
 *
 * The dungeon is structured as a 2D array of Room objects where each room
 * may have references to adjacent rooms (north, south, east, west).
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class Dungeon {

    private Room[][] myRooms;
    private Pillar[] myPillarsToPlace;

    private static final Pillar PILLAR_OF_ENCAPSULATION =
            new Pillar("Pillar of Encapsulation");
    private static final Pillar PILLAR_OF_ABSTRACTION =
            new Pillar("Pillar of Abstraction");
    private static final Pillar PILLAR_OF_INHERITENCE =
            new Pillar("Pillar of Inheritance");
    private static final Pillar PILLAR_OF_POLYMORPHISM =
            new Pillar("Pillar of Polymorphism");

    private int myHeroX;
    private int myHeroY;
    private int myDifficulty;

    /**
     * Constructs a Dungeon with a given difficulty level.
     * The difficulty determines the overall size of the dungeon.
     *
     * @param theDifficulty the dungeon difficulty/size factor
     */
    public Dungeon(final int theDifficulty) {
        myHeroX = 0;
        myHeroY = 0;
        myDifficulty = theDifficulty;

        myRooms = new Room[myDifficulty * myDifficulty][myDifficulty * myDifficulty];

        myPillarsToPlace = new Pillar[] {
                PILLAR_OF_ENCAPSULATION,
                PILLAR_OF_ABSTRACTION,
                PILLAR_OF_INHERITENCE,
                PILLAR_OF_POLYMORPHISM
        };

        generateDungeon();

        System.out.println(toString());
    }

    /**
     * Entry point for basic dungeon testing.
     */
    public static void main() {
        new Dungeon(4);
    }

    /**
     * Generates the dungeon layout including rooms, room connections,
     * entrance/exit placement, and random pillar placement.
     */
    public void generateDungeon() {

        final int theSize = myDifficulty * myDifficulty;
        final Random theRandom = new Random();

        for (int theX = 0; theX < theSize; theX++) {
            for (int theY = 0; theY < theSize; theY++) {

                myRooms[theX][theY] = new Room(null);

                if (theX == 0 && theY == theSize / 2) {
                    myRooms[theX][theY].setIsEntrance(true);
                }

                if (theX == theSize - 1 && theY == theSize / 2) {
                    myRooms[theX][theY].setIsExit(true);
                }

                if (theX > 0) {
                    myRooms[theX][theY].setNorthRoom(myRooms[theX - 1][theY]);
                }

                if (theX < theSize - 1) {
                    myRooms[theX][theY].setSouthRoom(myRooms[theX + 1][theY]);
                }

                if (theY > 0) {
                    myRooms[theX][theY].setWestRoom(myRooms[theX][theY - 1]);
                }

                if (theY < theSize - 1) {
                    myRooms[theX][theY].setEastRoom(myRooms[theX][theY + 1]);
                }
            }
        }

        while (myPillarsToPlace.length > 0) {

            final int theSizeLimit = myDifficulty * myDifficulty;
            final int theMin = (int)(theSizeLimit * 0.25);
            final int theMax = (int)(theSizeLimit * 0.75);

            final int theAttemptedX = theRandom.nextInt(theMax - theMin) + theMin;
            final int theAttemptedY = theRandom.nextInt(theMax - theMin) + theMin;

            final Room theRoom = myRooms[theAttemptedX][theAttemptedY];

            if (theRoom != null
                    && !theRoom.getIsEntrance()
                    && !theRoom.getIsExit()
                    && theRoom.getItems().length == 0) {

                final Pillar thePillar =
                        myPillarsToPlace[myPillarsToPlace.length - 1];

                myPillarsToPlace =
                        Arrays.copyOf(myPillarsToPlace, myPillarsToPlace.length - 1);

                theRoom.addItem(thePillar);
            }
        }
    }

    /**
     * Returns a formatted string representation of the dungeon.
     * Each room contributes a multi-line representation that is
     * assembled into a full grid display.
     *
     * @return formatted dungeon layout
     */
    @Override
    public String toString() {

        if (myRooms == null) {
            return "Dungeon not generated.";
        }

        final StringBuilder sb = new StringBuilder();

        for (int theX = 0; theX < myDifficulty * myDifficulty; theX++) {

            final StringBuilder theTop = new StringBuilder();
            final StringBuilder theMid = new StringBuilder();
            final StringBuilder theBot = new StringBuilder();

            for (int theY = 0; theY < myDifficulty * myDifficulty; theY++) {

                final String[] theParts =
                        myRooms[theX][theY].toString().split("%%");

                if (theParts.length == 3) {
                    theTop.append(theParts[0]).append(" ");
                    theMid.append(theParts[1]).append(" ");
                    theBot.append(theParts[2]).append(" ");
                }
            }

            sb.append(theTop).append("\n");
            sb.append(theMid).append("\n");
            sb.append(theBot).append("\n");
        }
        return sb.toString();
    }

    /**
     * Validates dungeon structure consistency.
     */
    private void validateDungeon() {
    }

    /**
     * Moves the hero within the dungeon by the given offsets.
     *
     * @param theXChange horizontal movement
     * @param theYChange vertical movement
     */
    public void moveHero(final int theXChange, final int theYChange) {
    }

    /**
     * Returns the hero's current X position.
     *
     * @return hero X coordinate
     */
    public int getX() {
        return myHeroX;
    }

    /**
     * Returns the hero's current Y position.
     *
     * @return hero Y coordinate
     */
    public int getY() {
        return myHeroY;
    }
}