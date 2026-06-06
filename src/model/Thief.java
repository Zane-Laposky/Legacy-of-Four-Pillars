package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Thief is a Hero class that can perform a special surprise attack
 * with varying outcomes including failure, normal attack, or double attack.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class Thief extends Hero implements Serializable {

    /**
     * serialVersionUID for load and save game
     */
    @Serial
    private static final long serialVersionUID = 1L;

    private static final double CHANCE_CAUGHT = 0.2;
    private static final double CHANCE_SURPRISE_ATTACK = 0.4;

    public Thief(final String theName) {
        super(theName, 75, 20, 40, 6, 0.8);
        setMyChanceToBlock(0.4);
    }

    /**
     * Performs the Thief's special attack behavior.
     *
     * @param theMonster the target monster
     */
    public void surpriseAttack(final Monster theMonster) {

        final double theChance = Math.random();

        if (theChance <= CHANCE_CAUGHT) {
            return;

        } else if (theChance <= CHANCE_SURPRISE_ATTACK) {

            int theDamage = getMyMinDamage()
                    + (int) (Math.random() * (getMyMaxDamage() - getMyMinDamage() + 1));

            theMonster.takeDamage(theDamage);

        } else {

            attack(theMonster);
            attack(theMonster);
        }
    }
}
