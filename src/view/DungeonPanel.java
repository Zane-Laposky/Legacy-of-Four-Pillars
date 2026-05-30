/*
 * DungeonPanel.java
 * Spring 2026
 */
package view;

import model.Room;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * DungeonPanel displays the dungeon grid and visible rooms for the player.
 * It listens for "room" and "vision" property change events from the controller
 * and updates the 3x3 grid of RoomPanels accordingly.
 *
 * @author Emily Hernandez updated by Zane Laposky
 * @version 1.2 Spring 2026
 */
class DungeonPanel implements PropertyChangeListener {

    /** Number of rows and columns in the dungeon view grid. */
    private static final int DIMENSION = 3;

    /** Row index of the centre cell (hero's current room). */
    private static final int CENTRE = 1;

    /** Background colour of the dungeon panel. */
    private static final Color COL_BG = new Color(18, 18, 24);

    /** The outer panel containing the room grid. */
    private final JPanel myPanel;

    /** 2D array of room panels making up the dungeon view. */
    private final RoomPanel[][] myRooms;

    /**
     * Constructs a DungeonPanel and initialises the room grid.
     */
    DungeonPanel() {
        myPanel = new JPanel();
        myRooms = new RoomPanel[DIMENSION][DIMENSION];
        initPanel();
    }

    /**
     * Builds the dungeon grid by creating a 2D array of RoomPanels.
     */
    private void initPanel() {
        myPanel.setLayout(new GridLayout(DIMENSION, DIMENSION));
        myPanel.setBackground(COL_BG);

        for (int row = 0; row < DIMENSION; row++) {
            for (int col = 0; col < DIMENSION; col++) {
                myRooms[row][col] = new RoomPanel();
                myPanel.add(myRooms[row][col].getPanel());
            }
        }
    }

    /**
     * Responds to "room" and "vision" property change events.
     *
     * "room"   — clears all cells and redraws only the hero's current room
     *            in the centre, highlighted in gold.
     * "vision" — fills the surrounding eight cells with adjacent rooms.
     *
     * @param theEvent the property change event
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        final String property = theEvent.getPropertyName();

        if ("room".equals(property)) {
            handleRoomUpdate((Room) theEvent.getNewValue());
        } else if ("vision".equals(property)) {
            handleVisionUpdate((Room) theEvent.getNewValue());
        }
    }

    /**
     * Clears all panels and draws only the hero's current room in the centre.
     *
     * @param theCurrentRoom the room the hero is currently in
     */
    private void handleRoomUpdate(final Room theCurrentRoom) {
        for (int row = 0; row < DIMENSION; row++) {
            for (int col = 0; col < DIMENSION; col++) {
                if (row == CENTRE && col == CENTRE) {
                    myRooms[CENTRE][CENTRE].displayRoom(theCurrentRoom, true);
                } else {
                    myRooms[row][col].clearRoom();
                }
            }
        }
    }

    /**
     * Fills the eight surrounding panels with adjacent rooms when a vision
     * potion is used.
     *
     * @param theCurrentRoom the room the hero is currently in
     */
    private void handleVisionUpdate(final Room theCurrentRoom) {
        myRooms[0][CENTRE].displayRoom(theCurrentRoom.getNorthRoom(), false);
        myRooms[CENTRE][0].displayRoom(theCurrentRoom.getWestRoom(),  false);
        myRooms[CENTRE][2].displayRoom(theCurrentRoom.getEastRoom(),  false);
        myRooms[2][CENTRE].displayRoom(theCurrentRoom.getSouthRoom(), false);

        final Room northRoom = theCurrentRoom.getNorthRoom();
        if (northRoom != null) {
            myRooms[0][0].displayRoom(northRoom.getWestRoom(), false);
            myRooms[0][2].displayRoom(northRoom.getEastRoom(), false);
        }

        final Room southRoom = theCurrentRoom.getSouthRoom();
        if (southRoom != null) {
            myRooms[2][0].displayRoom(southRoom.getWestRoom(), false);
            myRooms[2][2].displayRoom(southRoom.getEastRoom(), false);
        }
    }

    /**
     * Returns the dungeon panel.
     *
     * @return the dungeon panel
     */
    JPanel getPanel() {
        return myPanel;
    }
}