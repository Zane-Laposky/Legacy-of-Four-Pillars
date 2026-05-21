package tester;

import controller.Persistence;
import model.Hero;
import model.Priestess;
import model.Thief;
import model.Warrior;

import java.util.Optional;

/**
 * Manual test for the Data save/load system.
 * This tests saving and loading multiple hero types.
 */
public class PersistenceTest {

    public static void main(String[] args) {
        Persistence persistence = new Persistence();

        testHeroSaveLoad(persistence, new Warrior("Ryan"), 88);
        testHeroSaveLoad(persistence, new Thief("Test Thief"), 72);
        testHeroSaveLoad(persistence, new Priestess("Test Priestess"), 64);

        System.out.println("PersistenceTest finished.");
    }

    private static void testHeroSaveLoad(Persistence persistence, Hero hero, int hp) {
        hero.setMyHitPoints(hp);
        hero.setMyMinDamage(10);
        hero.setMyMaxDamage(25);
        hero.setMyAttackSpeed(5);
        hero.setMyChanceToHit(0.75);
        hero.setMyChanceToBlock(0.25);

        System.out.println("--------------------------------");
        System.out.println("Saving hero: " + hero.getMyName()
                + " (" + hero.getClass().getSimpleName() + ")");

        persistence.savePlayer(hero);

        Optional<Hero> loadedHero = persistence.loadPlayer();

        if (loadedHero.isPresent()) {
            Hero loaded = loadedHero.get();

            System.out.println("Loaded hero name: " + loaded.getMyName());
            System.out.println("Loaded hero class: " + loaded.getClass().getSimpleName());
            System.out.println("Loaded hero HP: " + loaded.getMyHitPoints());
            System.out.println("Loaded min damage: " + loaded.getMyMinDamage());
            System.out.println("Loaded max damage: " + loaded.getMyMaxDamage());
            System.out.println("Loaded attack speed: " + loaded.getMyAttackSpeed());
            System.out.println("Loaded chance to hit: " + loaded.getMyChanceToHit());
            System.out.println("Loaded chance to block: " + loaded.getMyChanceToBlock());

            boolean passed =
                    loaded.getMyName().equals(hero.getMyName())
                            && loaded.getClass().getSimpleName().equals(hero.getClass().getSimpleName())
                            && loaded.getMyHitPoints() == hp
                            && loaded.getMyMinDamage() == 10
                            && loaded.getMyMaxDamage() == 25
                            && loaded.getMyAttackSpeed() == 5
                            && loaded.getMyChanceToHit() == 0.75
                            && loaded.getMyChanceToBlock() == 0.25;

            if (passed) {
                System.out.println("Save/load test passed.");
            } else {
                System.out.println("Save/load test failed. Loaded data did not match saved data.");
            }
        } else {
            System.out.println("Save/load test failed. No saved hero was loaded.");
        }
    }
}
