/*
 * GameMenuBar
 * Spring 2026
 */
package view;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * This class creates the  menu bar for the game. The menu bar is
 * organized by menu items such as File, View, and Help for the game window.
 *
 * @author Emily Hernandez
 * @version 1.0 Spring 2026
 */
public class GameMenuBar {

    /**
     * Menu Bar used in the game window.
     */
    private final JMenuBar myMenuBar;

    /**
     * Constructs a GameMenuBar object and adds all menus
     * items to the menu bar.
     */
    public GameMenuBar() {
        myMenuBar = new JMenuBar();
        myMenuBar.add(fileMenuBar());
        myMenuBar.add(viewMenuBar());
        myMenuBar.add(helpMenuBar());
    }

    /**
     * Return the menu bar.
     *
     * @return the menu bar
     */
    public JMenuBar getMenuBar() {
        return myMenuBar;
    }

    /**
     * Create file menu with new, open, save and quit options, each with
     * keyboard shortcuts.
     *
     * @return the file menu
     */
    private JMenu fileMenuBar() {
        JMenu fileMenu = new JMenu("File");

        //menu item in file
        JMenuItem newGame = new JMenuItem("New");
        JMenuItem loadGame = new JMenuItem("Open...");
        JMenuItem saveGame = new JMenuItem("Save");
        JMenuItem exitGame = new JMenuItem("Quit");

        //keyboard shortcut for each item in file
        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        loadGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        saveGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        exitGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0));

        //STILL NEED ACTION
        //need to controller to tell what it does
        newGame.addActionListener(_ -> {

        });
        loadGame.addActionListener(_ -> {
        });
        saveGame.addActionListener(_ -> {
        });
        exitGame.addActionListener(_ -> {
            System.exit(0);
        });

        fileMenu.add(newGame);
        fileMenu.add(loadGame);
        fileMenu.add(saveGame);
        fileMenu.add(exitGame);

        return fileMenu;
    }

    /**
     * Create view menu with options to see inventory, character status and keyboard shortcut option.
     *
     * @return the view menu
     */
    private JMenu viewMenuBar() {
        JMenu viewMenu = new JMenu("View");

        //menu item in view
        JMenuItem inventoryView = new JMenuItem("Inventory");
        JMenuItem statusView = new JMenuItem("Character Stats");
        JMenuItem keyboardShortcuts = new JMenuItem("Keyboard Shortcuts");

        viewMenu.add(inventoryView);
        viewMenu.add(statusView);
        viewMenu.add(keyboardShortcuts);

        return viewMenu;
    }

    /**
     * Create help menu with game guidelines and about section that displays information
     * popups when clicked.
     *
     * @return the help menu
     */
    private JMenu helpMenuBar() {
        JMenu helpMenu = new JMenu("Help");

        //menu item in help
        JMenuItem guidelines = new JMenuItem("Game Guidelines");
        JMenuItem aboutGame = new JMenuItem("About");

        helpMenu.add(guidelines);
        helpMenu.add(aboutGame);

        guidelines.addActionListener(_ -> {
            JOptionPane.showMessageDialog(
                    null,
                    """
                            Legacy of Four Pillars
                            
                            Explore a dungeon, collect all four Pillars of OO and reach the exit to win.
                            The hero may take damage from falling into pits or fighting monsters.
                            Use potions to heal and special abilities to defeat monsters.
                            """
            );
        });

        aboutGame.addActionListener(_ -> {
            JOptionPane.showMessageDialog(
                    null,
                    """
                            Legacy of Four Pillars
                            
                            Created by Devin Riel, Ryan Nguyen, Zane Laposky, Emily Hernandez
                            
                            Version 1.0, Spring 2026
                            """
            );
        });
        return helpMenu;
    }

}
