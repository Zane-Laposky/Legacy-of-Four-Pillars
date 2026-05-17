/*
 * GameMenuBar
 * Spring 2026
 */
package view;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This class creates the  menu bar for the game. The menu bar is
 * organized by menu items such as File, View, and Help for the game window.
 *
 * @author Emily Hernandez
 * @version 1.0 Spring 2026
 */
class GameMenuBar implements PropertyChangeListener {

    private final PropertyChangeSupport myChangeSupport;
    /**
     * Menu Bar used in the game window.
     */
    private final JMenuBar myMenuBar;

    private JMenuItem myNewGame;
    private JMenuItem myLoadGame;
    private JMenuItem myExitGame;
    private JMenuItem myAboutGame;
    private JMenuItem myGuidelines;
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
        myExitGame = new JMenuItem("Quit");

        //keyboard shortcut for each item in file
        myNewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        myLoadGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        myExitGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0));

        fileMenu.add(myNewGame);
        fileMenu.add(myLoadGame);
        fileMenu.add(myExitGame);

        return fileMenu;
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
        myGuidelines = new JMenuItem("Game Guidelines");
        myKeyboardSC = new JMenuItem("Keyboard Shortcuts");
        myAboutGame = new JMenuItem("About");

        helpMenu.add(myGuidelines);
        helpMenu.add(myKeyboardSC);
        helpMenu.add(myAboutGame);

        return helpMenu;
    }

    private void addListeners() {
        myNewGame.addActionListener(_ -> myChangeSupport.firePropertyChange(
                "menu", "", "NewGame"));
        myLoadGame.addActionListener(_ -> myChangeSupport.firePropertyChange(
                "menu", "", "LoadGame"));
        myExitGame.addActionListener(_ -> System.exit(0));

        myGuidelines.addActionListener(_ -> JOptionPane.showMessageDialog(
                null,
                """
                        Legacy of Four Pillars
                        
                        Explore a dungeon, collect all four Pillars of OO and reach the exit to win.
                        The hero may take damage from falling into pits or fighting monsters.
                        Use potions to heal and special abilities to defeat monsters.
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
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
