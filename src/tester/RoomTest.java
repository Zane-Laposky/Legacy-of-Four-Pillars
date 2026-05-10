/*
 * Legacy of Four Pillars
 * Room test suite for the dungeon project.
 */
package tester;

import java.lang.reflect.Field;

import model.Gremlin;
import model.HealingPotion;
import model.Item;
import model.Monster;
import model.Ogre;
import model.Pillar;
import model.Room;
import model.Skeleton;
import model.VisionPotion;
import model.Warrior;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the Room class.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class RoomTest {

    /**
     * Primary room used by most tests.
     */
    private Room myTestRoom;

    /**
     * Secondary room used for comparison tests.
     */
    private Room myTestRoom2;

    /**
     * Hero used for enter and movement-related tests.
     */
    private Warrior myTestHero;

    /**
     * Sets up fresh test objects before each test case.
     */
    @BeforeEach
    public void setUp() {
        myTestRoom = new Room(null);
        myTestRoom2 = new Room(null);
        myTestHero = new Warrior("Tester");
    }

    /**
     * Verifies that two newly constructed empty rooms have the same string
     * representation.
     */
    @Test
    public void testRoomEmptyGeneration() {
        assertEquals(myTestRoom.toString(), myTestRoom2.toString());
        assertEquals("***%%* *%%***", myTestRoom.toString());
    }

    /**
     * Verifies that all room direction references are null by default.
     */
    @Test
    public void testRoomDirectionsNullByDefault() {
        assertNull(myTestRoom.getNorthRoom());
        assertNull(myTestRoom.getEastRoom());
        assertNull(myTestRoom.getSouthRoom());
        assertNull(myTestRoom.getWestRoom());
    }

    /**
     * Verifies that adjacent room references can be assigned and read back
     * correctly.
     */
    @Test
    public void testRoomDirections() {
        final Room northRoom = new Room(null);
        final Room eastRoom = new Room(null);
        final Room southRoom = new Room(null);
        final Room westRoom = new Room(null);

        myTestRoom.setNorthRoom(northRoom);
        myTestRoom.setEastRoom(eastRoom);
        myTestRoom.setSouthRoom(southRoom);
        myTestRoom.setWestRoom(westRoom);

        assertSame(northRoom, myTestRoom.getNorthRoom());
        assertSame(eastRoom, myTestRoom.getEastRoom());
        assertSame(southRoom, myTestRoom.getSouthRoom());
        assertSame(westRoom, myTestRoom.getWestRoom());

        assertEquals("*-*%%| |%%*-*", myTestRoom.toString());
    }

    /**
     * Verifies that direction references can be cleared by assigning null.
     */
    @Test
    public void testSetNullDirections() {
        myTestRoom.setNorthRoom(new Room(null));
        myTestRoom.setEastRoom(new Room(null));
        myTestRoom.setSouthRoom(new Room(null));
        myTestRoom.setWestRoom(new Room(null));

        myTestRoom.setNorthRoom(null);
        myTestRoom.setEastRoom(null);
        myTestRoom.setSouthRoom(null);
        myTestRoom.setWestRoom(null);

        assertNull(myTestRoom.getNorthRoom());
        assertNull(myTestRoom.getEastRoom());
        assertNull(myTestRoom.getSouthRoom());
        assertNull(myTestRoom.getWestRoom());
        assertEquals("***%%* *%%***", myTestRoom.toString());
    }

    /**
     * Verifies that the entrance flag updates correctly and affects the string
     * representation.
     */
    @Test
    public void testRoomEntrance() {
        myTestRoom.setIsEntrance(true);
        assertTrue(myTestRoom.getIsEntrance());
        assertEquals("***%%*i*%%***", myTestRoom.toString());

        myTestRoom.setIsEntrance(false);
        assertFalse(myTestRoom.getIsEntrance());
        assertEquals("***%%* *%%***", myTestRoom.toString());
    }

    /**
     * Verifies that the exit flag updates correctly and affects the string
     * representation.
     */
    @Test
    public void testRoomExit() {
        myTestRoom.setIsExit(true);
        assertTrue(myTestRoom.getIsExit());
        assertEquals("***%%*O*%%***", myTestRoom.toString());

        myTestRoom.setIsExit(false);
        assertFalse(myTestRoom.getIsExit());
        assertEquals("***%%* *%%***", myTestRoom.toString());
    }

    /**
     * Verifies that the visited flag starts false and can be updated.
     */
    @Test
    public void testRoomVisitedFlag() {
        assertFalse(myTestRoom.getHasVisitedBefore());

        myTestRoom.setHasVisitedBefore(true);
        assertTrue(myTestRoom.getHasVisitedBefore());

        myTestRoom.setHasVisitedBefore(false);
        assertFalse(myTestRoom.getHasVisitedBefore());
    }

    /**
     * Verifies that the room coordinates start at -1 and can be updated.
     */
    @Test
    public void testRoomXAndYCoordinates() {
        assertEquals(-1, myTestRoom.getX());
        assertEquals(-1, myTestRoom.getY());

        myTestRoom.setX(7);
        myTestRoom.setY(3);

        assertEquals(7, myTestRoom.getX());
        assertEquals(3, myTestRoom.getY());
    }

    /**
     * Verifies that adding a vision potion works correctly.
     */
    @Test
    public void testAddItemVisionPotion() {
        final VisionPotion item = new VisionPotion();

        myTestRoom.addItem(item);

        assertEquals(1, myTestRoom.getItems().length);
        assertSame(item, myTestRoom.getItems()[0]);
        assertEquals("***%%*V*%%***", myTestRoom.toString());
    }

    /**
     * Verifies that adding a healing potion works correctly.
     */
    @Test
    public void testAddItemHealingPotion() {
        final HealingPotion item = new HealingPotion();

        myTestRoom.addItem(item);

        assertEquals(1, myTestRoom.getItems().length);
        assertSame(item, myTestRoom.getItems()[0]);
        assertEquals("***%%*H*%%***", myTestRoom.toString());
    }

    /**
     * Verifies that adding a pillar works correctly.
     */
    @Test
    public void testAddItemPillar() {
        final Pillar item = new Pillar("Pillar of Courage");

        myTestRoom.addItem(item);

        assertEquals(1, myTestRoom.getItems().length);
        assertSame(item, myTestRoom.getItems()[0]);
        assertEquals("***%%*C*%%***", myTestRoom.toString());
    }

    /**
     * Verifies that multiple items are represented with the multiple-item marker.
     */
    @Test
    public void testAddMultipleItemsShowsM() {
        myTestRoom.addItem(new VisionPotion());
        myTestRoom.addItem(new HealingPotion());

        assertEquals(2, myTestRoom.getItems().length);
        assertEquals("***%%*M*%%***", myTestRoom.toString());
    }

    /**
     * Verifies that items can be removed by name fragment.
     */
    @Test
    public void testRemoveItemsFromRoom() {
        final VisionPotion visionPotion = new VisionPotion();
        final HealingPotion healingPotion = new HealingPotion();
        final Pillar pillar = new Pillar("Pillar of Power");

        myTestRoom.addItem(visionPotion);
        myTestRoom.addItem(healingPotion);
        myTestRoom.addItem(pillar);

        assertEquals(3, myTestRoom.getItems().length);

        myTestRoom.removeItems("vision");

        assertEquals(2, myTestRoom.getItems().length);
        assertFalse(containsReference(myTestRoom.getItems(), visionPotion));

        myTestRoom.removeItems("healing");
        assertEquals(1, myTestRoom.getItems().length);
        assertFalse(containsReference(myTestRoom.getItems(), healingPotion));

        myTestRoom.removeItems("power");
        assertEquals(0, myTestRoom.getItems().length);
        assertFalse(containsReference(myTestRoom.getItems(), pillar));
    }

    /**
     * Verifies that removing items from an empty room does not fail.
     */
    @Test
    public void testRemoveItemsFromEmptyRoom() {
        assertEquals(0, myTestRoom.getItems().length);

        myTestRoom.removeItems("anything");

        assertEquals(0, myTestRoom.getItems().length);
        assertEquals("***%%* *%%***", myTestRoom.toString());
    }

    /**
     * Verifies that item removal is case-insensitive.
     */
    @Test
    public void testRemoveItemsCaseInsensitive() {
        final VisionPotion item = new VisionPotion();
        myTestRoom.addItem(item);

        myTestRoom.removeItems("VISION");

        assertEquals(0, myTestRoom.getItems().length);
    }

    /**
     * Verifies that a null entrance-requirement array is stored as null.
     */
    @Test
    public void testConstructorStoresNullEntranceRequirements() {
        final Room roomWithNullRequirements = new Room(null);
        assertNull(getEntranceRequirements(roomWithNullRequirements));
    }

    /**
     * Verifies that multiple entrance requirements are stored in order.
     */
    @Test
    public void testConstructorStoresDifferentRequirementItems() {
        final VisionPotion visionPotion = new VisionPotion();
        final HealingPotion healingPotion = new HealingPotion();
        final Pillar pillar = new Pillar("Pillar of Wisdom");

        final Room room = new Room(new Item[] {visionPotion, healingPotion, pillar});
        final Item[] requirements = getEntranceRequirements(room);

        assertNotNull(requirements);
        assertEquals(3, requirements.length);
        assertSame(visionPotion, requirements[0]);
        assertSame(healingPotion, requirements[1]);
        assertSame(pillar, requirements[2]);
    }

    /**
     * Verifies that single-item entrance requirements are stored correctly.
     */
    @Test
    public void testConstructorStoresSingleRequirementItem() {
        final Room visionRoom = new Room(new Item[] {new VisionPotion()});
        final Room healingRoom = new Room(new Item[] {new HealingPotion()});
        final Room pillarRoom = new Room(new Item[] {new Pillar("Pillar of Honor")});

        assertEquals(1, getEntranceRequirements(visionRoom).length);
        assertEquals(1, getEntranceRequirements(healingRoom).length);
        assertEquals(1, getEntranceRequirements(pillarRoom).length);
    }

    /**
     * Verifies that entering a restricted room without the required item does
     * not mark the room as visited.
     */
    @Test
    public void testEnterWithoutRequiredItemDoesNotMarkRoomVisited() {
        final Item requiredItem = new VisionPotion();
        final Room restrictedRoom = new Room(new Item[] {requiredItem});

        assertFalse(restrictedRoom.getHasVisitedBefore());

        restrictedRoom.enter(myTestHero);

        assertFalse(restrictedRoom.getHasVisitedBefore());
    }

    /**
     * Verifies that entering a normal room marks it as visited.
     */
    @Test
    public void testEnterNormalRoomMarksVisited() {
        assertFalse(myTestRoom.getHasVisitedBefore());

        myTestRoom.enter(myTestHero);

        assertTrue(myTestRoom.getHasVisitedBefore());
    }

    /**
     * Verifies that moving through a connected north room enters the adjacent
     * room.
     */
    @Test
    public void testTryDoorToNorthRoom() {
        final Room northRoom = new Room(null);
        northRoom.setIsExit(true);
        myTestRoom.setNorthRoom(northRoom);

        myTestRoom.enter(myTestHero);
        assertTrue(myTestRoom.getHasVisitedBefore());
        assertFalse(northRoom.getHasVisitedBefore());

        myTestRoom.tryDoor(myTestHero, "North");

        assertTrue(northRoom.getHasVisitedBefore());
        assertEquals("***%%*O*%%***", northRoom.toString());
    }

    /**
     * Verifies that an invalid direction does not move the hero.
     */
    @Test
    public void testTryDoorInvalidDirectionDoesNothing() {
        final Room northRoom = new Room(null);
        myTestRoom.setNorthRoom(northRoom);

        myTestRoom.tryDoor(myTestHero, "Up");

        assertFalse(northRoom.getHasVisitedBefore());
    }

    /**
     * Verifies that attempting to move in a missing direction does nothing.
     */
    @Test
    public void testTryDoorWhenAdjacentRoomIsNullDoesNothing() {
        myTestRoom.setNorthRoom(null);

        myTestRoom.tryDoor(myTestHero, "North");

        assertEquals("***%%* *%%***", myTestRoom.toString());
    }

    /**
     * Verifies that the room monster array starts empty.
     */
    @Test
    public void testMonsterArrayStartsEmpty() {
        final Monster[] monsters = myTestRoom.getMonsters();

        assertNotNull(monsters);
        assertEquals(0, monsters.length);
    }

    /**
     * Verifies that adding an ogre works correctly.
     */
    @Test
    public void testAddOgreMonster() {
        final Ogre ogre = new Ogre();

        myTestRoom.addMonster(ogre);

        assertEquals(1, myTestRoom.getMonsters().length);
        assertSame(ogre, myTestRoom.getMonsters()[0]);
        assertEquals("Ogre", myTestRoom.getMonsters()[0].getMyName());
    }

    /**
     * Verifies that adding a skeleton works correctly.
     */
    @Test
    public void testAddSkeletonMonster() {
        final Skeleton skeleton = new Skeleton();

        myTestRoom.addMonster(skeleton);

        assertEquals(1, myTestRoom.getMonsters().length);
        assertSame(skeleton, myTestRoom.getMonsters()[0]);
        assertEquals("Skeleton", myTestRoom.getMonsters()[0].getMyName());
    }

    /**
     * Verifies that adding a gremlin works correctly.
     */
    @Test
    public void testAddGremlinMonster() {
        final Gremlin gremlin = new Gremlin();

        myTestRoom.addMonster(gremlin);

        assertEquals(1, myTestRoom.getMonsters().length);
        assertSame(gremlin, myTestRoom.getMonsters()[0]);
        assertEquals("Gremlin", myTestRoom.getMonsters()[0].getMyName());
    }

    /**
     * Verifies that multiple monsters are appended in order.
     */
    @Test
    public void testAddMultipleMonsters() {
        final Ogre ogre = new Ogre();
        final Skeleton skeleton = new Skeleton();
        final Gremlin gremlin = new Gremlin();

        myTestRoom.addMonster(ogre);
        myTestRoom.addMonster(skeleton);
        myTestRoom.addMonster(gremlin);

        assertEquals(3, myTestRoom.getMonsters().length);
        assertSame(ogre, myTestRoom.getMonsters()[0]);
        assertSame(skeleton, myTestRoom.getMonsters()[1]);
        assertSame(gremlin, myTestRoom.getMonsters()[2]);
    }

    /**
     * Returns true if the given item array contains the exact target reference.
     *
     * @param theItems the array to search
     * @param theTarget the exact item reference to find
     * @return true if the target reference appears in the array
     */
    private boolean containsReference(final Item[] theItems, final Item theTarget) {
        for (final Item item : theItems) {
            if (item == theTarget) {
                return true;
            }
        }

        return false;
    }

    /**
     * Reads the private entrance-requirements array from a room for testing.
     *
     * @param theRoom the room to inspect
     * @return the room's entrance-requirements array
     */
    private Item[] getEntranceRequirements(final Room theRoom) {
        try {
            final Field field = Room.class.getDeclaredField("myEntranceRequirements");
            field.setAccessible(true);
            return (Item[]) field.get(theRoom);
        } catch (ReflectiveOperationException e) {
            fail("Could not read myEntranceRequirements: " + e.getMessage());
            return null;
        }
    }
}