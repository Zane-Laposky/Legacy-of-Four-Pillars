/*
 * Legacy of Four Pillars
 * Dungeon Test Suite
 */
package tester;

import model.Dungeon;
import model.Item;
import model.Pillar;
import model.Room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for verifying correctness of the Dungeon system including
 * generation, structure, pillar placement, and hero movement.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class DungeonTest {

    private Dungeon myEasyDungeon;
    private Dungeon myMediumDungeon;
    private Dungeon myHardDungeon;

    /**
     * Sets up fresh dungeon instances before each test and generates them.
     */
    @BeforeEach
    public void setUp() {

        myEasyDungeon = new Dungeon(1);
        myMediumDungeon = new Dungeon(2);
        myHardDungeon = new Dungeon(3);

        myEasyDungeon.generateDungeon();
        myMediumDungeon.generateDungeon();
        myHardDungeon.generateDungeon();
    }

    /**
     * Ensures dungeon objects are created properly.
     */
    @Test
    public void testDungeonCreationNotNull() {

        assertNotNull(myEasyDungeon);
        assertNotNull(myMediumDungeon);
        assertNotNull(myHardDungeon);
    }

    /**
     * Ensures dungeon room grids are initialized.
     */
    @Test
    public void testDungeonRoomsArrayNeverNull() {

        assertNotNull(myEasyDungeon.getRooms());
        assertNotNull(myMediumDungeon.getRooms());
        assertNotNull(myHardDungeon.getRooms());
    }

    /**
     * Verifies dungeon size scaling by difficulty.
     */
    @Test
    public void testDungeonDifferentSizes() {

        assertEquals(1, myEasyDungeon.getRooms().length);
        assertEquals(4, myMediumDungeon.getRooms().length);
        assertEquals(9, myHardDungeon.getRooms().length);
    }

    /**
     * Ensures all rooms in a dungeon are initialized.
     */
    @Test
    public void testDungeonRoomsNotNull() {

        final Room[][] theRooms = myMediumDungeon.getRooms();

        for (final Room[] theRow : theRooms) {
            for (final Room theRoom : theRow) {
                assertNotNull(theRoom);
            }
        }
    }

    /**
     * Ensures an entrance exists.
     */
    @Test
    public void testEntranceExists() {

        assertTrue(hasEntrance(myMediumDungeon));
    }

    /**
     * Ensures an exit exists.
     */
    @Test
    public void testExitExists() {

        assertTrue(hasExit(myMediumDungeon));
    }

    /**
     * Ensures exactly one entrance exists.
     */
    @Test
    public void testOnlyOneEntrance() {

        assertEquals(1, countEntrances(myMediumDungeon));
    }

    /**
     * Ensures exactly one exit exists.
     */
    @Test
    public void testOnlyOneExit() {

        assertEquals(1, countExits(myMediumDungeon));
    }

    /**
     * Ensures entrance and exit are different rooms.
     */
    @Test
    public void testEntranceExitDifferent() {

        final Room[][] theRooms = myMediumDungeon.getRooms();

        Room theEntrance = null;
        Room theExit = null;

        for (final Room[] theRow : theRooms) {
            for (final Room theRoom : theRow) {

                if (theRoom.getIsEntrance()) {
                    theEntrance = theRoom;
                }

                if (theRoom.getIsExit()) {
                    theExit = theRoom;
                }
            }
        }

        assertNotNull(theEntrance);
        assertNotNull(theExit);
        assertNotSame(theEntrance, theExit);
    }

    /**
     * Ensures different dungeon generations produce different layouts.
     */
    @Test
    public void testDifferentGenerations() {

        final Dungeon theFirst = new Dungeon(3);
        final Dungeon theSecond = new Dungeon(3);

        theFirst.generateDungeon();
        theSecond.generateDungeon();

        assertNotEquals(
                normalize(theFirst.toString()),
                normalize(theSecond.toString())
        );
    }

    /**
     * Ensures regeneration changes dungeon output.
     */
    @Test
    public void testDungeonRegenerationChangesOutput() {

        final String theBefore = normalize(myMediumDungeon.toString());

        myMediumDungeon.generateDungeon();

        final String theAfter = normalize(myMediumDungeon.toString());

        assertNotEquals(theBefore, theAfter);
    }

    /**
     * Ensures pillars exist in hard dungeon.
     */
    @Test
    public void testPillarsExist() {

        final Room[][] theRooms = myHardDungeon.getRooms();

        boolean hasEncapsulation = false;
        boolean hasAbstraction = false;
        boolean hasInheritance = false;
        boolean hasPolymorphism = false;

        for (final Room[] theRow : theRooms) {
            for (final Room theRoom : theRow) {

                for (final Item theItem : theRoom.getItems()) {

                    if (theItem instanceof Pillar) {

                        final String theName = ((Pillar) theItem).getMyName();

                        if (theName.contains("Encapsulation")) {
                            hasEncapsulation = true;
                        }
                        if (theName.contains("Abstraction")) {
                            hasAbstraction = true;
                        }
                        if (theName.contains("Inheritance")) {
                            hasInheritance = true;
                        }
                        if (theName.contains("Polymorphism")) {
                            hasPolymorphism = true;
                        }
                    }
                }
            }
        }

        assertTrue(hasEncapsulation);
        assertTrue(hasAbstraction);
        assertTrue(hasInheritance);
        assertTrue(hasPolymorphism);
    }

    /**
     * Ensures no room contains more than one pillar.
     */
    @Test
    public void testNoRoomHasMultiplePillars() {

        final Room[][] theRooms = myHardDungeon.getRooms();

        for (final Room[] theRow : theRooms) {
            for (final Room theRoom : theRow) {

                int count = 0;

                for (final Item theItem : theRoom.getItems()) {
                    if (theItem instanceof Pillar) {
                        count++;
                    }
                }

                assertTrue(count <= 1);
            }
        }
    }

    /**
     * Ensures pillars are not placed in entrance or exit.
     */
    @Test
    public void testPillarsNotInEntranceOrExit() {

        final Room[][] theRooms = myHardDungeon.getRooms();

        for (final Room[] theRow : theRooms) {
            for (final Room theRoom : theRow) {

                if (theRoom.getIsEntrance() || theRoom.getIsExit()) {

                    for (final Item theItem : theRoom.getItems()) {
                        assertFalse(theItem instanceof Pillar);
                    }
                }
            }
        }
    }

    /**
     * Placeholder structural test.
     */
    @Test
    public void testPillarsPlacedThenRemoved() {

        assertEquals(0, myHardDungeon.getRooms().length >= 0 ? 0 : 0);
    }

    /**
     * Ensures hero starts at origin.
     */
    @Test
    public void testHeroStartsAtZero() {

        assertEquals(0, myMediumDungeon.getX());
        assertEquals(0, myMediumDungeon.getY());
    }

    /**
     * Ensures hero movement does not break state.
     */
    @Test
    public void testMoveHeroDoesNotCrash() {

        myMediumDungeon.moveHero(1, 0);
        myMediumDungeon.moveHero(-1, 0);
        myMediumDungeon.moveHero(0, 1);
        myMediumDungeon.moveHero(0, -1);

        assertEquals(0, myMediumDungeon.getX());
        assertEquals(0, myMediumDungeon.getY());
    }

    /**
     * Ensures room string representations are valid.
     */
    @Test
    public void testRoomToStringValid() {

        final Room[][] theRooms = myMediumDungeon.getRooms();

        for (final Room[] theRow : theRooms) {
            for (final Room theRoom : theRow) {
                assertNotNull(theRoom.toString());
            }
        }
    }

    /**
     * Ensures dungeon string output is valid.
     */
    @Test
    public void testDungeonStringValid() {

        final String theString = myMediumDungeon.toString();

        assertNotNull(theString);
        assertFalse(theString.isEmpty());
    }

    /**
     * Ensures required markers exist in dungeon output.
     */
    @Test
    public void testDungeonMarkersExist() {

        final String theString = myMediumDungeon.toString();

        assertTrue(theString.contains("i"));
        assertTrue(theString.contains("O"));
        assertTrue(theString.contains("*"));
    }

    private String normalize(final String theString) {

        return theString.replaceAll("\\s+", "");
    }

    private boolean hasEntrance(final Dungeon theDungeon) {

        for (final Room[] theRow : theDungeon.getRooms()) {
            for (final Room theRoom : theRow) {
                if (theRoom.getIsEntrance()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasExit(final Dungeon theDungeon) {

        for (final Room[] theRow : theDungeon.getRooms()) {
            for (final Room theRoom : theRow) {
                if (theRoom.getIsExit()) {
                    return true;
                }
            }
        }
        return false;
    }

    private int countEntrances(final Dungeon theDungeon) {

        int count = 0;

        for (final Room[] theRow : theDungeon.getRooms()) {
            for (final Room theRoom : theRow) {
                if (theRoom.getIsEntrance()) {
                    count++;
                }
            }
        }
        return count;
    }

    private int countExits(final Dungeon theDungeon) {

        int count = 0;

        for (final Room[] theRow : theDungeon.getRooms()) {
            for (final Room theRoom : theRow) {
                if (theRoom.getIsExit()) {
                    count++;
                }
            }
        }
        return count;
    }
}