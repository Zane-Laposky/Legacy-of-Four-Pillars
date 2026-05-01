package Model;

import java.util.Arrays;

public class Room {
    private Item[] myItems;
    private boolean isEntrance;
    private boolean isExit;
    private Pillar myPillar;
    private Room myNorthRoom;
    private Room mySouthRoom;
    private Room myWestRoom;
    private Room myEastRoom;
    private Monster[] myMonsters;

    private Item[] myEntranceRequirements;
    private boolean hasVisitedBefore;

    public Room(final Item[] theEntranceRequirements){
        myItems = new Item[0];
        isEntrance = false;
        isExit = false;
        myPillar = null;
        myNorthRoom = null;
        mySouthRoom = null;
        myWestRoom = null;
        myEastRoom = null;
        myMonsters = new Monster [0];
        myEntranceRequirements = theEntranceRequirements;
        hasVisitedBefore = false;
    }

    public void enter(final Hero theHero){
        if (myEntranceRequirements != null) {
            for (Item requiredItem : myEntranceRequirements) {
                boolean found = false;
                for (Item heroItem : theHero.getMyInventory()) {
                    if (heroItem == requiredItem) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("You cannot enter this room. Missing required item: " + requiredItem.toString());
                    return;
                }
            }
        }
        theHero.setCurrentRoom(this);
        System.out.println("Entered the Room");
        setHasVisitedBefore(true);
    }

    public void tryDoor(final Hero theHero, final String theDirection){
        if (theDirection.equals("North")) {
            if (myNorthRoom != null) {
                myNorthRoom.enter(theHero);
            } else {
                System.out.println("There is no Room in that direction");
            }
        } else if (theDirection.equals("South")) {
            if (mySouthRoom != null) {
                mySouthRoom.enter(theHero);
            } else {
                System.out.println("There is no Room in that direction");
            }
        } else if (theDirection.equals("West")) {
            if (myWestRoom != null) {
                myWestRoom.enter(theHero);
            }  else {
                System.out.println("There is no Room in that direction");
            }
        } else if (theDirection.equals("East")) {
            if (myEastRoom != null) {
                myEastRoom.enter(theHero);
            }  else {
                System.out.println("There is no Room in that direction");
            }
        }
    }

    public void removeItems(final String theItemName){
        if (myItems != null) {
            for (Item item : myItems) {
                if (item.toString().contains(theItemName.toLowerCase())) {
                    myItems = Arrays.stream(myItems).filter(i -> i != item).toArray(Item[]::new);
                }
            }
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Item[] getItems(){
        return myItems;
    }
    public boolean getIsEntrance() {
        return isEntrance;
    }
    public boolean getIsExit() {
        return isExit;
    }
    public Pillar getMyPillar() {
        return myPillar;
    }
    public Room getNorthRoomExists() {
        return myNorthRoom;
    }
    public Room getSouthRoomExists() {
        return mySouthRoom;
    }
    public Room getWestRoomExists() {
        return myWestRoom;
    }
    public Room getEastRoomExists() {
        return myEastRoom;
    }
    public Monster[] getMyMonsters() {
        return myMonsters;
    }
    public Item[] getMyEntranceRequirements() {
        return myEntranceRequirements;
    }
    public boolean getHasVisitedBefore() {
        return hasVisitedBefore;
    }



    public void addItem(final Item theItem){
        myItems = Arrays.copyOf(myItems, myItems.length + 1);
        myItems[myItems.length-1] = theItem;
    }
    public void setIsEntrance(final boolean isEntrance) {
        this.isEntrance = isEntrance;
    }
    public void setIsExit(final boolean isExit) {
        this.isExit = isExit;
    }
    public void setNorthRoomExists(final Room northRoomExists) {
        this.myNorthRoom = northRoomExists;
    }
    public void setSouthRoomExists(final Room southRoomExists) {
        this.mySouthRoom = southRoomExists;
    }
    public void setWestRoomExists(final Room westRoomExists) {
        this.myWestRoom = westRoomExists;
    }
    public void setEastRoomExists(final Room eastRoomExists) {
        this.myEastRoom = eastRoomExists;
    }
    public void setMyMonsters(final Monster[] myMonsters) {
        this.myMonsters = myMonsters;
    }
    public void setMyEntranceRequirements(final Item[] myEntranceRequirements) {
        this.myEntranceRequirements = myEntranceRequirements;
    }
    public void setHasVisitedBefore(final boolean hasVisitedBefore) {
        this.hasVisitedBefore = hasVisitedBefore;
    }
    
    
}
