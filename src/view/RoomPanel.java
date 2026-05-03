package view;

import model.Room;

import javax.swing.*;
import java.awt.*;

public class RoomPanel {
    private static final int DIMENSION = 3;
    private final JPanel myOneRoomPanel;
    private final JLabel[][] myRoomStructure;

    public RoomPanel() {
        myOneRoomPanel = new JPanel();
        myRoomStructure =  new JLabel[DIMENSION][DIMENSION];
        initPanel();
    }

    private void initPanel() {
        for (int row =  0; row < DIMENSION; row++) {
            for (int col =  0; col < DIMENSION; col++) {
                myRoomStructure[row][col] = new JLabel();
                myRoomStructure[row][col].setOpaque(true);
                myRoomStructure[row][col].setForeground(Color.WHITE);
                myOneRoomPanel.add(myRoomStructure[row][col]);
            }
        }
    }
    public JPanel getPanel() {
        return myOneRoomPanel;
    }

    //NEED INFORMATION FROM CONTROLLER ON THE CURRENT ROOM PLAYER IS IN
    public void drawCurrentRoom(final Room theCurrentRoom) {
        String[] tempRoom = theCurrentRoom.toString().split("%%");
        for (int row = 0; row < DIMENSION; row++) {
            for(int col = 0; col < DIMENSION; col++) {
                myRoomStructure[row][col].setText(String.valueOf(tempRoom[row].charAt(col)));

            }
        }
        myOneRoomPanel.revalidate();
        myOneRoomPanel.repaint();
    }
}
