import Controller.GameController;
import model.Dungeon;
import view.GameView;

public class Main {
    public static void main(String[] args) {
        int difficulty = 3;
        Dungeon myGameModel = new Dungeon(difficulty);
        GameView myGameGUI = new GameView();
        GameController myController = new GameController(myGameModel, myGameGUI);
    }
}
