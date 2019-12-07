package jp.meao0525.battleemblem.beplayer;

import jp.meao0525.battleemblem.battleclass.BattleClass;
import jp.meao0525.battleemblem.battleclass.ClassStatus;
import jp.meao0525.battleemblem.beitem.BeItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

        //装備を与える
        player.getInventory().addItem(battleClass.getItem().toItemStack());
        //鎧は必要ですか？
        if (battleClass.equals(BattleClass.ARMOR_KNIGHT) || battleClass.equals(BattleClass.BRAVE_HERO)) {
            setArmor(getPlayer(), battleClass);
        }

        //ロードアウトセレクター回収する
        getPlayer().getInventory().remove(Material.EMERALD);
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

    private void setArmor(Player player, BattleClass battleClass) {
        //同じ操作してるのが気持ち悪い
        if (battleClass.equals(BattleClass.ARMOR_KNIGHT)) {
            //重鎧兵の装備
            player.getInventory().setHelmet(BeItems.KNIGHT_HELMET.toItemStack());
            player.getInventory().setChestplate(BeItems.KNIGHT_CHESTPLATE.toItemStack());
            player.getInventory().setLeggings(BeItems.KNIGHT_LEGGINGS.toItemStack());
            player.getInventory().setBoots(BeItems.KNIGHT_BOOTS.toItemStack());
        } else {
            //勇者の装備
            player.getInventory().setHelmet(BeItems.BRAVE_HELMET.toItemStack());
            player.getInventory().setChestplate(BeItems.BRAVE_CHESTPLATE.toItemStack());
            player.getInventory().setLeggings(BeItems.BRAVE_LEGGINGS.toItemStack());
            player.getInventory().setBoots(BeItems.BRAVE_BOOTS.toItemStack());
        }
    }
}
