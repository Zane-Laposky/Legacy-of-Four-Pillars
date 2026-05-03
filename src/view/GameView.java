package view;

import javax.swing.*;
import java.awt.*;

public class GameView {
    JFrame myFrame;

    public GameView() {

        //set up general window dimension
        myFrame = new JFrame("Legacy of Four Pillars");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(500, 500);
        myFrame.setMinimumSize(new Dimension(300, 300));
        myFrame.setResizable(true);

        //prompt for character name in popup window
        String characterName = namePrompt();

        //initial GUI
        initGuiComponent();

        // must be last to make everything visible
        myFrame.setVisible(true);
    }

    //GUI initial component
    private void initGuiComponent() {
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myFrame.add(myPanel);
        myFrame.setJMenuBar(new MenuBar().getMenuBar());

    }


    //prompt window for the user to name the character
    public String namePrompt() {
        String myCharacterName = null;
        while (myCharacterName == null || myCharacterName.trim().isEmpty()) {
            myCharacterName = JOptionPane.showInputDialog(
                    myFrame,
                    "Name your character: ",
                    "Character Name",
                    JOptionPane.QUESTION_MESSAGE
            );
        }
        return myCharacterName;
    }

    // testing purpose ONLY2
//    public static void main(String[] args) {
//        new GameView();
//    }
}
