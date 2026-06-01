# Legacy-of-Four-Pillars

Team Member: Emily Hernandnez, Zane Laposky, Devin Riel, Ryan Nguyen 
IDE: IntelliJ IDEA

link: https://github.com/Zane-Laposky/Legacy-of-Four-Pillars

A Java-based dungeon adventure game built with the MVC design pattern. Features a procedurally generated maze, hero classes, turn-based combat, item collection, and save/load functionality using serialization.

Issues from Iteration 4:

1. My biggest challenge for iteration 4 was ensuring tha that the new wall designs for the dungeons correctly matched one another so that upon generation you can visually and easily seperate the different rooms from one another - Zane Laposky

2. My biggest challenge this week was getting the GUI to work with the rest of the application. While the GUI worked correctly during manual testing, it was not running properly when launched through Main. I spent a significant amount of time researching how MVC works, studying how they should communicate with each other through property change events. I also reread and analyze all the classes written by my teammates to better understand the overall structure and how each component connected. I attempted to rewrite the data flow in a unidirectional style to resolve the communication issue, however after investing a large amount of time into this approach, an update from the controller ended up resolving the problem. I then abort my changes and continued working on adding features such as the New Game option in the menu bar, an end game prompt that handles both win and loss conditions, and a GUI test environment to verify that panels update correctly using mock property change events. Additionally, I spent time resolving merge conflicts that arose. - Emily Hernandez

3. My biggest issue this week was trying to get a timer to work properly. I have never used a timer before so I had to spend a while researching it and trying out different things before I got a working version. After I got a working version, I then had to play with the timing to make it so that the messages displayed where readable but still going at a good enough speed that you didn't have to wait a long time to see the next one. - Devin Riel (Iteration 5)

4. My issue during Iteration 4 was that the save/load system still needs to be connected more fully to the controller and GUI. I was able to test repeated saves in PersistenceTest.java, but the feature is still mostly being tested manually instead of through actual gameplay. - Ryan Nguyen
