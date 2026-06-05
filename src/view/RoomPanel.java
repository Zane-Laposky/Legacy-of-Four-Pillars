/**
 * RoomPanel
 * Spring 2026
 */
package view;

import model.Room;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;

/**
 * Represent a single room panel in the game. It is a 3 by 3 grid layout where
 * each grid display the layout of the room based on the room created by the model.
 *
 * @author Emily Hernandez
 * @version 1.0 Spring 2026
 */
class RoomPanel {

    /**
     * Map each room symbol characters to image filename using map of entries
     */
    private static final Map<String, String> IMAGE_SYMBOLS =
            Map.ofEntries(
                    Map.entry("o", "ogre.png"),
                    Map.entry("g", "gremlins.png"),
                    Map.entry("s", "skeleton.png"),
                    Map.entry("♥", "health-potion.png"),
                    Map.entry("⚇", "VisionPotion.png"),
                    Map.entry("X", "exit-door.png"),
                    Map.entry("I", "pillar.png"),
                    Map.entry("A", "pillar.png"),
                    Map.entry("E", "pillar.png"),
                    Map.entry("P", "pillar.png"),
                    Map.entry("#", "entrance.png")
            );

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
        myRoomStructure = new JLabel[DIMENSION][DIMENSION];
        initPanel();
    }

    /**
     * Sets up a 3x3 grid of labels to display a room's characters.
     */
    private void initPanel() {
        myOneRoomPanel.setLayout(new GridLayout(DIMENSION, DIMENSION));
        myOneRoomPanel.setBackground(Color.BLACK);

        //each room has 3x3 grid . one character for each
        for (int row = 0; row < DIMENSION; row++) {
            for (int col = 0; col < DIMENSION; col++) {
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
     * Return the background image for wall scaling to the given size
     *
     * @param theSize dimension of the image
     * @return background image if file is found or else, it returns null
     */
    private ImageIcon getBackground(final int theSize) {
        try {
            ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/wall.png")));
            Image size = image.getImage().getScaledInstance(theSize, theSize, Image.SCALE_SMOOTH);
            return new ImageIcon(size);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Return a scaled image from the given file name to use as symbol
     *
     * @param theFileName image file name
     * @return scaled image or null if the file is not found
     */
    private ImageIcon getIcon(final String theFileName) {
        try {
            ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/" + theFileName)));
            int cellSize = myRoomStructure[1][1].getWidth();
            Image size = image.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
            return new ImageIcon(size);
        } catch (Exception e) {
            return null;
        }
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
            for (int col = 0; col < DIMENSION; col++) {
                myRoomStructure[row][col].setBackground(Color.BLACK);
                myRoomStructure[row][col].setForeground(Color.WHITE);

                String icon = String.valueOf(tempRoom[row].charAt(col));

                if (row == 1 && col == 1 && IMAGE_SYMBOLS.containsKey(icon)) {
                    myRoomStructure[row][col].setText("");
                    myRoomStructure[row][col].setIcon(getIcon(IMAGE_SYMBOLS.get(icon)));
                } else {
                    myRoomStructure[row][col].setIcon(null);
                    myRoomStructure[row][col].setText(icon);
                }
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
        if (theRoom != null) {
            drawRoom(theRoom);
        } else {
            clearRoom();
        }
    }

    /**
     * Clears the room panel by setting all labels to empty and
     * resetting the background to black.
     */
    public void clearRoom() {
        myOneRoomPanel.setBackground(Color.BLACK);
        for (int row = 0; row < DIMENSION; row++) {
            for (int col = 0; col < DIMENSION; col++) {
                myRoomStructure[row][col].setText("");
                int size = myRoomStructure[row][col].getWidth();
                myRoomStructure[row][col].setIcon(getBackground(size));
                myRoomStructure[row][col].setBackground(Color.BLACK);
            }
        }
    }
}

