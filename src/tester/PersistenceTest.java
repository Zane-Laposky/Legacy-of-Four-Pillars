package tester;

import controller.Persistence;
import model.HealingPotion;
import model.Hero;
import model.Item;
import model.Pillar;
import model.Priestess;
import model.Thief;
import model.VisionPotion;
import model.Warrior;

import java.util.Optional;

/**
 * Manual test for the Data save/load system.
 *
 * This tests basic hero save/load, repeated saves, and inventory saving.
 */
public class PersistenceTest {

    public static void main(String[] args) {
        Persistence persistence = new Persistence();

        testHeroSaveLoad(persistence, new Warrior("Ryan"), 88);
        testHeroSaveLoad(persistence, new Thief("Test Thief"), 72);
        testHeroSaveLoad(persistence, new Priestess("Test Priestess"), 64);

        testRepeatedSaveOverwrite(persistence);
        testInventorySaveLoad(persistence);

        System.out.println("--------------------------------");
        System.out.println("PersistenceTest finished.");
    }

    private static void testHeroSaveLoad(final Persistence persistence,
                                         final Hero hero,
                                         final int hp) {
        setTestStats(hero, hp, 10, 25, 5, 0.75, 0.25);

        System.out.println("--------------------------------");
        System.out.println("Saving hero: " + hero.getMyName()
                + " (" + hero.getClass().getSimpleName() + ")");

        persistence.savePlayer(hero);

        Optional<Hero> loadedHero = persistence.loadPlayer();

        if (loadedHero.isPresent()) {
            Hero loaded = loadedHero.get();
            printLoadedHero(loaded);

            boolean passed = heroMatchesExpected(
                    loaded,
                    hero.getMyName(),
                    hero.getClass().getSimpleName(),
                    hp,
                    10,
                    25,
                    5,
                    0.75,
                    0.25
            );

            printResult("Basic save/load test", passed);
        } else {
            System.out.println("Basic save/load test failed. No saved hero was loaded.");
        }
    }

    /**
     * Tests saving over an existing save.
     *
     * The first save uses one set of values.
     * The second save changes those values.
     * The test passes only if the second loaded hero has the updated values.
     */
    private static void testRepeatedSaveOverwrite(final Persistence persistence) {
        System.out.println("--------------------------------");
        System.out.println("Testing repeated save overwrite behavior.");

        Hero hero = new Warrior("Overwrite Test");

        setTestStats(hero, 88, 10, 25, 5, 0.75, 0.25);
        System.out.println("First save:");
        persistence.savePlayer(hero);

        Optional<Hero> firstLoad = persistence.loadPlayer();

        if (firstLoad.isPresent()) {
            System.out.println("First loaded save:");
            printLoadedHero(firstLoad.get());
        } else {
            System.out.println("Repeated save test failed. First save did not load.");
            return;
        }

        setTestStats(hero, 40, 12, 30, 7, 0.85, 0.35);
        System.out.println("Second save with updated values:");
        persistence.savePlayer(hero);

        Optional<Hero> secondLoad = persistence.loadPlayer();

        if (secondLoad.isPresent()) {
            Hero loaded = secondLoad.get();

            System.out.println("Second loaded save after overwrite:");
            printLoadedHero(loaded);

            boolean passed = heroMatchesExpected(
                    loaded,
                    "Overwrite Test",
                    "Warrior",
                    40,
                    12,
                    30,
                    7,
                    0.85,
                    0.35
            );

            printResult("Repeated save overwrite test", passed);
        } else {
            System.out.println("Repeated save test failed. Second save did not load.");
        }
    }

    /**
     * Tests that hero inventory items are saved and loaded.
     */
    private static void testInventorySaveLoad(final Persistence persistence) {
        System.out.println("--------------------------------");
        System.out.println("Testing inventory save/load behavior.");

        Hero hero = new Warrior("Inventory Test");
        setTestStats(hero, 90, 11, 22, 6, 0.80, 0.30);

        hero.addItem(new Item[] {
                new HealingPotion(),
                new VisionPotion(),
                new Pillar("Pillar of Abstraction")
        });

        persistence.savePlayer(hero);

        Optional<Hero> loadedHero = persistence.loadPlayer();

        if (loadedHero.isPresent()) {
            Hero loaded = loadedHero.get();

            System.out.println("Loaded inventory:");
            printInventory(loaded);

            boolean passed = inventoryHasItem(loaded, "Healing Potion")
                    && inventoryHasItem(loaded, "Vision Potion")
                    && inventoryHasItem(loaded, "Pillar of Abstraction");

            printResult("Inventory save/load test", passed);
        } else {
            System.out.println("Inventory save/load test failed. No saved hero was loaded.");
        }
    }

    private static void setTestStats(final Hero hero,
                                     final int hp,
                                     final int minDamage,
                                     final int maxDamage,
                                     final int attackSpeed,
                                     final double chanceToHit,
                                     final double chanceToBlock) {
        hero.setMyHitPoints(hp);
        hero.setMyMinDamage(minDamage);
        hero.setMyMaxDamage(maxDamage);
        hero.setMyAttackSpeed(attackSpeed);
        hero.setMyChanceToHit(chanceToHit);
        hero.setMyChanceToBlock(chanceToBlock);
    }

    private static boolean heroMatchesExpected(final Hero loaded,
                                               final String expectedName,
                                               final String expectedClass,
                                               final int expectedHp,
                                               final int expectedMinDamage,
                                               final int expectedMaxDamage,
                                               final int expectedAttackSpeed,
                                               final double expectedChanceToHit,
                                               final double expectedChanceToBlock) {
        return loaded.getMyName().equals(expectedName)
                && loaded.getClass().getSimpleName().equals(expectedClass)
                && loaded.getMyHitPoints() == expectedHp
                && loaded.getMyMinDamage() == expectedMinDamage
                && loaded.getMyMaxDamage() == expectedMaxDamage
                && loaded.getMyAttackSpeed() == expectedAttackSpeed
                && Double.compare(loaded.getMyChanceToHit(), expectedChanceToHit) == 0
                && Double.compare(loaded.getMyChanceToBlock(), expectedChanceToBlock) == 0;
    }

    private static boolean inventoryHasItem(final Hero hero, final String itemName) {
        for (Item item : hero.getMyInventory()) {
            if (item != null && item.getMyName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }

        return false;
    }

    private static void printLoadedHero(final Hero loaded) {
        System.out.println("Loaded hero name: " + loaded.getMyName());
        System.out.println("Loaded hero class: " + loaded.getClass().getSimpleName());
        System.out.println("Loaded hero HP: " + loaded.getMyHitPoints());
        System.out.println("Loaded min damage: " + loaded.getMyMinDamage());
        System.out.println("Loaded max damage: " + loaded.getMyMaxDamage());
        System.out.println("Loaded attack speed: " + loaded.getMyAttackSpeed());
        System.out.println("Loaded chance to hit: " + loaded.getMyChanceToHit());
        System.out.println("Loaded chance to block: " + loaded.getMyChanceToBlock());
    }

    private static void printInventory(final Hero loaded) {
        Item[] inventory = loaded.getMyInventory();

        if (inventory.length == 0) {
            System.out.println("Inventory is empty.");
            return;
        }

        for (Item item : inventory) {
            if (item != null) {
                System.out.println("- " + item.getMyName());
            }
        }
    }

    private static void printResult(final String testName, final boolean passed) {
        if (passed) {
            System.out.println(testName + " passed.");
        } else {
            System.out.println(testName + " failed. Loaded data did not match saved data.");
        }
    }
}
