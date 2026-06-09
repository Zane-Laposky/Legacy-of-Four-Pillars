package controller;

import model.HealingPotion;
import model.Hero;
import model.Item;
import model.Pillar;
import model.Priestess;
import model.Thief;
import model.VisionPotion;
import model.Warrior;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores hero data in a format that can be saved to and loaded from the database.
 *
 * @author Ryan Nguyen
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
    private final String myInventoryItems;

    public PlayerWrapper(final String thePlayerName,
                         final String theHeroType,
                         final int theHitPoints,
                         final int theMinDamage,
                         final int theMaxDamage,
                         final int theAttackSpeed,
                         final double theChanceToHit,
                         final double theChanceToBlock,
                         final int theRoomX,
                         final int theRoomY,
                         final String theInventoryItems) {

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
        myInventoryItems = theInventoryItems == null ? "" : theInventoryItems;
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
                roomY,
                inventoryToString(theHero.getMyInventory())
        );
    }

    /**
     * Rebuilds a Hero object from the saved data.
     * <p>
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

        hero.addItem(inventoryFromString(myInventoryItems));

        return hero;
    }

    private static String inventoryToString(final Item[] theItems) {
        if (theItems == null || theItems.length == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        for (Item item : theItems) {
            if (item != null) {
                if (!result.isEmpty()) {
                    result.append("|");
                }

                result.append(item.getMyName());
            }
        }

        return result.toString();
    }

    private static Item[] inventoryFromString(final String theSavedItems) {
        if (theSavedItems == null || theSavedItems.isBlank()) {
            return new Item[0];
        }

        String[] itemNames = theSavedItems.split("\\|");
        List<Item> items = new ArrayList<>();

        for (String itemName : itemNames) {
            Item item = createItemFromName(itemName);

            if (item != null) {
                items.add(item);
            }
        }

        return items.toArray(new Item[0]);
    }

    private static Item createItemFromName(final String theName) {
        if (theName == null || theName.isBlank()) {
            return null;
        }

        if ("Healing Potion".equalsIgnoreCase(theName)) {
            return new HealingPotion();
        }

        if ("Vision Potion".equalsIgnoreCase(theName)) {
            return new VisionPotion();
        }

        if (theName.toLowerCase().contains("pillar")) {
            return new Pillar(theName);
        }

        return null;
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

    public String getInventoryItems() {
        return myInventoryItems;
    }

    @Override
    public String toString() {
        return "PlayerWrapper{" +
                "name='" + myPlayerName + '\'' +
                ", heroType='" + myHeroType + '\'' +
                ", hp=" + myHitPoints +
                ", roomX=" + myRoomX +
                ", roomY=" + myRoomY +
                ", inventoryItems='" + myInventoryItems + '\'' +
                '}';
    }
}
