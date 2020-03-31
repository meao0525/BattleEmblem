package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beitem.BeItemName;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

import static jp.meao0525.battleemblem.beitem.BeItemName.*;

public class BeAbilityEvent implements Listener {

    private Plugin plugin;
    private static HashMap<Player,CoolDownThread> cooldownPlayers = new HashMap<>();

    public BeAbilityEvent(Plugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        //ゲーム中しか使えない
        if (BeGame.getPhase() != 2) { return; }

        Player player = e.getPlayer();
        //みぎくりっく以外
        if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR))
                && !(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }

        //手に持ってるアイテムは?
        ItemStack item = player.getInventory().getItemInMainHand();
        //何も持ってない
        if (item == null) { return; }

        BePlayer bePlayer = BePlayerList.getBePlayer(player);
        //ゲームプレイヤーじゃないやんけぇ
        if (bePlayer == null) { return; }
        //アビリティ使用できない
        if (cooldownPlayers.containsKey(player)) {
            player.sendMessage(ChatColor.GRAY + "アビリティは現在使用できません");
            return;
        }

        //能力発動!!!
        activateAbility(bePlayer,item);
    }

    //アビリティ内容
    public void activateAbility(BePlayer bePlayer, ItemStack item) {
        //timer用変数
        int cd = 0;
        int at = 0;
        //アイテム名取得
        String iName = item.getItemMeta().getDisplayName();
        //名前で判定
        switch(iName) {
            case MASTER_SWORD_NAME:
                /* ==剣聖アビリティ==
                 * 5秒間スピードを1段階あげる(CD:15s)
                 */
                cd = 15;
                at= 5;
                bePlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100,1));
                break;
            case BERSERKER_AXE_NAME:
                /* ==狂戦士アビリティ==
                 * 殴った人を2秒スタンorめっちゃノックバック(CD:30s)
                 */
                cd = 30;
                break;
            case KNIGHT_AXE_NAME:
                /* ==重鎧兵アビリティ==
                 * 半径5メートル以内の敵の動きを5秒間止める(CD:30s)
                 */
                cd = 30;
                break;
            case BRAVE_SWORD_NAME:
                /* ==勇者アビリティ==
                 * 被ダメージの50%回復(CD:30s)
                 */
                cd = 30;
                break;
            case SNIPER_BOW_NAME:
                /* ==狙撃手アビリティ==
                 * なんか特殊な矢でも渡します？
                 */
                cd = 10;
                break;
            case ASSASSIN_DAGGER_NAME:
                /* ==暗殺者アビリティ==
                 * 10秒間透明化する
                 * 攻撃をすると透明化が解除される(CD:10s)
                 */
                cd = 10;
                break;
        }
        //能力時間、クールダウンを設定
        CoolDownThread thread = new CoolDownThread(bePlayer, at, cd);
        cooldownPlayers.put(bePlayer.getPlayer(), thread);
        //タイマースタート
        thread.startCount();
    }

    private class CoolDownThread {

        Player player;
        BePlayer bePlayer;
        //能力時間とクールダウンの合計
        int totalTime;
        //クールダウン
        int cooldown;

        private CoolDownThread(BePlayer beplayer, int abilityTime, int cooldown) {
            this.bePlayer = beplayer;
            this.player = beplayer.getPlayer();
            this.cooldown = cooldown;
            this.totalTime = cooldown + abilityTime;
        }

        private void startCount() {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (cooldownPlayers.containsKey(player)) {
                        if (totalTime > cooldown) {
                            //能力の使用中
                        } else if (totalTime > 0) {
                            //残りクールダウンを表示
                            player.sendTitle("", "能力使用可能まで" + ChatColor.RED + cooldown + ChatColor.RESET +"秒", 0, 20, 0);
                            cooldown--;
                        } else {
                            //クールダウン終わり
                            cooldownPlayers.remove(player);
                            cancel();
                        }
                    }
                }
            }.runTaskTimer(plugin,0,20);
        }
    }
}
