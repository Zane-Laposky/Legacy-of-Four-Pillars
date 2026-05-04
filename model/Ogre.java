package model;

/**
 * Ogre is a strong Monster with high health and moderate damage.
 * It has a low chance to heal after taking damage.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class Ogre extends Monster {

    private static final double CHANCE_TO_HEAL = 0.1;
    private static final int MIN_HEAL = 30;
    private static final int MAX_HEAL = 60;

    /**
     * Constructs an Ogre with predefined stats.
     */
    public Ogre() {
        super("Ogre", 200, 30, 60, 2, 0.6);

        setMyChanceToHeal(CHANCE_TO_HEAL);
        setMyMinHeal(MIN_HEAL);
        setMyMaxHeal(MAX_HEAL);
    }
}