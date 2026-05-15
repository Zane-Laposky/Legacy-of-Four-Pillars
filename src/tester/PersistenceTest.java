package tester;

import controller.Persistence;
import model.Hero;
import model.Warrior;

import java.util.Optional;

/**
 * Simple manual test for the Data save/load system.
 */
public class PersistenceTest {

    public static void main(String[] args) {
        Persistence persistence = new Persistence();

        Hero testHero = new Warrior("Ryan");
        testHero.setMyHitPoints(88);

        System.out.println("Saving hero...");
        //persistence.savePlayer(testHero);

        System.out.println("Loading hero...");
        //Optional<Hero> loadedHero = persistence.loadPlayer();

//        if (loadedHero.isPresent()) {
//            Hero hero = loadedHero.get();
//            System.out.println("Loaded hero name: " + hero.getMyName());
//            System.out.println("Loaded hero class: " + hero.getClass().getSimpleName());
//            System.out.println("Loaded hero HP: " + hero.getMyHitPoints());
//        } else {
//            System.out.println("No save found.");
//        }
    }
}
