package jp.meao0525.battleemblem.beplayer;

import jp.meao0525.battleemblem.battleclass.BattleClass;
import jp.meao0525.battleemblem.battleclass.ClassStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BePlayer {
    private Player player;
    private int attack;
    private int defence;

    public BePlayer(Player player) {
        this.player = player;
    }

    public void setBattleClass(BattleClass battleClass) {
        //バトルクラスを使用中にする
        battleClass.setUsed(true);

        //headerにクラス名をセット
        player.setPlayerListHeader(battleClass.getName());

        //プレイヤーのステータスをセットしていくよ
        ClassStatus status = battleClass.getStatus();
        player.setHealthScale(status.getHp()); //HP
        player.setWalkSpeed(status.getSpeed()); //足の速さ
        setAttack(status.getAttack()); //攻撃
        setDefence(status.getDefence()); //防御

        //TODO: 装備を与える

        //ロードアウトセレクター回収する
        player.getInventory().remove(Material.EMERALD);
    }

    public void removeBattleClass() {
        //TODO: headerを空にする
        //TODO: ステータスを元に戻す
        //TODO: 攻撃と防御を空に
        //TODO: バトルクラスのUsedをfalseにする
        //TODO: ロードアウトセレクターを渡す
    }

    public Player getPlayer() {
        return player;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }
}
