package Model;

public abstract class Hero extends DungeonCharacter {

    private double myChanceToBlock;
    private SpecialAbility mySpecialAbility;

    public Hero(String theName, int theHitPoint, int theMinDamage, int theMaxDamage, int theAttackSpeed, double theChanceToHit) {
        super(theName, theHitPoint, theMinDamage, theMaxDamage, theAttackSpeed, theChanceToHit);
    }

    public double getMyChanceToBlock() {
        return myChanceToBlock;
    }
    public void setMyChanceToBlock(double theChanceToBlock) {
        myChanceToBlock = theChanceToBlock;
    }
    public SpecialAbility getMySpecialAbility() {
        return mySpecialAbility;
    }

    private class SpecialAbility{

    }
}
