package jp.meao0525.battleemblem.beplayer;

import jp.meao0525.battleemblem.BattleEmblemMain;
import jp.meao0525.battleemblem.battleclass.BattleClass;
import jp.meao0525.battleemblem.battleclass.ClassStatus;
import jp.meao0525.battleemblem.beitem.BeItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Timer;
import java.util.TimerTask;

import static jp.meao0525.battleemblem.beplayer.BePlayerStatus.*;


public class BePlayer {
    private Player player;
    private BattleClass battleClass;
    private double attack;
    private double defence;

    private int life = PLAYER_LIFE;

    private boolean ability = false;
    private int cooldown = -1;

    private Timer cdTimer;

    public BePlayer(Player player) {
        this.player = player;
    }

    public void setBattleClass(BattleClass battleClass) {
        this.battleClass = battleClass;

        //バトルクラスを使用中にする
        battleClass.setUsed(true);

        //playerListNameにクラス名をセット
        player.setPlayerListName(ChatColor.AQUA + "[" +battleClass.getName() + "]" + ChatColor.RESET + player.getDisplayName());

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
        //スナイパーには矢もあげようね
        if (battleClass.equals(BattleClass.SNIPER)) {
            player.getInventory().addItem(new ItemStack(Material.ARROW));
        }

        //ロードアウトセレクター回収する
        getPlayer().getInventory().remove(Material.EMERALD);
    }

    public void removeBattleClass() {
        //プレイヤーリスト名を元に戻す
        player.setPlayerListName(player.getDisplayName());

        //ステータスを元に戻す
        player.setHealthScale(DEFAULT_HEALTH);
        player.setWalkSpeed(DEFAULT_SPEED);
        //攻撃と防御を空に
        attack = 0;
        defence = 0;

        //装備を回収
        player.getInventory().clear();

    }
    public boolean isBattleClass() {
        //バトルクラスを持っているか？
        if (!player.getPlayerListName().equalsIgnoreCase(player.getDisplayName())) {
            return true;
        }
        return false;
    }

    public boolean isBattleClass(BattleClass battleClass) {
        //指定されたバトルクラスになっているか？
        if (this.battleClass.equals(battleClass)) {
            return true;
        }
        return false;
    }

    public boolean hasBeItem(BeItems beItem) {
        //指定されたアイテムを持っているか？
        if (beItem.toItemStack().equals(player.getInventory().getItemInMainHand())) {
            return true;
        }
        return false;
    }

    public void setAbilityTime(int abilitytime, int cd) {
        //能力時間の後にクールダウン開始するとき
        ability = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //クールダウン開始
                setCooldown(cd);
                //能力時間終了
                ability = false;
                timer.cancel();
            }
        }, abilitytime*1000);
    }

    public void setCooldown(int count) {
        //いきなりクールダウン開始するとき
        cooldown = count;
        cdTimer = new Timer();
        cdTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (cooldown > 0) {
                    //残りクールダウンを表示
                    player.sendTitle("", "能力使用可能まで" + ChatColor.RED + cooldown + ChatColor.RESET +"秒", 0, 20, 0);
                } else {
                    //クールダウン終わり
                    cdTimer.cancel();
                }
                cooldown--;
            }
        },0,1000);
    }

    public void stopCooldown() {
        //クールダウン強制終了
        if (cdTimer != null) {
            cdTimer.cancel();
        }
    }

    public boolean isCooldown() {
        //クールダウン中ですかぁ?
        if (cooldown >= 0) { return true; }
        return false;
    }

    //げったーせったー
    public Player getPlayer() {
        return player;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getDefence() {
        return defence;
    }

    public void setDefence(double defence) {
        this.defence = defence;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isAbilityFlag() {
        return ability;
    }

    public void setAbilityFlag(boolean abilityFlag) {
        //時間無制限に能力が使用できるとき
        this.ability = abilityFlag;
    }

    //ぷらいべーとなめそっど
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
