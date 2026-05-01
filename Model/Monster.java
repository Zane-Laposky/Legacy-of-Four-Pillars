package Model;

/**
 * Monster is an abstract subclass of DungeonCharacter that represents
 * enemy characters in the dungeon adventure game.
 *
 * Monsters have the ability to heal themselves after taking damage,
 * based on a probability check. Healing restores a random amount
 * within a defined range but cannot exceed maximum hit points.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public abstract class Monster extends DungeonCharacter {

    private double myChanceToHeal;
    private int myMaxHitPoints;
    private int myMinHeal;
    private int myMaxHeal;

    /**
     * Constructs a Monster with the specified combat attributes.
     * Initializes healing-related values to default states.
     *
     * @param theName the name of the monster
     * @param theHitPoints starting hit points
     * @param theMinDamage minimum damage output
     * @param theMaxDamage maximum damage output
     * @param theAttackSpeed attack speed value
     * @param theChanceToHit chance to successfully hit a target
     */
    public Monster(final String theName,
                   final int theHitPoints,
                   final int theMinDamage,
                   final int theMaxDamage,
                   final int theAttackSpeed,
                   final double theChanceToHit) {

        super(theName, theHitPoints, theMinDamage, theMaxDamage, theAttackSpeed, theChanceToHit);

        myMaxHitPoints = theHitPoints;
        myChanceToHeal = 0.0;
        myMinHeal = 0;
        myMaxHeal = 0;
    }

    /**
     * Returns the chance that this monster will attempt to heal.
     *
     * @return chance to heal (0.0 to 1.0)
     */
    public double getMyChanceToHeal() {
        return myChanceToHeal;
    }

    /**
     * Sets the chance that this monster will attempt to heal.
     *
     * @param theChanceToHeal new healing probability
     */
    public void setMyChanceToHeal(final double theChanceToHeal) {
        myChanceToHeal = theChanceToHeal;
    }

    /**
     * Returns the maximum hit points this monster can have.
     *
     * @return maximum hit points
     */
    public int getMyMaxHitPoints() {
        return myMaxHitPoints;
    }

    /**
     * Sets the maximum hit points for this monster.
     *
     * @param theMaxHitPoints maximum hit points value
     */
    public void setMyMaxHitPoints(final int theMaxHitPoints) {
        myMaxHitPoints = theMaxHitPoints;
    }

    /**
     * Returns the minimum amount of healing this monster can receive.
     *
     * @return minimum heal value
     */
    public int getMyMinHeal() {
        return myMinHeal;
    }

    /**
     * Sets the minimum healing value for this monster.
     *
     * @param theMinHeal minimum heal amount
     */
    public void setMyMinHeal(final int theMinHeal) {
        myMinHeal = theMinHeal;
    }

    /**
     * Returns the maximum amount of healing this monster can receive.
     *
     * @return maximum heal value
     */
    public int getMyMaxHeal() {
        return myMaxHeal;
    }

    /**
     * Sets the maximum healing value for this monster.
     *
     * @param theMaxHeal maximum heal amount
     */
    public void setMyMaxHeal(final int theMaxHeal) {
        myMaxHeal = theMaxHeal;
    }

    /**
     * Attempts to heal the monster based on its chance to heal.
     * If successful, restores a random amount of hit points within
     * the configured healing range. Healing cannot exceed max HP.
     */
    public void heal() {

        final double theChance = Math.random();

        if (theChance <= myChanceToHeal) {

            final int theHealAmount = myMinHeal
                    + (int) (Math.random() * (myMaxHeal - myMinHeal + 1));

            final int theNewHP = getMyHitPoints() + theHealAmount;

            if (theNewHP > myMaxHitPoints) {
                setMyHitPoints(myMaxHitPoints);
            } else {
                setMyHitPoints(theNewHP);
            }

            System.out.println(getMyName() + " heals for " + theHealAmount);
        }
    }
}