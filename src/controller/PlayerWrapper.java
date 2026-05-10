package Controller;
import model.Hero;
import model.Item;
import model.Room;


/**
 * PlayerWrapper is a helper class for the first version of the game.
 *
 * This class wraps around the Hero object so the controller can access
 * important hero information without directly handling every model detail.
 * It helps the controller get the hero, check hit points, add items to the
 * inventory, and check the hero's current room.
 *
 * @author Devin Riel
 * @version 1.0
 */
public class PlayerWrapper {
    /**
     * The hero controlled by the player.
     */
    private Hero myHero;

    /**
     * Creates a PlayerWrapper for the given hero.
     *
     * @param theHero the hero controlled by the player
     */
    public PlayerWrapper(Hero theHero) {
        this.myHero = theHero;
    }

    /**
     * Gets the hero stored inside this wrapper.
     *
     * @return the hero controlled by the player
     */
    public Hero getMyHero() {
        return myHero;
    }

    /**
     * Sets the hero stored inside this wrapper.
     *
     * @param theHero the new hero to store
     */
    public void setMyHero(Hero theHero) {
        this.myHero = theHero;
    }

    /**
     * Gets the hero's current hit points.
     *
     * @return the hero's current hit points
     */
    public int getHeroHitPoints() {
        return myHero.getMyHitPoints();
    }

    /**
     * Adds an item array to the hero's inventory.
     *
     * The Hero class currently expects an Item array, so this method passes
     * the array directly into the hero's addItem method.
     *
     * @param theItem the item array being added to the hero's inventory
     */
    public void addItemtoInventory(Item theItem []) {
        myHero.addItem(theItem);
    }

    /**
     * Gets the room the hero is currently inside.
     *
     * @return the hero's current room
     */
    public Room getCurrentRoom() {
        return myHero.getCurrentRoom();
    }


    /**
     * Checks whether the hero's current room has monsters.
     *
     * This only checks whether the monster array exists.
     * It does not check whether the monsters are alive.
     *
     * @return true if the current room has a monster array, false otherwise
     */
    public boolean currentRoomHasMonster() {
        if (myHero.getCurrentRoom().getMonsters() != null) {
            return true;
        } else  {
            return false;
        }
    }
}
