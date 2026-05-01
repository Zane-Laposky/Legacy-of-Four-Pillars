package Model;

/**
 * DungeonCharacter is the abstract base class for all combat entities
 * in the dungeon adventure game. It defines shared combat attributes
 * and behavior such as attacking, taking damage, and checking life status.
 *
 * All Heroes and Monsters inherit from this class.
 *
 * @author Zane laposky
 * @version 1.0
 */
public abstract class DungeonCharacter {

    private String myName;
    private int myHitPoints;
    private int myMinDamage;
    private int myMaxDamage;
    private int myAttackSpeed;
    private double myChanceToHit;

    /**
     * Constructs a DungeonCharacter with the specified attributes.
     *
     * @param theName the name of the character
     * @param theHitPoints the starting hit points of the character
     * @param theMinDamage the minimum damage the character can deal
     * @param theMaxDamage the maximum damage the character can deal
     * @param theAttackSpeed the speed at which the character attacks
     * @param theChanceToHit the probability (0.0 to 1.0) of successfully hitting a target
     */
    public DungeonCharacter(final String theName,
                            final int theHitPoints,
                            final int theMinDamage,
                            final int theMaxDamage,
                            final int theAttackSpeed,
                            final double theChanceToHit) {

        myName = theName;
        myHitPoints = theHitPoints;
        myMinDamage = theMinDamage;
        myMaxDamage = theMaxDamage;
        myAttackSpeed = theAttackSpeed;
        myChanceToHit = theChanceToHit;
    }

    /**
     * Attempts to attack another DungeonCharacter.
     * If the attack is successful based on chanceToHit,
     * a random damage value within the character's damage range is applied.
     *
     * @param theTarget the character being attacked
     */
    public void attack(final DungeonCharacter theTarget) {

        if (Math.random() <= myChanceToHit) {

            int theDamage = myMinDamage
                    + (int)(Math.random() * (myMaxDamage - myMinDamage + 1));

            theTarget.takeDamage(theDamage);
        }
    }

    /**
     * Applies damage to this character, reducing hit points.
     * Hit points will not drop below zero.
     *
     * @param theDamage the amount of damage taken
     */
    public void takeDamage(final int theDamage) {

        myHitPoints -= theDamage;

        if (myHitPoints < 0) {
            myHitPoints = 0;
        }
    }

    /**
     * Checks whether the character is still alive.
     *
     * @return true if hit points are greater than zero, false otherwise
     */
    public boolean isAlive() {
        return myHitPoints > 0;
    }

    /**
     * Returns the name of the character.
     *
     * @return the character name
     */
    public String getMyName() {
        return myName;
    }

    /**
     * Sets the name of the character.
     *
     * @param theName the new name of the character
     */
    public void setMyName(final String theName) {
        myName = theName;
    }

    /**
     * Returns the current hit points of the character.
     *
     * @return current hit points
     */
    public int getMyHitPoints() {
        return myHitPoints;
    }

    /**
     * Sets the hit points of the character.
     *
     * @param theHitPoints the new hit points value
     */
    public void setMyHitPoints(final int theHitPoints) {
        myHitPoints = theHitPoints;
    }

    /**
     * Returns the minimum damage this character can deal.
     *
     * @return minimum damage
     */
    public int getMyMinDamage() {
        return myMinDamage;
    }

    /**
     * Sets the minimum damage this character can deal.
     *
     * @param theMinDamage the new minimum damage
     */
    public void setMyMinDamage(final int theMinDamage) {
        myMinDamage = theMinDamage;
    }

    /**
     * Returns the maximum damage this character can deal.
     *
     * @return maximum damage
     */
    public int getMyMaxDamage() {
        return myMaxDamage;
    }

    /**
     * Sets the maximum damage this character can deal.
     *
     * @param theMaxDamage the new maximum damage
     */
    public void setMyMaxDamage(final int theMaxDamage) {
        myMaxDamage = theMaxDamage;
    }

    /**
     * Returns the attack speed of the character.
     *
     * @return attack speed
     */
    public int getMyAttackSpeed() {
        return myAttackSpeed;
    }

    /**
     * Sets the attack speed of the character.
     *
     * @param theAttackSpeed the new attack speed value
     */
    public void setMyAttackSpeed(final int theAttackSpeed) {
        myAttackSpeed = theAttackSpeed;
    }

    /**
     * Returns the chance this character has to successfully hit a target.
     *
     * @return chance to hit (0.0 to 1.0)
     */
    public double getMyChanceToHit() {
        return myChanceToHit;
    }

    /**
     * Sets the chance this character has to hit a target.
     *
     * @param theChanceToHit new chance to hit value (0.0 to 1.0)
     */
    public void setMyChanceToHit(final double theChanceToHit) {
        myChanceToHit = theChanceToHit;
    }

    /**
     * Returns a string representation of the character.
     *
     * @return formatted string containing name and hit points
     */
    @Override
    public String toString() {
        return myName + " HP:" + myHitPoints;
    }
}