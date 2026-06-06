//TESTING PURPOSE ONLY
package view;

import model.Room;
import model.Skeleton;
import model.VisionPotion;

class GUITestEnvironment {
     static void main() throws InterruptedException {
        GameView testEnvironment = new GameView();
        testEnvironment.showFrame();

        //temp Room to test
        Room testRoom = new Room(null);
        Room testERoom = new Room(null);
        Room testNRoom = new Room(null);

        testRoom.setEastRoom(testERoom);
        testRoom.setNorthRoom(testNRoom);

        testNRoom.addItem(new VisionPotion());
        testERoom.addMonster(new Skeleton());

        //Listener in GameView
        testEnvironment.testFireEvent("message", "", "Testing 1 2 3");
        Thread.sleep(3000);

        //testing room view
        testEnvironment.testFireEvent("message", "", "Entered Room");
        testEnvironment.testFireEvent("room", null, testRoom);
        Thread.sleep(3000);

        //testing monster in the room
        testEnvironment.testFireEvent("message", "", "Monster in the Room");
        testEnvironment.testFireEvent("room", null, testERoom);
        testEnvironment.testFireEvent("Monster", false, true);
        Thread.sleep(3000);

        //status panel update testing
        testEnvironment.testFireEvent("message", "", "Change in inventory");
        testEnvironment.testFireEvent("Pillar", 0, 3);
        testEnvironment.testFireEvent("HealingPotion", 0, 4);
        Thread.sleep(3000);

        //vision potion activated
        testEnvironment.testFireEvent("message", "", "Vision Potion Activated");
        testEnvironment.testFireEvent("room", testERoom, testRoom);
        testEnvironment.testFireEvent("vision", null, testRoom);
        Thread.sleep(3000);

        //no file to load
        testEnvironment.testFireEvent("noSaveFile", null, null);

    }

}
