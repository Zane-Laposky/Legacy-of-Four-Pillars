/*
 * Legacy of Four Pillars
 * Monster and DungeonCharacter test suite.
 */
package tester;

import model.DungeonCharacter;
import model.Gremlin;
import model.Monster;
import model.Ogre;
import model.Skeleton;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests DungeonCharacter and Monster behavior, including construction,
 * getters, setters, damage handling, attacking, and healing.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class MonsterTest {

    private TestCharacter myCharacter;
    private TestCharacter myTarget;
    private TestMonster myMonster;
    private Gremlin myGremlin;
    private Ogre myOgre;
    private Skeleton mySkeleton;

    /**
     * Sets up fresh test objects before each test case.
     */
    @BeforeEach
    public void setUp() {
        myCharacter = new TestCharacter("Hero", 100, 10, 20, 5, 1.0);
        myTarget = new TestCharacter("Target", 100, 5, 5, 1, 0.0);
        myMonster = new TestMonster("TestMonster", 80, 12, 12, 3, 1.0);

        myGremlin = new Gremlin();
        myOgre = new Ogre();
        mySkeleton = new Skeleton();
    }

    /**
     * Verifies that constructor values are stored correctly through the
     * superclass.
     */
    @Test
    public void testConstructorStoresValues() {
        assertEquals("TestMonster", myMonster.getMyName());
        assertEquals(80, myMonster.getMyHitPoints());
        assertEquals(12, myMonster.getMyMinDamage());
        assertEquals(12, myMonster.getMyMaxDamage());
        assertEquals(3, myMonster.getMyAttackSpeed());
        assertEquals(1.0, myMonster.getMyChanceToHit());

        assertEquals(0.0, myMonster.getMyChanceToHeal());
        assertEquals(80, myMonster.getMyMaxHitPoints());
        assertEquals(0, myMonster.getMyMinHeal());
        assertEquals(0, myMonster.getMyMaxHeal());
    }

    /**
     * Verifies that a null name is stored and represented safely.
     */
    @Test
    public void testNullName() {
        final TestCharacter character = new TestCharacter(null, 10, 1, 2, 3, 0.5);

        assertNull(character.getMyName());
        assertEquals("null HP:10", character.toString());
    }

    /**
     * Verifies that a very long name is stored correctly.
     */
    @Test
    public void testIncrediblyLongName() {
        final String longName = "A".repeat(1000);
        final TestCharacter character = new TestCharacter(longName, 10, 1, 2, 3, 0.5);

        assertEquals(longName, character.getMyName());
        assertTrue(character.toString().startsWith(longName));
    }

    /**
     * Verifies that unusual names are stored unchanged.
     */
    @Test
    public void testInvalidNameCharacters() {
        final String invalidName = "123!@# Name <>?";

        myCharacter.setMyName(invalidName);

        assertEquals(invalidName, myCharacter.getMyName());
    }

    /**
     * Verifies that setMyName accepts null.
     */
    @Test
    public void testSetNameToNull() {
        myCharacter.setMyName(null);

        assertNull(myCharacter.getMyName());
    }

    /**
     * Verifies that hit point setters accept negative, zero, and large values.
     */
    @Test
    public void testHitPointEdgeValues() {
        myCharacter.setMyHitPoints(-5);
        assertEquals(-5, myCharacter.getMyHitPoints());
        assertFalse(myCharacter.isAlive());

        myCharacter.setMyHitPoints(0);
        assertEquals(0, myCharacter.getMyHitPoints());
        assertFalse(myCharacter.isAlive());

        myCharacter.setMyHitPoints(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, myCharacter.getMyHitPoints());
        assertTrue(myCharacter.isAlive());
    }

    /**
     * Verifies that damage fields accept edge values.
     */
    @Test
    public void testDamageFieldEdgeValues() {
        myCharacter.setMyMinDamage(-10);
        myCharacter.setMyMaxDamage(-1);

        assertEquals(-10, myCharacter.getMyMinDamage());
        assertEquals(-1, myCharacter.getMyMaxDamage());

        myCharacter.setMyMinDamage(Integer.MAX_VALUE - 1);
        myCharacter.setMyMaxDamage(Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE - 1, myCharacter.getMyMinDamage());
        assertEquals(Integer.MAX_VALUE, myCharacter.getMyMaxDamage());
    }

    /**
     * Verifies that attack speed accepts edge values.
     */
    @Test
    public void testAttackSpeedEdgeValues() {
        myCharacter.setMyAttackSpeed(-1);
        assertEquals(-1, myCharacter.getMyAttackSpeed());

        myCharacter.setMyAttackSpeed(0);
        assertEquals(0, myCharacter.getMyAttackSpeed());

        myCharacter.setMyAttackSpeed(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, myCharacter.getMyAttackSpeed());
    }

    /**
     * Verifies that chance-to-hit accepts edge values.
     */
    @Test
    public void testChanceToHitEdgeValues() {
        myCharacter.setMyChanceToHit(-0.25);
        assertEquals(-0.25, myCharacter.getMyChanceToHit());

        myCharacter.setMyChanceToHit(0.0);
        assertEquals(0.0, myCharacter.getMyChanceToHit());

        myCharacter.setMyChanceToHit(1.0);
        assertEquals(1.0, myCharacter.getMyChanceToHit());

        myCharacter.setMyChanceToHit(2.5);
        assertEquals(2.5, myCharacter.getMyChanceToHit());
    }

    /**
     * Verifies that takeDamage reduces hit points correctly.
     */
    @Test
    public void testTakeDamage() {
        myCharacter.takeDamage(25);

        assertEquals(75, myCharacter.getMyHitPoints());
    }

    /**
     * Verifies that takeDamage clamps hit points at zero.
     */
    @Test
    public void testTakeDamageClampsAtZero() {
        myCharacter.takeDamage(1000);

        assertEquals(0, myCharacter.getMyHitPoints());
        assertFalse(myCharacter.isAlive());
    }

    /**
     * Verifies that negative damage follows the current arithmetic behavior.
     */
    @Test
    public void testTakeDamageWithNegativeValue() {
        myCharacter.takeDamage(-10);

        assertEquals(110, myCharacter.getMyHitPoints());
    }

    /**
     * Verifies that attack succeeds when chance-to-hit is guaranteed and
     * damage is fixed.
     */
    @Test
    public void testAttackGuaranteedHit() {
        myCharacter.setMyMinDamage(15);
        myCharacter.setMyMaxDamage(15);
        myCharacter.setMyChanceToHit(1.0);

        myCharacter.attack(myTarget);

        assertEquals(85, myTarget.getMyHitPoints());
    }

    /**
     * Verifies that attack does nothing when chance-to-hit is zero.
     */
    @Test
    public void testAttackGuaranteedMiss() {
        myCharacter.setMyChanceToHit(0.0);

        myCharacter.attack(myTarget);

        assertEquals(100, myTarget.getMyHitPoints());
    }

    /**
     * Verifies that isAlive reflects hit point state correctly.
     */
    @Test
    public void testIsAlive() {
        myCharacter.setMyHitPoints(1);
        assertTrue(myCharacter.isAlive());

        myCharacter.setMyHitPoints(0);
        assertFalse(myCharacter.isAlive());

        myCharacter.setMyHitPoints(-1);
        assertFalse(myCharacter.isAlive());
    }

    /**
     * Verifies that toString formats the state correctly.
     */
    @Test
    public void testToString() {
        assertEquals("Hero HP:100", myCharacter.toString());
    }

    /**
     * Verifies that the Gremlin constructor uses the Monster superclass.
     */
    @Test
    public void testGremlinConstructorValues() {
        assertEquals("Gremlin", myGremlin.getMyName());
        assertEquals(70, myGremlin.getMyHitPoints());
        assertEquals(15, myGremlin.getMyMinDamage());
        assertEquals(30, myGremlin.getMyMaxDamage());
        assertEquals(5, myGremlin.getMyAttackSpeed());
        assertEquals(0.8, myGremlin.getMyChanceToHit());

        assertEquals(0.4, myGremlin.getMyChanceToHeal());
        assertEquals(20, myGremlin.getMyMinHeal());
        assertEquals(40, myGremlin.getMyMaxHeal());
        assertEquals(70, myGremlin.getMyMaxHitPoints());
    }

    /**
     * Verifies that the Ogre constructor uses the Monster superclass.
     */
    @Test
    public void testOgreConstructorValues() {
        assertEquals("Ogre", myOgre.getMyName());
        assertEquals(200, myOgre.getMyHitPoints());
        assertEquals(30, myOgre.getMyMinDamage());
        assertEquals(60, myOgre.getMyMaxDamage());
        assertEquals(2, myOgre.getMyAttackSpeed());
        assertEquals(0.6, myOgre.getMyChanceToHit());

        assertEquals(0.1, myOgre.getMyChanceToHeal());
        assertEquals(30, myOgre.getMyMinHeal());
        assertEquals(60, myOgre.getMyMaxHeal());
        assertEquals(200, myOgre.getMyMaxHitPoints());
    }

    /**
     * Verifies that the Skeleton constructor uses the Monster superclass.
     */
    @Test
    public void testSkeletonConstructorValues() {
        assertEquals("Skeleton", mySkeleton.getMyName());
        assertEquals(100, mySkeleton.getMyHitPoints());
        assertEquals(30, mySkeleton.getMyMinDamage());
        assertEquals(50, mySkeleton.getMyMaxDamage());
        assertEquals(3, mySkeleton.getMyAttackSpeed());
        assertEquals(0.8, mySkeleton.getMyChanceToHit());

        assertEquals(0.3, mySkeleton.getMyChanceToHeal());
        assertEquals(30, mySkeleton.getMyMinHeal());
        assertEquals(50, mySkeleton.getMyMaxHeal());
        assertEquals(100, mySkeleton.getMyMaxHitPoints());
    }

    /**
     * Verifies that Monster setter methods accept edge values.
     */
    @Test
    public void testMonsterSetterEdgeValues() {
        myMonster.setMyChanceToHeal(-1.0);
        myMonster.setMyMinHeal(-20);
        myMonster.setMyMaxHeal(-10);
        myMonster.setMyMaxHitPoints(Integer.MAX_VALUE);

        assertEquals(-1.0, myMonster.getMyChanceToHeal());
        assertEquals(-20, myMonster.getMyMinHeal());
        assertEquals(-10, myMonster.getMyMaxHeal());
        assertEquals(Integer.MAX_VALUE, myMonster.getMyMaxHitPoints());
    }

    /**
     * Verifies that healing can increase hit points when chance-to-heal is
     * guaranteed and the heal amount is fixed.
     */
    @Test
    public void testHealGuaranteedSuccess() {
        myMonster.setMyHitPoints(50);
        myMonster.setMyChanceToHeal(1.0);
        myMonster.setMyMinHeal(10);
        myMonster.setMyMaxHeal(10);

        myMonster.heal();

        assertEquals(60, myMonster.getMyHitPoints());
    }

    /**
     * Verifies that healing does not exceed the monster's maximum hit points.
     */
    @Test
    public void testHealStopsAtMaxHitPoints() {
        myMonster.setMyHitPoints(79);
        myMonster.setMyChanceToHeal(1.0);
        myMonster.setMyMinHeal(10);
        myMonster.setMyMaxHeal(10);

        myMonster.heal();

        assertEquals(80, myMonster.getMyHitPoints());
    }

    /**
     * Verifies that healing does nothing when chance-to-heal is zero.
     */
    @Test
    public void testHealGuaranteedMiss() {
        myMonster.setMyHitPoints(50);
        myMonster.setMyChanceToHeal(0.0);
        myMonster.setMyMinHeal(10);
        myMonster.setMyMaxHeal(10);

        myMonster.heal();

        assertEquals(50, myMonster.getMyHitPoints());
    }

    /**
     * Verifies that extreme string and numeric combinations do not crash.
     */
    @Test
    public void testLargeAndSmallCombinations() {
        final TestCharacter character =
                new TestCharacter("X".repeat(500), Integer.MAX_VALUE, Integer.MIN_VALUE + 1,
                        Integer.MAX_VALUE, Integer.MAX_VALUE, 1.0);

        assertNotNull(character.toString());
        assertTrue(character.isAlive());

        character.takeDamage(1);
        assertEquals(Integer.MAX_VALUE - 1, character.getMyHitPoints());
    }

    /**
     * Minimal concrete DungeonCharacter used for testing abstract behavior.
     */
    private static class TestCharacter extends DungeonCharacter {

        /**
         * Creates a test character with the provided values.
         *
         * @param theName the character name
         * @param theHitPoints the hit points
         * @param theMinDamage the minimum damage
         * @param theMaxDamage the maximum damage
         * @param theAttackSpeed the attack speed
         * @param theChanceToHit the hit chance
         */
        TestCharacter(final String theName,
                      final int theHitPoints,
                      final int theMinDamage,
                      final int theMaxDamage,
                      final int theAttackSpeed,
                      final double theChanceToHit) {
            super(theName, theHitPoints, theMinDamage, theMaxDamage, theAttackSpeed, theChanceToHit);
        }
    }

    /**
     * Minimal concrete Monster used for testing Monster-specific behavior.
     */
    private static class TestMonster extends Monster {

        /**
         * Creates a test monster with the provided values.
         *
         * @param theName the monster name
         * @param theHitPoints the hit points
         * @param theMinDamage the minimum damage
         * @param theMaxDamage the maximum damage
         * @param theAttackSpeed the attack speed
         * @param theChanceToHit the hit chance
         */
        TestMonster(final String theName,
                    final int theHitPoints,
                    final int theMinDamage,
                    final int theMaxDamage,
                    final int theAttackSpeed,
                    final double theChanceToHit) {
            super(theName, theHitPoints, theMinDamage, theMaxDamage, theAttackSpeed, theChanceToHit);
        }
    }
}