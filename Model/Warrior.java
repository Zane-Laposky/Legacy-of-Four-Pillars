package Model;

/**
 * Warrior is a Hero class with high health and strong damage output.
 * It has a special ability called Crushing Blow that can deal
 * high damage with a limited chance of success.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class Warrior extends Hero {

    private static final double CRUSHING_BLOW_CHANCE = 0.4;
    private static final int MIN_CRUSHING_BLOW_DAMAGE = 75;
    private static final int MAX_CRUSHING_BLOW_DAMAGE = 175;

    /**
     * Constructs a Warrior with predefined stats.
     *
     * @param theName the name of the Warrior
     */
    public Warrior(final String theName) {
        super(theName, 125, 35, 60, 4, 0.8);
        setMyChanceToBlock(0.2);
    }

    /**
     * Performs the Warrior's special attack: Crushing Blow.
     * Has a chance to deal high damage to the target monster.
     *
     * @param theMonster the target monster
     */
    public void crushingBlow(final Monster theMonster) {

        final double theChance = Math.random();

        if (theChance <= CRUSHING_BLOW_CHANCE) {

            final int theDamage =
                    MIN_CRUSHING_BLOW_DAMAGE +
                            (int)(Math.random() * (MAX_CRUSHING_BLOW_DAMAGE - MIN_CRUSHING_BLOW_DAMAGE + 1));

            theMonster.takeDamage(theDamage);

            System.out.println("Crushing Blow hits for " + theDamage);

        } else {

            System.out.println("Crushing Blow failed.");
        }
    }
}