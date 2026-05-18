# Legacy-of-Four-Pillars

Team Member: Emily Hernandnez, Zane Laposky, Devin Riel, Ryan Nguyen 
IDE: IntelliJ IDEA

link: https://github.com/Zane-Laposky/Legacy-of-Four-Pillars

A Java-based dungeon adventure game built with the MVC design pattern. Features a procedurally generated maze, hero classes, turn-based combat, item collection, and save/load functionality using serialization.

Features:

*


Issues from Iteration 3:

1. The challenge I had this week was figuring out how to let the controller know what is happening on the GUI without giving it direct access to every panel. I learned how to use PropertyChangeSupport and PropertyChangeListener properly to send and receive signals. - Emily Hernandez

2. My biggest issue this week was trying to use PropertyChangeListener and redo all the code to use it. I also struggled with tryint to get the view and the control to communicate properly through the PropertyChangeListener and had to spend several hours researhing PropertyChangeListener to figure out how to properly set it all up. - Devin Riel 

3. During Iteration 3 was improving the Data/save-load testing. I updated PersistenceTest.java so it tested multiple hero types instead of only one hero. The test now checks Warrior, Thief, and Priestess save/load behavior and verifies basic saved values like name, class, HP, damage stats, attack speed, chance to hit, and chance to block. The main issue is that this is still a manual test, and the save/load system still needs to be connected more fully to the controller and GUI for actual gameplay. - Ryan Nguyen
