package Model;

public abstract class Monster extends DungeonCharacter {

    private double myChanceToHeal;
    private int myMinHeal;
    private int myMaxHeal;

    public Monster(String theName, int theHitPoint, int theMinDamage, int theMaxDamage, int theAttackSpeed, double theChanceToHit) {
        super(theName, theHitPoint, theMinDamage, theMaxDamage, theAttackSpeed, theChanceToHit);
    }

    public double getMyChanceToHeal() {
        return myChanceToHeal;
    }
    public void setMyChanceToHeal(double theChanceToHeal) {
        myChanceToHeal = theChanceToHeal;
    }
    public int getMyMinHeal() {
        return myMinHeal;
    }
    public void setMyMinHeal(int theMinHeal) {
        myMinHeal = theMinHeal;
    }
    public int getMyMaxHeal() {
        return myMaxHeal;
    }
    public void setMyMaxHeal(int theMaxHeal) {
        myMaxHeal = theMaxHeal;
    }

    public void heal(){

    }
}
