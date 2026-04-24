package Model;

public abstract class DungeonCharacter {
    private String myName;
    private int myHitPoints;
    private int myMinDamage;
    private int myMaxDamage;
    private int myAttackSpeed;
    private double myChanceToHit;

    public DungeonCharacter(final String theName, final int theHitPoint,final int theMinDamage, final int theMaxDamage, final int theAttackSpeed, final double theChanceToHit) {
        myName = theName;
        myHitPoints = theHitPoint;
        myMinDamage = theMinDamage;
        myMaxDamage = theMaxDamage;
        myAttackSpeed = theAttackSpeed;
        myChanceToHit = theChanceToHit;
    }

    public void takeDamage(final int theDamage){

    }
    public boolean isAlive(){
        return myHitPoints > 0;
    }
    public void attack(final DungeonCharacter theDungeonCharacter){

    }



    public String getMyName(){
        return myName;
    }
    public void setMyName(final String theName){
        myName = theName;
    }
    public int getMyHitPoints(){
        return myHitPoints;
    }
    public void setMyHitPoints(final int theHitPoints){
        myHitPoints = theHitPoints;
    }
    public int getMyMinDamage(){
        return myMinDamage;
    }
    public void setMyMinDamage(final int theMinDamage){
        myMinDamage = theMinDamage;
    }
    public int getMyMaxDamage(){
        return myMaxDamage;
    }
    public void setMyMaxDamage(final int theMaxDamage){
        myMaxDamage = theMaxDamage;
    }
    public int getMyAttackSpeed(){
        return myAttackSpeed;
    }
    public void setMyAttackSpeed(final int theAttackSpeed){
        myAttackSpeed = theAttackSpeed;
    }
    public double getMyChanceToHit(){
        return myChanceToHit;
    }
    public void setMyChanceToHit(final double theChanceToHit){
        myChanceToHit = theChanceToHit;
    }

}
