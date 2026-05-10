/*
 * Legacy of Four Pillars
 * Item test suite.
 */
package tester;

import model.HealingPotion;
import model.Item;
import model.Pillar;
import model.VisionPotion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the Item hierarchy including Item, Pillar, HealingPotion,
 * and VisionPotion behavior under normal and edge-case conditions.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class ItemTest {

    private TestItem myTestItem;
    private HealingPotion myHealingPotion;
    private VisionPotion myVisionPotion;
    private Pillar myPillar;

    /**
     * Sets up fresh item instances before each test.
     */
    @BeforeEach
    public void setUp() {

        myTestItem = new TestItem("Test Item");
        myHealingPotion = new HealingPotion();
        myVisionPotion = new VisionPotion();
        myPillar = new Pillar("Pillar of Encapsulation");
    }

    /**
     * Tests normal item name behavior.
     */
    @Test
    public void testItemConstructorAndAccessors() {

        assertEquals("Test Item", myTestItem.getMyName());
        assertEquals("Test Item", myTestItem.toString());
    }

    /**
     * Tests item behavior with null name.
     */
    @Test
    public void testItemWithNullName() {

        final TestItem theItem = new TestItem(null);

        assertNull(theItem.getMyName());
        assertNull(theItem.toString());
    }

    /**
     * Tests item behavior with empty string name.
     */
    @Test
    public void testItemWithEmptyName() {

        final TestItem theItem = new TestItem("");

        assertEquals("", theItem.getMyName());
        assertEquals("", theItem.toString());
    }

    /**
     * Tests item behavior with extremely long name.
     */
    @Test
    public void testItemWithLongName() {

        final String theLongName = "A".repeat(10000);

        final TestItem theItem = new TestItem(theLongName);

        assertEquals(theLongName, theItem.getMyName());
        assertEquals(theLongName, theItem.toString());
    }

    /**
     * Tests item behavior with special characters.
     */
    @Test
    public void testItemWithSpecialCharacters() {

        final String theSpecialName = "Item !@#$%^&*()";

        final TestItem theItem = new TestItem(theSpecialName);

        assertEquals(theSpecialName, theItem.getMyName());
        assertEquals(theSpecialName, theItem.toString());
    }

    /**
     * Tests HealingPotion name correctness.
     */
    @Test
    public void testHealingPotionName() {

        assertEquals("Healing Potion", myHealingPotion.getMyName());
        assertEquals("Healing Potion", myHealingPotion.toString());
    }

    /**
     * Tests HealingPotion heal amount is within valid range.
     */
    @Test
    public void testHealingPotionHealAmountRange() {

        for (int i = 0; i < 100; i++) {

            final HealingPotion thePotion = new HealingPotion();

            assertTrue(thePotion.getMyHealAmount() >= 5);
            assertTrue(thePotion.getMyHealAmount() <= 15);
        }
    }

    /**
     * Tests HealingPotion never produces negative heal values.
     */
    @Test
    public void testHealingPotionNoNegativeValues() {

        final HealingPotion thePotion = new HealingPotion();

        assertTrue(thePotion.getMyHealAmount() >= 0);
    }

    /**
     * Tests VisionPotion name correctness.
     */
    @Test
    public void testVisionPotionName() {

        assertEquals("Vision Potion", myVisionPotion.getMyName());
        assertEquals("Vision Potion", myVisionPotion.toString());
    }

    /**
     * Tests VisionPotion fixed vision range.
     */
    @Test
    public void testVisionPotionVisionRange() {

        assertEquals(8, myVisionPotion.getMyVisionRange());
    }

    /**
     * Tests VisionPotion consistency across instances.
     */
    @Test
    public void testVisionPotionConsistency() {

        final VisionPotion theFirstPotion = new VisionPotion();
        final VisionPotion theSecondPotion = new VisionPotion();

        assertEquals(8, theFirstPotion.getMyVisionRange());
        assertEquals(8, theSecondPotion.getMyVisionRange());
    }

    /**
     * Tests Pillar name assignment.
     */
    @Test
    public void testPillarName() {

        assertEquals("Pillar of Encapsulation", myPillar.getMyName());
        assertEquals("Pillar of Encapsulation", myPillar.toString());
    }

    /**
     * Tests multiple pillar types.
     */
    @Test
    public void testDifferentPillarNames() {

        final Pillar theAbstraction = new Pillar("Abstraction");
        final Pillar theInheritance = new Pillar("Inheritance");
        final Pillar thePolymorphism = new Pillar("Polymorphism");

        assertEquals("Abstraction", theAbstraction.getMyName());
        assertEquals("Inheritance", theInheritance.getMyName());
        assertEquals("Polymorphism", thePolymorphism.getMyName());
    }

    /**
     * Tests pillar null name handling.
     */
    @Test
    public void testPillarWithNullName() {

        final Pillar thePillar = new Pillar(null);

        assertNull(thePillar.getMyName());
        assertNull(thePillar.toString());
    }

    /**
     * Tests pillar with very large name.
     */
    @Test
    public void testPillarWithLongName() {

        final String theLongName = "Pillar ".repeat(100);

        final Pillar thePillar = new Pillar(theLongName);

        assertEquals(theLongName, thePillar.getMyName());
        assertEquals(theLongName, thePillar.toString());
    }

    /**
     * Minimal concrete class for testing Item.
     */
    private static class TestItem extends Item {

        /**
         * Constructs a test item.
         *
         * @param theName item name
         */
        TestItem(final String theName) {

            super(theName);
        }
    }
}