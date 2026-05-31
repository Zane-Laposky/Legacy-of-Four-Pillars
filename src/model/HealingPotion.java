package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * HealingPotion represents an Item that restores a random amount of
 * health to a Hero when used.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class HealingPotion extends Item implements Serializable {

    /**
     * serialVersionUID for load and save game
     */
    @Serial
    private static final long serialVersionUID = 1L;

    private static final int MIN_HEAL_AMOUNT = 5;
    private static final int MAX_HEAL_BONUS = 10;

    private final int myHealAmount;

    /**
     * Constructs a HealingPotion and randomly generates its heal value.
     */
    public HealingPotion() {
        super("Healing Potion");
        myHealAmount = generateHealAmount();
    }

    /**
     * Generates a random heal amount within the allowed range.
     *
     * @return random heal value
     */
    private int generateHealAmount() {
        return MIN_HEAL_AMOUNT + (int) (Math.random() * (MAX_HEAL_BONUS + 1));
    }

    /**
     * Returns the heal amount of this potion.
     *
     * @return heal amount
     */
    public int getMyHealAmount() {
        return myHealAmount;
    }
}