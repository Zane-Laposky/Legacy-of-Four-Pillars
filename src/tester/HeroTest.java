/*
 * Legacy of Four Pillars
 * Hero and combat test suite.
 */
package tester;

import java.lang.reflect.Field;

import model.DungeonCharacter;
import model.Gremlin;
import model.Hero;
import model.Monster;
import model.Priestess;
import model.Room;
import model.Thief;
import model.Warrior;
import model.Item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the Hero hierarchy and inherited combat behavior.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class HeroTest {

    /**
     * Base hero used for Hero-level behavior tests.
     */
    private TestHero myBaseHero;

    /**
     * Character used for DungeonCharacter-level tests.
     */
    private TestCharacter myCharacter;

    /**
     * Target character used for attack tests.
     */
    private TestCharacter myTargetCharacter;

    /**
     * Monster used for Monster-level tests.
     */
    private TestMonster myMonster;

    /**
     * Target monster used for attack and special-ability tests.
     */
    private TestMonster myTargetMonster;

    /**
     * Warrior used for subclass-specific tests.
     */
    private Warrior myWarrior;

    /**
     * Priestess used for subclass-specific tests.
     */
    private Priestess myPriestess;

    /**
     * Thief used for subclass-specific tests.
     */
    private Thief myThief;

    /**
     * Sets up fresh objects before each test.
     */
    @BeforeEach
    public void setUp() {
        myBaseHero = new TestHero("BaseHero");

        myCharacter = new TestCharacter("Hero", 100, 10, 20, 5, 1.0);
        myTargetCharacter = new TestCharacter("Target", 100, 5, 5, 1, 0.0);

        myMonster = new TestMonster("TestMonster", 80, 12, 12, 3, 1.0);
        myTargetMonster = new TestMonster("TargetMonster", 1000, 1, 1, 1, 0.0);

        myWarrior = new Warrior("Warrior");
        myPriestess = new Priestess("Priestess");
        myThief = new Thief("Thief");
    }

    /**
     * Verifies that the Hero base class starts with expected defaults.
     */
    @Test
    public void testHeroDefaults() {
        assertEquals(0.0, myBaseHero.getMyChanceToBlock(), 0.0);
        assertNull(myBaseHero.getCurrentRoom());
        assertNotNull(myBaseHero.getMyInventory());
        assertEquals(0, myBaseHero.getMyInventory().length);
        assertNull(readSpecialAbility(myBaseHero));
    }

    /**
     * Verifies that the chance-to-block setter and getter work for legal and
     * edge values.
     */
    @Test
    public void testSetAndGetChanceToBlock() {
        myBaseHero.setMyChanceToBlock(0.5);
        assertEquals(0.5, myBaseHero.getMyChanceToBlock(), 0.0);

        myBaseHero.setMyChanceToBlock(-1.0);
        assertEquals(-1.0, myBaseHero.getMyChanceToBlock(), 0.0);

        myBaseHero.setMyChanceToBlock(2.0);
        assertEquals(2.0, myBaseHero.getMyChanceToBlock(), 0.0);
    }

    /**
     * Verifies that the current room setter and getter work correctly.
     */
    @Test
    public void testSetAndGetCurrentRoom() {
        final Room theRoom = new Room(null);

        myBaseHero.setCurrentRoom(theRoom);
        assertSame(theRoom, myBaseHero.getCurrentRoom());

        myBaseHero.setCurrentRoom(null);
        assertNull(myBaseHero.getCurrentRoom());
    }

    /**
     * Verifies that the inventory starts empty.
     */
    @Test
    public void testHeroInventoryStartsEmpty() {
        assertNotNull(myBaseHero.getMyInventory());
        assertEquals(0, myBaseHero.getMyInventory().length);
    }

    /**
     * Verifies that the special ability starts null.
     */
    @Test
    public void testHeroSpecialAbilityStartsNull() {
        assertNull(readSpecialAbility(myBaseHero));
    }

    /**
     * Verifies that DungeonCharacter constructor values are stored correctly.
     */
    @Test
    public void testCharacterConstructorValues() {
        assertEquals("Hero", myCharacter.getMyName());
        assertEquals(100, myCharacter.getMyHitPoints());
        assertEquals(10, myCharacter.getMyMinDamage());
        assertEquals(20, myCharacter.getMyMaxDamage());
        assertEquals(5, myCharacter.getMyAttackSpeed());
        assertEquals(1.0, myCharacter.getMyChanceToHit(), 0.0);
    }

    /**
     * Verifies that a null name is stored and represented safely.
     */
    @Test
    public void testNullName() {
        final TestCharacter character =
                new TestCharacter(null, 10, 1, 2, 3, 0.5);

        assertNull(character.getMyName());
        assertEquals("null HP:10", character.toString());
    }

    /**
     * Verifies that a very long name is stored correctly.
     */
    @Test
    public void testIncrediblyLongName() {
        final String longName = "A".repeat(1000);
        final TestCharacter character =
                new TestCharacter(longName, 10, 1, 2, 3, 0.5);

        assertEquals(longName, character.getMyName());
        assertTrue(character.toString().startsWith(longName));
    }

    /**
     * Verifies that unusual name content is stored unchanged.
     */
    @Test
    public void testInvalidNameCharacters() {
        final String strangeName = "123!@# Name <>?";

        myCharacter.setMyName(strangeName);

        assertEquals(strangeName, myCharacter.getMyName());
    }

    /**
     * Verifies that empty and blank names are accepted as-is.
     */
    @Test
    public void testEmptyAndBlankNames() {
        myCharacter.setMyName("");
        assertEquals("", myCharacter.getMyName());

        myCharacter.setMyName("   ");
        assertEquals("   ", myCharacter.getMyName());
    }

    /**
     * Verifies that hit point edge values are stored directly.
     */
    @Test
    public void testHitPointEdgeCases() {
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
     * Verifies that explicit casts are accepted for hit points.
     */
    @Test
    public void testExplicitCastHitPoints() {
        final long hitPointSource = 250L;
        myCharacter.setMyHitPoints((int) hitPointSource);

        assertEquals(250, myCharacter.getMyHitPoints());
    }

    /**
     * Verifies that damage range edge values are stored directly.
     */
    @Test
    public void testDamageEdgeCases() {
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
     * Verifies that explicit casts are accepted for damage values.
     */
    @Test
    public void testExplicitCastDamageValues() {
        final long minDamageSource = 11L;
        final double maxDamageSource = 29.0;

        myCharacter.setMyMinDamage((int) minDamageSource);
        myCharacter.setMyMaxDamage((int) maxDamageSource);

        assertEquals(11, myCharacter.getMyMinDamage());
        assertEquals(29, myCharacter.getMyMaxDamage());
    }

    /**
     * Verifies that attack speed edge values are stored directly.
     */
    @Test
    public void testAttackSpeedEdgeCases() {
        myCharacter.setMyAttackSpeed(-1);
        assertEquals(-1, myCharacter.getMyAttackSpeed());

        myCharacter.setMyAttackSpeed(0);
        assertEquals(0, myCharacter.getMyAttackSpeed());

        myCharacter.setMyAttackSpeed(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, myCharacter.getMyAttackSpeed());
    }

    /**
     * Verifies that explicit casts are accepted for attack speed.
     */
    @Test
    public void testExplicitCastAttackSpeed() {
        final long attackSpeedSource = 6L;
        myCharacter.setMyAttackSpeed((int) attackSpeedSource);

        assertEquals(6, myCharacter.getMyAttackSpeed());
    }

    /**
     * Verifies that chance-to-hit edge values are stored directly.
     */
    @Test
    public void testChanceToHitEdgeCases() {
        myCharacter.setMyChanceToHit(-0.25);
        assertEquals(-0.25, myCharacter.getMyChanceToHit(), 0.0);

        myCharacter.setMyChanceToHit(0.0);
        assertEquals(0.0, myCharacter.getMyChanceToHit(), 0.0);

        myCharacter.setMyChanceToHit(1.0);
        assertEquals(1.0, myCharacter.getMyChanceToHit(), 0.0);

        myCharacter.setMyChanceToHit(2.5);
        assertEquals(2.5, myCharacter.getMyChanceToHit(), 0.0);
    }

    /**
     * Verifies that explicit casts are accepted for chance-to-hit.
     */
    @Test
    public void testExplicitCastChanceToHit() {
        final float chanceSource = 0.75F;
        myCharacter.setMyChanceToHit((double) chanceSource);

        assertEquals(0.75, myCharacter.getMyChanceToHit(), 0.0);
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
     * Verifies that takeDamage with zero leaves hit points unchanged.
     */
    @Test
    public void testTakeDamageZero() {
        myCharacter.takeDamage(0);

        assertEquals(100, myCharacter.getMyHitPoints());
    }

    /**
     * Verifies that large damage clamps hit points at zero.
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
    public void testTakeDamageNegativeValue() {
        myCharacter.takeDamage(-10);

        assertEquals(110, myCharacter.getMyHitPoints());
    }

    /**
     * Verifies that explicit casts can be used when calling takeDamage.
     */
    @Test
    public void testTakeDamageWithExplicitCast() {
        final long damageSource = 15L;

        myCharacter.takeDamage((int) damageSource);

        assertEquals(85, myCharacter.getMyHitPoints());
    }

    /**
     * Verifies that attack succeeds when chance-to-hit is guaranteed.
     */
    @Test
    public void testAttackGuaranteedHit() {
        myCharacter.setMyMinDamage(15);
        myCharacter.setMyMaxDamage(15);
        myCharacter.setMyChanceToHit(1.0);

        myCharacter.attack(myTargetCharacter);

        assertEquals(85, myTargetCharacter.getMyHitPoints());
    }

    /**
     * Verifies that attack does nothing when chance-to-hit is zero.
     */
    @Test
    public void testAttackGuaranteedMiss() {
        myCharacter.setMyChanceToHit(0.0);

        myCharacter.attack(myTargetCharacter);

        assertEquals(100, myTargetCharacter.getMyHitPoints());
    }

    /**
     * Verifies that attacking a null target with a guaranteed miss does not
     * throw.
     */
    @Test
    public void testAttackNullTargetWithGuaranteedMiss() {
        myCharacter.setMyChanceToHit(0.0);

        assertDoesNotThrow(() -> myCharacter.attack(null));
    }

    /**
     * Verifies that attacking a null target with a guaranteed hit throws.
     */
    @Test
    public void testAttackNullTargetWithGuaranteedHit() {
        myCharacter.setMyChanceToHit(1.0);

        assertThrows(NullPointerException.class, () -> myCharacter.attack(null));
    }

    /**
     * Verifies the alive check.
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
     * Verifies the string representation of a character.
     */
    @Test
    public void testToString() {
        assertEquals("Hero HP:100", myCharacter.toString());
    }

    /**
     * Verifies that toString handles a null name safely.
     */
    @Test
    public void testToStringWithNullName() {
        myCharacter.setMyName(null);

        assertEquals("null HP:100", myCharacter.toString());
    }

    /**
     * Verifies the Monster constructor stores all inherited and monster-specific
     * values correctly.
     */
    @Test
    public void testMonsterConstructorValues() {
        assertEquals("TestMonster", myMonster.getMyName());
        assertEquals(80, myMonster.getMyHitPoints());
        assertEquals(12, myMonster.getMyMinDamage());
        assertEquals(12, myMonster.getMyMaxDamage());
        assertEquals(3, myMonster.getMyAttackSpeed());
        assertEquals(1.0, myMonster.getMyChanceToHit(), 0.0);

        assertEquals(0.0, myMonster.getMyChanceToHeal(), 0.0);
        assertEquals(80, myMonster.getMyMaxHitPoints());
        assertEquals(0, myMonster.getMyMinHeal());
        assertEquals(0, myMonster.getMyMaxHeal());
    }

    /**
     * Verifies that Monster healing configuration setters and getters work for
     * edge values.
     */
    @Test
    public void testMonsterHealingSetterEdgeCases() {
        myMonster.setMyChanceToHeal(-1.0);
        myMonster.setMyMinHeal(-20);
        myMonster.setMyMaxHeal(-10);
        myMonster.setMyMaxHitPoints(Integer.MAX_VALUE);

        assertEquals(-1.0, myMonster.getMyChanceToHeal(), 0.0);
        assertEquals(-20, myMonster.getMyMinHeal());
        assertEquals(-10, myMonster.getMyMaxHeal());
        assertEquals(Integer.MAX_VALUE, myMonster.getMyMaxHitPoints());
    }

    /**
     * Verifies that healing increases hit points when the chance is guaranteed.
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
     * Verifies that healing does nothing when the chance to heal is zero.
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
     * Verifies that healing accepts large ranges and does not fail.
     */
    @Test
    public void testHealLargeRange() {
        myMonster.setMyHitPoints(1);
        myMonster.setMyChanceToHeal(1.0);
        myMonster.setMyMinHeal(1);
        myMonster.setMyMaxHeal(Integer.MAX_VALUE);

        myMonster.heal();

        assertTrue(myMonster.getMyHitPoints() >= 2);
        assertTrue(myMonster.getMyHitPoints() <= myMonster.getMyMaxHitPoints());
    }

    /**
     * Verifies the Gremlin constructor values.
     */
    @Test
    public void testGremlinConstructorValues() {
        final Monster gremlin = new Gremlin();

        assertEquals("Gremlin", gremlin.getMyName());
        assertEquals(70, gremlin.getMyHitPoints());
        assertEquals(15, gremlin.getMyMinDamage());
        assertEquals(30, gremlin.getMyMaxDamage());
        assertEquals(5, gremlin.getMyAttackSpeed());
        assertEquals(0.8, gremlin.getMyChanceToHit(), 0.0);
        assertEquals(0.4, gremlin.getMyChanceToHeal(), 0.0);
        assertEquals(20, gremlin.getMyMinHeal());
        assertEquals(40, gremlin.getMyMaxHeal());
        assertEquals(70, gremlin.getMyMaxHitPoints());
    }

    /**
     * Verifies the Ogre constructor values.
     */
    @Test
    public void testOgreConstructorValues() {
        final Monster ogre = new model.Ogre();

        assertEquals("Ogre", ogre.getMyName());
        assertEquals(200, ogre.getMyHitPoints());
        assertEquals(30, ogre.getMyMinDamage());
        assertEquals(60, ogre.getMyMaxDamage());
        assertEquals(2, ogre.getMyAttackSpeed());
        assertEquals(0.6, ogre.getMyChanceToHit(), 0.0);
        assertEquals(0.1, ogre.getMyChanceToHeal(), 0.0);
        assertEquals(30, ogre.getMyMinHeal());
        assertEquals(60, ogre.getMyMaxHeal());
        assertEquals(200, ogre.getMyMaxHitPoints());
    }

    /**
     * Verifies the Skeleton constructor values.
     */
    @Test
    public void testSkeletonConstructorValues() {
        final Monster skeleton = new model.Skeleton();

        assertEquals("Skeleton", skeleton.getMyName());
        assertEquals(100, skeleton.getMyHitPoints());
        assertEquals(30, skeleton.getMyMinDamage());
        assertEquals(50, skeleton.getMyMaxDamage());
        assertEquals(3, skeleton.getMyAttackSpeed());
        assertEquals(0.8, skeleton.getMyChanceToHit(), 0.0);
        assertEquals(0.3, skeleton.getMyChanceToHeal(), 0.0);
        assertEquals(30, skeleton.getMyMinHeal());
        assertEquals(50, skeleton.getMyMaxHeal());
        assertEquals(100, skeleton.getMyMaxHitPoints());
    }

    /**
     * Verifies that Warrior defaults are correct.
     */
    @Test
    public void testWarriorConstructorValues() {
        assertEquals("Warrior", myWarrior.getMyName());
        assertEquals(125, myWarrior.getMyHitPoints());
        assertEquals(35, myWarrior.getMyMinDamage());
        assertEquals(60, myWarrior.getMyMaxDamage());
        assertEquals(4, myWarrior.getMyAttackSpeed());
        assertEquals(0.8, myWarrior.getMyChanceToHit(), 0.0);
        assertEquals(0.2, myWarrior.getMyChanceToBlock(), 0.0);
    }

    /**
     * Verifies that Priestess defaults are correct.
     */
    @Test
    public void testPriestessConstructorValues() {
        assertEquals("Priestess", myPriestess.getMyName());
        assertEquals(75, myPriestess.getMyHitPoints());
        assertEquals(25, myPriestess.getMyMinDamage());
        assertEquals(45, myPriestess.getMyMaxDamage());
        assertEquals(5, myPriestess.getMyAttackSpeed());
        assertEquals(0.7, myPriestess.getMyChanceToHit(), 0.0);
        assertEquals(0.3, myPriestess.getMyChanceToBlock(), 0.0);
    }

    /**
     * Verifies that Thief defaults are correct.
     */
    @Test
    public void testThiefConstructorValues() {
        assertEquals("Thief", myThief.getMyName());
        assertEquals(75, myThief.getMyHitPoints());
        assertEquals(20, myThief.getMyMinDamage());
        assertEquals(40, myThief.getMyMaxDamage());
        assertEquals(6, myThief.getMyAttackSpeed());
        assertEquals(0.8, myThief.getMyChanceToHit(), 0.0);
        assertEquals(0.4, myThief.getMyChanceToBlock(), 0.0);
    }

    /**
     * Verifies that Warrior crushing blow stays within the legal damage range.
     */
    @Test
    public void testWarriorCrushingBlowBounds() {
        myTargetMonster.setMyHitPoints(1000);

        myWarrior.crushingBlow(myTargetMonster);

        assertTrue(myTargetMonster.getMyHitPoints() <= 1000);
        assertTrue(myTargetMonster.getMyHitPoints() >= 825);
    }

    /**
     * Verifies that Priestess healing stays within the legal healing range.
     */
    @Test
    public void testPriestessHealSelfBounds() {
        myPriestess.setMyHitPoints(50);

        myPriestess.healSelf();

        assertTrue(myPriestess.getMyHitPoints() >= 60);
        assertTrue(myPriestess.getMyHitPoints() <= 80);
    }

    /**
     * Verifies that Thief surprise attack stays within the legal outcome range.
     */
    @Test
    public void testThiefSurpriseAttackBounds() {
        myTargetMonster.setMyHitPoints(1000);

        myThief.surpriseAttack(myTargetMonster);

        assertTrue(myTargetMonster.getMyHitPoints() <= 1000);
        assertTrue(myTargetMonster.getMyHitPoints() >= 920);
    }

    /**
     * Verifies that the hero current room can be set and cleared.
     */
    @Test
    public void testHeroCurrentRoomSetAndClear() {
        final Room theRoom = new Room(null);

        myWarrior.setCurrentRoom(theRoom);
        assertSame(theRoom, myWarrior.getCurrentRoom());

        myWarrior.setCurrentRoom(null);
        assertNull(myWarrior.getCurrentRoom());
    }

    /**
     * Verifies that the base hero inventory remains empty.
     */
    @Test
    public void testHeroInventoryEmpty() {
        assertNotNull(myBaseHero.getMyInventory());
        assertEquals(0, myBaseHero.getMyInventory().length);
    }

    /**
     * Verifies that large and small numeric values can be stored.
     */
    @Test
    public void testLargeAndSmallNumericValues() {
        myCharacter.setMyMinDamage(Integer.MIN_VALUE + 1);
        myCharacter.setMyMaxDamage(Integer.MAX_VALUE);
        myCharacter.setMyAttackSpeed(Integer.MIN_VALUE + 1);
        myCharacter.setMyChanceToHit(0.0000001);

        assertEquals(Integer.MIN_VALUE + 1, myCharacter.getMyMinDamage());
        assertEquals(Integer.MAX_VALUE, myCharacter.getMyMaxDamage());
        assertEquals(Integer.MIN_VALUE + 1, myCharacter.getMyAttackSpeed());
        assertEquals(0.0000001, myCharacter.getMyChanceToHit(), 0.0);
    }

    /**
     * Reads the private special-ability field for testing.
     *
     * @param theHero the hero to inspect
     * @return the private special-ability value
     */
    private Object readSpecialAbility(final Hero theHero) {
        try {
            final Field field = Hero.class.getDeclaredField("mySpecialAbility");
            field.setAccessible(true);
            return field.get(theHero);
        } catch (ReflectiveOperationException e) {
            fail("Could not read mySpecialAbility: " + e.getMessage());
            return null;
        }
    }

    /**
     * Minimal concrete DungeonCharacter used to test inherited behavior.
     */
    private static class TestCharacter extends DungeonCharacter {

        /**
         * Creates a test character.
         *
         * @param theName the name
         * @param theHitPoints the hit points
         * @param theMinDamage the minimum damage
         * @param theMaxDamage the maximum damage
         * @param theAttackSpeed the attack speed
         * @param theChanceToHit the chance to hit
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
     * Minimal concrete Monster used to test Monster-specific behavior.
     */
    private static class TestMonster extends Monster {

        /**
         * Creates a test monster.
         *
         * @param theName the name
         * @param theHitPoints the hit points
         * @param theMinDamage the minimum damage
         * @param theMaxDamage the maximum damage
         * @param theAttackSpeed the attack speed
         * @param theChanceToHit the chance to hit
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

    /**
     * Minimal concrete Hero used to test Hero base behavior.
     */
    private static class TestHero extends Hero {

        /**
         * Creates a test hero.
         *
         * @param theName the name
         */
        TestHero(final String theName) {
            super(theName, 50, 10, 10, 1, 1.0);
        }
    }
}