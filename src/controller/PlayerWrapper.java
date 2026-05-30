package controller;

import model.Hero;
import model.Priestess;
import model.Thief;
import model.Warrior;

/**
 * Simple data object used for saving and loading a player's hero.
 *
 * I used this instead of saving the whole Hero object directly because
 * the database only needs simple values like name, class, HP, and room position.
 */
public class PlayerWrapper {

    private final String myPlayerName;
    private final String myHeroType;
    private final int myHitPoints;
    private final int myMinDamage;
    private final int myMaxDamage;
    private final int myAttackSpeed;
    private final double myChanceToHit;
    private final double myChanceToBlock;
    private final int myRoomX;
    private final int myRoomY;

    public PlayerWrapper(final String thePlayerName,
                         final String theHeroType,
                         final int theHitPoints,
                         final int theMinDamage,
                         final int theMaxDamage,
                         final int theAttackSpeed,
                         final double theChanceToHit,
                         final double theChanceToBlock,
                         final int theRoomX,
                         final int theRoomY) {

        myPlayerName = thePlayerName;
        myHeroType = theHeroType;
        myHitPoints = theHitPoints;
        myMinDamage = theMinDamage;
        myMaxDamage = theMaxDamage;
        myAttackSpeed = theAttackSpeed;
        myChanceToHit = theChanceToHit;
        myChanceToBlock = theChanceToBlock;
        myRoomX = theRoomX;
        myRoomY = theRoomY;
    }

    /**
     * Converts a Hero object into a simple save object.
     *
     * @param theHero hero to save
     * @return wrapped hero data
     */
    public static PlayerWrapper fromHero(final Hero theHero) {
        int roomX = -1;
        int roomY = -1;

        if (theHero.getCurrentRoom() != null) {
            roomX = theHero.getCurrentRoom().getX();
            roomY = theHero.getCurrentRoom().getY();
        }

        return new PlayerWrapper(
                theHero.getMyName(),
                theHero.getClass().getSimpleName(),
                theHero.getMyHitPoints(),
                theHero.getMyMinDamage(),
                theHero.getMyMaxDamage(),
                theHero.getMyAttackSpeed(),
                theHero.getMyChanceToHit(),
                theHero.getMyChanceToBlock(),
                roomX,
                roomY
        );
    }

    /**
     * Rebuilds a Hero object from the saved data.
     *
     * Room objects are not rebuilt here yet. For now, only the saved
     * room coordinates are stored so the controller can use them later.
     *
     * @return restored Hero
     */
    public Hero toHero() {
        Hero hero;

        if ("Priestess".equalsIgnoreCase(myHeroType)) {
            hero = new Priestess(myPlayerName);
        } else if ("Thief".equalsIgnoreCase(myHeroType)) {
            hero = new Thief(myPlayerName);
        } else {
            hero = new Warrior(myPlayerName);
        }

        hero.setMyHitPoints(myHitPoints);
        hero.setMyMinDamage(myMinDamage);
        hero.setMyMaxDamage(myMaxDamage);
        hero.setMyAttackSpeed(myAttackSpeed);
        hero.setMyChanceToHit(myChanceToHit);
        hero.setMyChanceToBlock(myChanceToBlock);

        return hero;
    }

    public String getPlayerName() {
        return myPlayerName;
    }

    public String getHeroType() {
        return myHeroType;
    }

    public int getHitPoints() {
        return myHitPoints;
    }

    public int getMinDamage() {
        return myMinDamage;
    }

    public int getMaxDamage() {
        return myMaxDamage;
    }

    public int getAttackSpeed() {
        return myAttackSpeed;
    }

    public double getChanceToHit() {
        return myChanceToHit;
    }

    public double getChanceToBlock() {
        return myChanceToBlock;
    }

    public int getRoomX() {
        return myRoomX;
    }

    public int getRoomY() {
        return myRoomY;
    }

    @Override
    public String toString() {
        return "PlayerWrapper{" +
                "name='" + myPlayerName + '\'' +
                ", heroType='" + myHeroType + '\'' +
                ", hp=" + myHitPoints +
                ", roomX=" + myRoomX +
                ", roomY=" + myRoomY +
                '}';
    }
}

