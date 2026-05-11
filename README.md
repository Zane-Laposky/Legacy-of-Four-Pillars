# Legacy-of-Four-Pillars

Team Member: Emily Hernandnez, Zane Laposky, Devin Riel, Ryan Nguyen 
IDE: IntelliJ IDEA

A Java-based dungeon adventure game built with the MVC design pattern. Features a procedurally generated maze, hero classes, turn-based combat, item collection, and save/load functionality using serialization.

Features:

* Randomly generated dungeon based Game with interactive enviornments
* Combat system
* Player Interaction
* Save/Load Feature


Issues from Iteration 2:

1. Developing complete testing suites took substantial time and figuring out the best way to run a dungeon generation so I can efficiently test different dungeons was difficult as I kept running into an issue where the dungeon would never fully complete generating during a test despite when I independently test the generation it was perfectly fine. So I had to spend a decent amount of time redesigning the Dungeon Test Suit such that it consistently worked -Zane Laposky

2. My biggest issue with this iteration was trying to merge my branch to the main branch. There was a large amount of files that I had to check and choose wether to keep the code or switch it with what was already there. I spent a large amount of time going through the code and trying to figure out which was the better choice. - Devin Riel 

3. I had difficulty adding new panels to the game window. Each change affected the layout of other panels unexpectedly. Spent a couple hours testing and debugging different layout managers and split pane configurations. Eventually rewrote part of the GUI layout logic to properly organize the stats, inventory, and control panels. -Emily Hernandez
