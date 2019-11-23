package jp.meao0525.battleemblem.battleclass;

public enum ClassStatus {
    SWORD_MASTER_STATUS(40.0,0.3F,6,0),
    BERSERKER_STATUS(40.0,0.3F,8,0),
    ARMOR_KNIGHT_STATUS(60.0,0.2F,6,2),
    BRAVE_HERO_STATUS(40.0,0.3F,4,1),
    SNIPER_STATUS(40.0,0.3F,1,0),
    ASSASSIN_STATUS(40.0,0.3F,4,0);


    private final double hp;
    private final float speed;
    private final int attack;
    private final int defence;

    private ClassStatus(double hp, float speed, int attack, int defence) {
        this.hp = hp;
        this.speed = speed;
        this.attack = attack;
        this.defence = defence;
    }

    public double getHp() {
        return hp;
    }

    public float getSpeed() {
        return speed;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }
}
