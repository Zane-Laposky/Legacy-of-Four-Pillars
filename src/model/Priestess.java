package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Priestess is a Hero class with healing abilities and moderate combat stats.
 * Her special ability allows her to heal herself during battle.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class Priestess extends Hero implements Serializable {

    /**
     * serialVersionUID for load and save game
     */
    @Serial
    private static final long serialVersionUID = 1L;

    private static final int MIN_HEAL = 10;
    private static final int MAX_HEAL_BONUS = 20;

    /**
     * Constructs a Priestess with predefined stats.
     *
     * @param theName the name of the Priestess
     */
    public Priestess(final String theName) {
        super(theName, 75, 25, 45, 5, 0.7);
        setMyChanceToBlock(0.3);
    }

    /**
     * Heals the Priestess by a random amount within a defined range.
     * Healing cannot exceed maximum intended behavior (optional cap logic).
     */
    public void healSelf() {

        final int theHeal = MIN_HEAL + (int)(Math.random() * (MAX_HEAL_BONUS + 1));

        setMyHitPoints(getMyHitPoints() + theHeal);
    }
}
