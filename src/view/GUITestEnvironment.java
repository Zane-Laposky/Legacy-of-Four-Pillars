//TESTING PURPOSE ONLY
package view;

import model.Room;
import model.Skeleton;
import model.VisionPotion;
import model.Warrior;

public class GUITestEnvironment {
    public static void main(String[] args) {
        GameView testEnvironment = new GameView();


        //temp Room to test
        Room testRoom = new Room(null);
        Room testERoom = new Room(null);
        Room testNRoom = new Room(null);

        testRoom.setEastRoom(testERoom);
        testRoom.setNorthRoom(testNRoom);

        testNRoom.addItem(new VisionPotion());
        testERoom.addMonster(new Skeleton());

        //temp character
        Warrior testWarrior = new Warrior("SUBJECT11");
        testWarrior.setMyAttackSpeed(10);
        testWarrior.setMyChanceToBlock(7.0);
        testWarrior.setMyMaxDamage(100);

        //fire changes
        testEnvironment.testFireEvent("room", null, testRoom);
        testEnvironment.testFireEvent("Hero", null, testWarrior);
        testEnvironment.testFireEvent("Pillar", 0, 3);
        testEnvironment.testFireEvent("message", "", "Testing 1 2 3");
    }

}
