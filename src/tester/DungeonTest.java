/*
 * Legacy of Four Pillars
 * Dungeon Test Suite
 * Spring 2026
 */
package tester;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import model.Dungeon;
import model.Room;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

/**
 * DungeonTest verifies dungeon generation,
 * room creation, exit validation, getter methods,
 * and dungeon rendering behavior.
 *
 * <p>
 * This test suite includes:
 * </p>
 *
 * <ul>
 *     <li>Dungeon instantiation testing</li>
 *     <li>Room generation validation</li>
 *     <li>Exit generation validation</li>
 *     <li>Dungeon rendering verification</li>
 *     <li>Getter verification</li>
 *     <li>Edge-case difficulty testing</li>
 *     <li>Stress and repeated generation testing</li>
 * </ul>
 *
 * @author Zane laposky
 * @version 1.1 - 5/16/2026
 */
public class DungeonTest
{
    /**
     * Minimum number of rooms expected
     * within every generated dungeon.
     */
    private static final int MINIMUM_ROOM_COUNT = 5;

    /**
     * Very large difficulty value near
     * the integer boundary.
     */
    private static final int LARGE_DIFFICULTY =
            214748365;

    /**
     * Verifies dungeon instantiation succeeds.
     */
    @Test
    public void testDungeonInstantiation()
    {
        final Dungeon dungeon =
                new Dungeon(10);

        assertNotNull(dungeon);
    }

    /**
     * Verifies hero coordinates begin at (0, 0).
     */
    @Test
    public void testDefaultHeroCoordinates()
    {
        final Dungeon dungeon =
                new Dungeon(10);

        assertEquals(0, dungeon.getX());
        assertEquals(0, dungeon.getY());
    }

    /**
     * Verifies room collection exists.
     */
    @Test
    public void testRoomsExist()
    {
        final Dungeon dungeon =
                new Dungeon(10);

        assertNotNull(dungeon.getRooms());
    }

    /**
     * Verifies minimum room generation occurs.
     */
    @Test
    public void testMinimumRoomGeneration()
    {
        final Dungeon dungeon =
                new Dungeon(10);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        assertTrue(
                rooms.size()
                        >= MINIMUM_ROOM_COUNT);
    }

    /**
     * Verifies entrance room exists.
     */
    @Test
    public void testEntranceExists()
    {
        final Dungeon dungeon =
                new Dungeon(10);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        assertTrue(rooms.containsKey("0,0"));
    }

    /**
     * Verifies exactly one entrance exists.
     */
    @Test
    public void testSingleEntranceExists()
    {
        final Dungeon dungeon =
                new Dungeon(25);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        int entranceCount = 0;

        for (final Object roomObject
                : rooms.values())
        {
            final Room room =
                    (Room) roomObject;

            if (room.getIsEntrance())
            {
                entranceCount++;
            }
        }

        assertEquals(1, entranceCount);
    }

    /**
     * Verifies dungeon contains a valid exit.
     */
    @Test
    public void testDungeonContainsExit()
    {
        final Dungeon dungeon =
                new Dungeon(10);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        boolean foundExit = false;

        for (final Object roomObject
                : rooms.values())
        {
            final Room room =
                    (Room) roomObject;

            if (room.getIsExit())
            {
                foundExit = true;
            }
        }

        assertTrue(foundExit);
    }

    /**
     * Verifies only one exit exists.
     */
    @Test
    public void testSingleExitExists()
    {
        final Dungeon dungeon =
                new Dungeon(10);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        int exitCount = 0;

        for (final Object roomObject
                : rooms.values())
        {
            final Room room =
                    (Room) roomObject;

            if (room.getIsExit())
            {
                exitCount++;
            }
        }

        assertEquals(1, exitCount);
    }

    /**
     * Verifies generated rooms are never null.
     */
    @Test
    public void testRoomsAreNotNull()
    {
        final Dungeon dungeon =
                new Dungeon(50);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        for (final Object roomObject
                : rooms.values())
        {
            assertNotNull(roomObject);
        }
    }

    /**
     * Verifies room coordinates match hashmap keys.
     */
    @Test
    public void testRoomCoordinatesMatchKeys()
    {
        final Dungeon dungeon =
                new Dungeon(50);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        for (final Object keyObject
                : rooms.keySet())
        {
            final String key =
                    (String) keyObject;

            final Room room =
                    (Room) rooms.get(key);

            final String expectedKey =
                    room.getX()
                            + ","
                            + room.getY();

            assertEquals(expectedKey, key);
        }
    }

    /**
     * Verifies generated dungeon string
     * is not null.
     */
    @Test
    public void testToStringNotNull()
    {
        final Dungeon dungeon =
                new Dungeon(10);

        assertNotNull(dungeon.toString());
    }

    /**
     * Verifies generated dungeon string
     * is not empty.
     */
    @Test
    public void testToStringNotEmpty()
    {
        final Dungeon dungeon =
                new Dungeon(10);

        assertFalse(
                dungeon.toString().isEmpty());
    }

    /**
     * Verifies dungeon rendering contains
     * visible room graphics.
     */
    @Test
    public void testToStringContainsRoomGraphics()
    {
        final Dungeon dungeon =
                new Dungeon(10);

        final String dungeonString =
                dungeon.toString();

        assertTrue(
                dungeonString.contains("╔")
                        || dungeonString.contains("╬")
                        || dungeonString.contains("║"));
    }

    /**
     * Verifies repeated toString calls
     * produce consistent output.
     */
    @RepeatedTest(10)
    public void testRepeatedToStringCalls()
    {
        final Dungeon dungeon =
                new Dungeon(20);

        final String firstString =
                dungeon.toString();

        final String secondString =
                dungeon.toString();

        assertEquals(
                firstString,
                secondString);
    }

    /**
     * Verifies generation succeeds
     * with zero difficulty.
     */
    @Test
    public void testZeroDifficulty()
    {
        final Dungeon dungeon =
                new Dungeon(0);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        assertTrue(
                rooms.size()
                        >= MINIMUM_ROOM_COUNT);
    }

    /**
     * Verifies generation succeeds
     * with negative difficulty.
     */
    @Test
    public void testNegativeDifficulty()
    {
        final Dungeon dungeon =
                new Dungeon(-10);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        assertTrue(
                rooms.size()
                        >= MINIMUM_ROOM_COUNT);
    }

    /**
     * Verifies generation succeeds
     * with difficulty one.
     */
    @Test
    public void testDifficultyOne()
    {
        final Dungeon dungeon =
                new Dungeon(1);

        assertNotNull(dungeon);
    }

    /**
     * Verifies generation succeeds
     * with moderate difficulty.
     */
    @Test
    public void testModerateDifficulty()
    {
        final Dungeon dungeon =
                new Dungeon(100);

        assertNotNull(dungeon);
    }

    /**
     * Verifies generation succeeds
     * near integer limits.
     */
    @Test
    public void testLargeDifficulty()
    {
        final Dungeon dungeon =
                new Dungeon(LARGE_DIFFICULTY);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        assertTrue(
                rooms.size()
                        >= MINIMUM_ROOM_COUNT);
    }

    /**
     * Verifies generation does not throw exceptions.
     */
    @RepeatedTest(50)
    public void testGenerationDoesNotThrow()
    {
        assertDoesNotThrow(
                () -> new Dungeon(100));
    }

    /**
     * Verifies repeated dungeon generation succeeds.
     */
    @RepeatedTest(25)
    public void testRepeatedDungeonGeneration()
    {
        final Dungeon dungeon =
                new Dungeon(50);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        assertTrue(
                rooms.size()
                        >= MINIMUM_ROOM_COUNT);
    }

    /**
     * Verifies repeated generations
     * always produce a valid exit.
     */
    @RepeatedTest(25)
    public void testRepeatedExitGeneration()
    {
        final Dungeon dungeon =
                new Dungeon(50);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        boolean foundExit = false;

        for (final Object roomObject
                : rooms.values())
        {
            final Room room =
                    (Room) roomObject;

            if (room.getIsExit())
            {
                foundExit = true;
            }
        }

        assertTrue(foundExit);
    }

    /**
     * Verifies all room keys use
     * valid coordinate formatting.
     */
    @Test
    public void testRoomKeyFormatting()
    {
        final Dungeon dungeon =
                new Dungeon(50);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        for (final Object keyObject
                : rooms.keySet())
        {
            final String key =
                    (String) keyObject;

            assertTrue(key.contains(","));
        }
    }

    /**
     * Verifies no room references itself
     * through directional connections.
     */
    @Test
    public void testRoomDoesNotReferenceItself()
    {
        final Dungeon dungeon =
                new Dungeon(50);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        for (final Object roomObject
                : rooms.values())
        {
            final Room room =
                    (Room) roomObject;

            assertFalse(room == room.getNorthRoom());
            assertFalse(room == room.getSouthRoom());
            assertFalse(room == room.getEastRoom());
            assertFalse(room == room.getWestRoom());
        }
    }

    /**
     * Verifies generated dungeon
     * contains newline formatting.
     */
    @Test
    public void testToStringContainsNewlines()
    {
        final Dungeon dungeon =
                new Dungeon(10);

        assertTrue(
                dungeon.toString()
                        .contains("\n"));
    }

    /**
     * Verifies room count remains positive.
     */
    @RepeatedTest(30)
    public void testRoomCountAlwaysPositive()
    {
        final Dungeon dungeon =
                new Dungeon(75);

        final HashMap<?, ?> rooms =
                (HashMap<?, ?>) dungeon.getRooms();

        assertTrue(rooms.size() > 0);
    }
}