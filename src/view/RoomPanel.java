/*
 * RoomPanel.java
 * Spring 2026
 */
package view;

import model.Room;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a single room panel in the game. It is a 3 by 3 grid layout where
 * each cell displays one character from the room's string representation.
 *
 * @author Emily Hernandez updated by Zane Laposky
 * @version 1.2 Spring 2026
 */
class RoomPanel {

    /** Number of rows and columns in the room grid. */
    private static final int DIMENSION = 3;

    /** Background colour shared across all room states. */
    private static final Color COL_BG = new Color(18, 18, 24);

    /** Foreground colour for visited rooms. */
    private static final Color COL_VISITED = new Color(200, 215, 245);

    /** Foreground colour for the hero's current room. */
    private static final Color COL_CURRENT = new Color(255, 220, 60);

    /** Font size used for room characters. */
    private static final int FONT_SIZE = 18;

    /** Panel representing one room. */
    private final JPanel myOneRoomPanel;

    /** 2D array storing the room display labels. */
    private final JLabel[][] myRoomStructure;

    /** Whether this panel is currently displaying the hero's room. */
    private boolean myIsCurrent;

    /**
     * Constructs a RoomPanel and initialises its grid of labels.
     */
    RoomPanel() {
        myOneRoomPanel = new JPanel();
        myRoomStructure = new JLabel[DIMENSION][DIMENSION];
        myIsCurrent = false;
        initPanel();
    }

    /**
     * Sets up a 3x3 grid of labels to display a room's characters.
     */
    private void initPanel() {
        myOneRoomPanel.setLayout(new GridLayout(DIMENSION, DIMENSION));
        myOneRoomPanel.setBackground(COL_BG);

        for (int row = 0; row < DIMENSION; row++) {
            for (int col = 0; col < DIMENSION; col++) {
                myRoomStructure[row][col] = new JLabel();
                myRoomStructure[row][col].setBackground(COL_BG);
                myRoomStructure[row][col].setForeground(COL_VISITED);
                myRoomStructure[row][col].setHorizontalAlignment(JLabel.CENTER);
                myRoomStructure[row][col].setVerticalAlignment(JLabel.CENTER);
                myRoomStructure[row][col].setFont(
                        new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE));
                myRoomStructure[row][col].setOpaque(true);
                myOneRoomPanel.add(myRoomStructure[row][col]);
            }
        }
    }

    /**
     * Returns the underlying Swing panel.
     *
     * @return the room panel
     */
    JPanel getPanel() {
        return myOneRoomPanel;
    }

    /**
     * Displays the given room, highlighting it if it is the hero's current room.
     *
     * @param theRoom      the room to display, or null to clear
     * @param theIsCurrent true if the hero is currently in this room
     */
    void displayRoom(final Room theRoom, final boolean theIsCurrent) {
        myIsCurrent = theIsCurrent;
        if (theRoom != null) {
            drawRoom(theRoom);
        } else {
            clearRoom();
        }
    }

    /**
     * Clears all labels in this panel.
     */
    void clearRoom() {
        myIsCurrent = false;
        for (int row = 0; row < DIMENSION; row++) {
            for (int col = 0; col < DIMENSION; col++) {
                myRoomStructure[row][col].setBackground(COL_BG);
                myRoomStructure[row][col].setForeground(COL_BG);
                myRoomStructure[row][col].setText("");
            }
        }
    }

    /**
     * Converts the room's string representation into a 3x3 grid of characters
     * and paints them onto the panel labels.
     *
     * @param theRoom the room to draw
     */
    private void drawRoom(final Room theRoom) {
        final Color foreground = myIsCurrent ? COL_CURRENT : COL_VISITED;
        final String[] rows = theRoom.toString().split("%%");

        myOneRoomPanel.setBackground(COL_BG);

        for (int row = 0; row < DIMENSION; row++) {
            for (int col = 0; col < DIMENSION; col++) {
                final JLabel cell = myRoomStructure[row][col];
                cell.setBackground(COL_BG);
                cell.setForeground(foreground);

                if (row < rows.length && col < rows[row].length()) {
                    cell.setText(String.valueOf(rows[row].charAt(col)));
                } else {
                    cell.setText(" ");
                }
            }
        }

        myOneRoomPanel.revalidate();
        myOneRoomPanel.repaint();
    }
}