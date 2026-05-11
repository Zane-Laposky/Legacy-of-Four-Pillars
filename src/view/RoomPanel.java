/**
 * RoomPanel
 * Spring 2026
 */
package view;

import model.Room;

import javax.swing.*;
import java.awt.*;

/**
 * Represent a single room panel in the game. It is a 3 by 3 grid layout where
 * each grid display the layout of the room based on the room created by the model.
 *
 * @author Emily Hernandez
 * @version 1.0 Spring 2026
 */
public class RoomPanel {

    /**
     * Dimension of the room layout
     */
    private static final int DIMENSION = 3;

    /**
     * Panel representing one room
     */
    private final JPanel myOneRoomPanel;

    /**
     * 2D array storing the room display labels
     */
    private final JLabel[][] myRoomStructure;


    /**
     * Construct a RoomPanel object with requirement
     */
    public RoomPanel() {
        myOneRoomPanel = new JPanel();
        myRoomStructure =  new JLabel[DIMENSION][DIMENSION];
        initPanel();
    }


    /**
     * Sets up a 3x3 grid of labels to display a room's characters. 
     */
    private void initPanel() {
        myOneRoomPanel.setLayout(new GridLayout(DIMENSION, DIMENSION));
        myOneRoomPanel.setBackground(Color.BLACK);

        //each room has 3x3 grid . one character for each
        for (int row =  0; row < DIMENSION; row++) {
            for (int col =  0; col < DIMENSION; col++) {
                myRoomStructure[row][col] = new JLabel();
                myRoomStructure[row][col].setBackground(Color.BLACK);
                myRoomStructure[row][col].setHorizontalAlignment(JLabel.CENTER);
                myRoomStructure[row][col].setVerticalAlignment(JLabel.CENTER);
                myRoomStructure[row][col].setFont(myRoomStructure[row][col].getFont().deriveFont(Font.BOLD, 24));
                myRoomStructure[row][col].setOpaque(true);
                myOneRoomPanel.add(myRoomStructure[row][col]);
            }
        }
    }

    /**
     * Returns the room panel.
     *
     * @return the room panel
     */
    public JPanel getPanel() {
        return myOneRoomPanel;
    }


    /**
     * Draw a single room by converting the room's string representation into a 3x3
     * grid of characters displayed on the panel.
     *
     * @param theRoom the requested room.
     */
    private void drawRoom(final Room theRoom) {
        myOneRoomPanel.setBackground(Color.DARK_GRAY);
        String[] tempRoom = theRoom.toString().split("%%");
        for (int row = 0; row < DIMENSION; row++) {
            for(int col = 0; col < DIMENSION; col++) {
                myRoomStructure[row][col].setBackground(Color.DARK_GRAY);
                myRoomStructure[row][col].setForeground(Color.WHITE);
                myRoomStructure[row][col].setText( String.valueOf(tempRoom[row].charAt(col)));
            }
        }
        myOneRoomPanel.revalidate();
        myOneRoomPanel.repaint();
    }

    /**
     * Display the requested room by calling drawRoom method
     *
     * @param theRoom the requested room to Display
     */
    public void displayRoom(final Room theRoom) {
        drawRoom(theRoom);
    }
}

