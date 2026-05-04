package Controller;

/**
 * Main is the starting point for the first version of the game.
 *
 * This class contains the main method, which Java uses to begin running
 * the program. It starts the game by creating a new GameController.
 *
 * @author Devin Riel
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        /*
         * Creates a new GameController.
         * The GameController handles setting up the hero, starting room,
         * game view, stats panel, and dungeon controller.
         */
        new GameController();
    }
}
