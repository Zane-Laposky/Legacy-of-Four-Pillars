package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Gremlin is a Monster with moderate health, fast attack speed,
 * and a higher chance to heal compared to stronger monsters.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class Gremlin extends Monster implements Serializable {

    /**
     * serialVersionUID for load and save game
     */
    @Serial
    private static final long serialVersionUID = 1L;


    private static final double CHANCE_TO_HEAL = 0.4;
    private static final int MIN_HEAL = 20;
    private static final int MAX_HEAL = 40;

    /**
     * Constructs a Gremlin with predefined stats.
     */
    public Gremlin() {
        super("Gremlin", 70, 15, 30, 5, 0.8);

        setMyChanceToHeal(CHANCE_TO_HEAL);
        setMyMinHeal(MIN_HEAL);
        setMyMaxHeal(MAX_HEAL);
    }
}