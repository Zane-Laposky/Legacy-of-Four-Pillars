# Legacy-of-Four-Pillars

Team Member: Emily Hernandnez, Zane Laposky, Devin Riel, Ryan Nguyen 
IDE: IntelliJ IDEA

link: https://github.com/Zane-Laposky/Legacy-of-Four-Pillars

A Java-based dungeon adventure game built with the MVC design pattern. Features a procedurally generated maze, hero classes, turn-based combat, item collection, and save/load functionality using serialization.

Issues from Iteration 5:

1. My biggest challenge was making sure that each menu item is working properly. It took many debugging and updating logic so it save and load event fire before controller was connected. I also spent good amount of time debugging the logic for vision potion display room. Time to time I had merge conflict with teammate's changes, I end up keeping the cleaner version. -Emily Hernandez

2. My issue during this iteration was that the save/load system had multiple problems at the same time. Player data was being saved through SQLite, while dungeon data was being saved through serialization, which made the save/load flow harder to test and debug. I worked on fixing this by adding the SQLite JDBC driver, updating the player save data to include missing inventory items, and testing the persistence system to make sure saved player data could be loaded back correctly. -Ryan Nguyen
