/*
 * Dungeon Panel
 * Spring 2026
 */
package view;

import model.Room;

import javax.swing.*;
import java.awt.*;

/**
 * DungeonPanel displays the dungeon grid and visible rooms for the player.
 *
 * @author Emily Hernandez
 * @version Spring 2026
 */
public class DungeonPanel {

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
        //TESTING ONLY
        Room tempRoom = new Room(null);
        tempRoom.setEastRoom(new Room(null));

        myPanel.setLayout(new GridLayout(DIMENSION, DIMENSION));
        myPanel.setBackground(Color.BLACK);
        for (int row = 0; row < DIMENSION; row++) {
            for (int col = 0; col < DIMENSION; col++) {
                myRooms[row][col] = new RoomPanel();

                //Player's current room
                if (row == 1 && col == 1) {
                    myRooms[row][col].displayRoom(tempRoom);
                }

                myPanel.add(myRooms[row][col].getPanel());
            }
        }
    }

    /**
     * Display the surrounding rooms when the vision potion is used.
     *
     * @param theRoom the current room which player is in
     */
    public void displayVisionRooms(final Room theRoom) {
        for  (int row = 0; row < DIMENSION; row++) {
            for  (int col = 0; col < DIMENSION; col++) {
                if (row !=1 || col !=1 ) {
                    myRooms[row][col].displayRoom(new Room(null));
                }
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

    public void updateRoom(final Room theRoom) {
        //
    }

    public void enableVisionRooms(final Room theCurrentRoom) {
        //
    }

    public void displayCurrentRoom(Room theRoom) {
    }
}
