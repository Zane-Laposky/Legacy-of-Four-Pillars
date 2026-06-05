package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * VisionPotion is an Item that grants the Hero enhanced vision,
 * allowing them to view surrounding rooms in the dungeon.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class VisionPotion extends Item implements Serializable {

    /**
     * serialVersionUID for load and save game
     */
    @Serial
    private static final long serialVersionUID = 1L;

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