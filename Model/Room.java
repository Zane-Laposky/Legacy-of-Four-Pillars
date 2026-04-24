package Model;

public class Room {
    private boolean hasHealingPotion;
    private boolean hasVisionPotion;
    private boolean isEntrance;
    private boolean isExit;
    private Pillar myPillar;
    private boolean northRoomExists;
    private boolean southRoomExists;
    private boolean westRoomExists;
    private boolean eastRoomExists;
    private Monster[] myMonsters;

    private boolean[] myEntranceRequirements;
    private boolean hasVisitedBefore;

    public Room(final boolean theHealingPotion, final boolean theVisionPotion, final boolean theEntrance, final boolean theExit, final Pillar thePillar, final boolean theNorthRoom, final boolean theSouthRoom, final boolean theWestRoom, final boolean theEastRoom, final Monster[] theMonsters, final boolean[] theEntranceRequirements) {
        hasHealingPotion = theHealingPotion;
        hasVisionPotion = theVisionPotion;
        isEntrance = theEntrance;
        isExit = theExit;
        myPillar = thePillar;
        northRoomExists = theNorthRoom;
        southRoomExists = theSouthRoom;
        westRoomExists = theWestRoom;
        eastRoomExists = theEastRoom;
        myMonsters = theMonsters;
        myEntranceRequirements = theEntranceRequirements;
        hasVisitedBefore = false;
    }


    public void enter(final Hero theHero){

    }

    public void removeItems(){

    }

    @Override
    public String toString() {
        return super.toString();
    }

    public boolean getHasHealingPotion() {
        return hasHealingPotion;
    }
    public boolean getHasVisionPotion() {
        return hasVisionPotion;
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
    public boolean getNorthRoomExists() {
        return northRoomExists;
    }
    public boolean getSouthRoomExists() {
        return southRoomExists;
    }
    public boolean getWestRoomExists() {
        return westRoomExists;
    }
    public boolean getEastRoomExists() {
        return eastRoomExists;
    }
    public Monster[] getMyMonsters() {
        return myMonsters;
    }
    public boolean[] getMyEntranceRequirements() {
        return myEntranceRequirements;
    }
    public boolean getHasVisitedBefore() {
        return hasVisitedBefore;
    }



    public void setHasHealingPotion(boolean hasHealingPotion) {
        this.hasHealingPotion = hasHealingPotion;
    }
    public void setHasVisionPotion(boolean hasVisionPotion) {
        this.hasVisionPotion = hasVisionPotion;
    }
    public void setIsEntrance(boolean isEntrance) {
        this.isEntrance = isEntrance;
    }
    public void setIsExit(boolean isExit) {
        this.isExit = isExit;
    }
    public void setNorthRoomExists(boolean northRoomExists) {
        this.northRoomExists = northRoomExists;
    }
    public void setSouthRoomExists(boolean southRoomExists) {
        this.southRoomExists = southRoomExists;
    }
    public void setWestRoomExists(boolean westRoomExists) {
        this.westRoomExists = westRoomExists;
    }
    public void setEastRoomExists(boolean eastRoomExists) {
        this.eastRoomExists = eastRoomExists;
    }
    public void setMyMonsters(Monster[] myMonsters) {
        this.myMonsters = myMonsters;
    }
    public void setMyEntranceRequirements(boolean[] myEntranceRequirements) {
        this.myEntranceRequirements = myEntranceRequirements;
    }
    public void setHasVisitedBefore(boolean hasVisitedBefore) {
        this.hasVisitedBefore = hasVisitedBefore;
    }

}
