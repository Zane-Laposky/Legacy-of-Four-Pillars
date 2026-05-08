package model;

/**
 * VisionPotion is an Item that grants the Hero enhanced vision,
 * allowing them to view surrounding rooms in the dungeon.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class VisionPotion extends Item {

    private static final int VISION_RANGE = 8;

    private final int myVisionRange;

    /**
     * Constructs a VisionPotion with a fixed vision range.
     */
    public VisionPotion() {
        super("Vision Potion");
        myVisionRange = VISION_RANGE;
    }

    /**
     * Returns the vision range provided by this potion.
     *
     * @return vision range
     */
    public int getMyVisionRange() {
        return myVisionRange;
    }
}