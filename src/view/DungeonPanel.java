package view;

import model.Room;

import javax.swing.*;
import java.awt.*;

public class DungeonPanel {
    private static final int DIMENSION = 3;
    private final JPanel myPanel;
    private final RoomPanel[][] myRooms;

    public DungeonPanel() {
        myPanel = new JPanel();
        myRooms = new RoomPanel [DIMENSION][DIMENSION];
        initPanel();
    }

    private void initPanel() {
        //TESTING ONLY
        Room tempRoom = new Room(null);
        tempRoom.setEastRoom(new Room(null));

        myPanel.setLayout(new GridLayout(DIMENSION, DIMENSION));
        myPanel.setBackground(Color.DARK_GRAY);
        for (int row = 0; row < DIMENSION; row++) {
            for (int col = 0; col < DIMENSION; col++) {
                myRooms[row][col] = new RoomPanel();

                //Player's current room
                if (row == 1 && col == 1) {
                    myRooms[row][col].drawCurrentRoom(tempRoom);
                }

                myPanel.add(myRooms[row][col].getPanel());
            }
        }
    }




    public JPanel getPanel() {
        return myPanel;
    }
}