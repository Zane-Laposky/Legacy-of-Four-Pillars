/*
 * GameMenuBar
 * Spring 2026
 */
package view;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This class creates the  menu bar for the game. The menu bar is
 * organized by menu items such as File, View, and Help for the game window.
 *
 * @author Emily Hernandez
 * @version 1.0 Spring 2026
 */
class GameMenuBar {

    /**
     * Property Change support
     */
    private final PropertyChangeSupport myChangeSupport;

    /**
     * Menu Bar used in the game window.
     */
    private final JMenuBar myMenuBar;

    /**
     *  New game menu item
     */
    private JMenuItem myNewGame;

    /**
     * Load game menu item
     */
    private JMenuItem myLoadGame;

    /**
     * Save game menu item
     */
    private JMenuItem mySaveGame;

    /**
     * Exit game menu item.
     */
    private JMenuItem myExitGame;

    /**
     * About game menu item.
     */
    private JMenuItem myAboutGame;

    /**
     * Guidelines menu item.
     */
    private JMenuItem myGuidelines;

    /**
     * Keyboard shortcut menu item.
     */
    private JMenuItem myKeyboardSC;


    /**
     * Constructs a GameMenuBar object and adds all menus
     * items to the menu bar.
     */
    public GameMenuBar() {
        myMenuBar = new JMenuBar();
        myMenuBar.add(fileMenuBar());
        myMenuBar.add(helpMenuBar());
        myChangeSupport = new PropertyChangeSupport(this);
        addListeners();
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
        myNewGame = new JMenuItem("New");
        myLoadGame = new JMenuItem("Open...");
        mySaveGame = new JMenuItem("Save");
        myExitGame = new JMenuItem("Quit");

        //keyboard shortcut for each item in file
        myNewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        myLoadGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        myExitGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        mySaveGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));

        fileMenu.add(myNewGame);
        fileMenu.add(myLoadGame);
        fileMenu.add(mySaveGame);
        fileMenu.add(myExitGame);

        return fileMenu;
    }

    /**
     * Create help menu with game guidelines, keyboard shortcuts and about section that displays information
     * popups when clicked.
     *
     * @return the help menu
     */
    private JMenu helpMenuBar() {
        JMenu helpMenu = new JMenu("Help");

        //menu item in help
        myGuidelines = new JMenuItem("Game Guidelines");
        myKeyboardSC = new JMenuItem("Keyboard Shortcuts");
        myAboutGame = new JMenuItem("About");

        helpMenu.add(myGuidelines);
        helpMenu.add(myKeyboardSC);
        helpMenu.add(myAboutGame);

        return helpMenu;
    }

    /**
     * Set up action listener
     */
    private void addListeners() {
        myNewGame.addActionListener(_ -> myChangeSupport.firePropertyChange(
                "menu", "", "NewGame"));
        myLoadGame.addActionListener(_ -> myChangeSupport.firePropertyChange(
                "menu", "", "LoadGame"));
        mySaveGame.addActionListener(_ -> myChangeSupport.firePropertyChange(
                "menu", "", "SaveGame"));
        myExitGame.addActionListener(_ -> System.exit(0));

        myGuidelines.addActionListener(_ -> JOptionPane.showMessageDialog(
                null,
                """
                        Legacy of Four Pillars
                        
                        Explore a dungeon, collect all four Pillars of OO and reach the exit to win.
                        The hero may take damage from falling into pits or fighting monsters.
                        Use potions to heal and special abilities to defeat monsters.
                        
                        Items:
                        Healing Potion - ♥
                        Vision Potion - ⚇
                        Pillar of Inheritance - I
                        Pillar of Polymorphism - P
                        Pillar of Encapsulation - E
                        Pillar of Abstraction - A
                        
                        Enemies:
                        Ogre - o
                        Gremlin - g
                        Skeleton - s
                        
                        Other:
                        Locked Room - ⚿
                        Entrance - #
                        Exit - X
                        """
        ));

        myAboutGame.addActionListener(_ -> JOptionPane.showMessageDialog(
                null,
                """
                        Legacy of Four Pillars
                        
                        Created by Devin Riel, Ryan Nguyen, Zane Laposky, Emily Hernandez
                        
                        Version 1.0, Spring 2026
                        """
        ));

        myKeyboardSC.addActionListener(_ -> JOptionPane.showMessageDialog(
                null,
                """
                        Keyboard Shortcuts
                        
                        W / ↑ = move up
                        S / ↓ = move down
                        A / ← = move left
                        D / → = move right
                        
                        SPACE = Attack
                          Q   = Special Ability
                          E   = Pick up items
                          H   = Healing Potion
                          V   = Vision Potion
                        
                        
                        """
        ));
    }

    /**
     * Allow controller or other class to listen in on action changes
     */
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        myChangeSupport.addPropertyChangeListener(listener);
    }
}
