/*
 * Dungeon Panel
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
 *
 * @author Emily Hernandez
 * @version Spring 2026
 */
class DungeonPanel implements PropertyChangeListener {

    /**
     * Dimension of the player's view for the game
     */
    private static final int DIMENSION = 3;

    /**
     * Dungeon Panel
     */
    private final JPanel myPanel;

    /**
     * 2D array grid of room panel for dungeon display
     */
    private final RoomPanel[][] myRooms;

    /**
     * Constructs and initializes panels
     */
    public DungeonPanel() {
        myPanel = new JPanel();
        myRooms = new RoomPanel[DIMENSION][DIMENSION];
        initPanel();
    }

    /**
     * Builds the dungeon grid by creating a 2D array of room panels.
     */
    private void initPanel() {
        myPanel.setLayout(new GridLayout(DIMENSION, DIMENSION));
        myPanel.setBackground(Color.BLACK);
        for (int row = 0; row < DIMENSION; row++) {
            for (int col = 0; col < DIMENSION; col++) {
                myRooms[row][col] = new RoomPanel();
                myPanel.add(myRooms[row][col].getPanel());
            }
        }
    }

    /**
     * Set up the buttons between update according to receive events from others
     *
     * @param theEvent Property Change Event
     */
    @Override
    public void propertyChange(PropertyChangeEvent theEvent) {
        if (theEvent.getPropertyName().equals("room")) {
            for (int row = 0; row < DIMENSION; row++) {
                for (int col = 0; col < DIMENSION; col++) {
                    if (row == 1 && col == 1) {
                        myRooms[1][1].displayRoom((Room) theEvent.getNewValue());
                    } else {
                        myRooms[row][col].clearRoom();
                    }
                }
            }

        }

        if  (theEvent.getPropertyName().equals("vision")) {
            Room tempCurrentRoom = (Room) theEvent.getNewValue();

            myRooms[0][1].displayRoom(tempCurrentRoom.getNorthRoom());
            myRooms[1][0].displayRoom(tempCurrentRoom.getWestRoom());
            myRooms[1][2].displayRoom(tempCurrentRoom.getEastRoom());
            myRooms[2][1].displayRoom(tempCurrentRoom.getSouthRoom());

            if  (tempCurrentRoom.getNorthRoom() != null) {
                myRooms[0][0].displayRoom(tempCurrentRoom.getNorthRoom().getWestRoom());
                myRooms[0][2].displayRoom(tempCurrentRoom.getNorthRoom().getEastRoom());
            }
            if  (tempCurrentRoom.getSouthRoom() != null) {
                myRooms[2][0].displayRoom(tempCurrentRoom.getSouthRoom().getWestRoom());
                myRooms[2][2].displayRoom(tempCurrentRoom.getSouthRoom().getEastRoom());
            }
        }
    }

    /**
     * Return the Dungeon panel
     *
     * @return the dungeon panel
     */
    public JPanel getPanel() {
        return myPanel;
    }
}
