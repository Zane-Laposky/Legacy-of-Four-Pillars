package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Skeleton is a Monster with balanced stats and a moderate chance to heal.
 * It is slower than other monsters but deals consistent damage.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class Skeleton extends Monster implements Serializable {

    /**
     * serialVersionUID for load and save game
     */
    @Serial
    private static final long serialVersionUID = 1L;

    private static final double CHANCE_TO_HEAL = 0.3;
    private static final int MIN_HEAL = 30;
    private static final int MAX_HEAL = 50;

    /**
     * Constructs a Skeleton with predefined stats.
     */
    public Skeleton() {
        super("Skeleton", 100, 30, 50, 3, 0.8);

        setMyChanceToHeal(CHANCE_TO_HEAL);
        setMyMinHeal(MIN_HEAL);
        setMyMaxHeal(MAX_HEAL);
    }
}