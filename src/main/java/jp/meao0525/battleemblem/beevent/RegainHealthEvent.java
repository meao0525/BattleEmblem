package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.BattleEmblemMain;
import jp.meao0525.battleemblem.battleclass.BattleClass;
import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;


import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class RegainHealthEvent implements Listener {

    private Plugin plugin;
    public static HashMap<Player,HealingThread> healingPlayers = new HashMap<>();

    public RegainHealthEvent(Plugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void PlayerSneakEvent(PlayerToggleSneakEvent e) {
        //ロビー中
        if (BeGame.getPhase() == 0) { return; }

        //プレイヤー取得
        Player player = e.getPlayer();
        //アドベンチャー以外のゲームモード
        if (!player.getGameMode().equals(GameMode.ADVENTURE)) { return; }
        //すでに体力満タン
        if (player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue()) { return; }

        if (!e.getPlayer().isSneaking()) {
            //スニーク時
            player.sendMessage(ChatColor.YELLOW + "回復中...");

            if (!healingPlayers.containsKey(player)) {
                //新しいスレッド用インスタンス
                HealingThread thread = new HealingThread(player);
                //healingPlayerに登録
                healingPlayers.put(player,thread);
            }
            return;

        } else {
            //スニーク解除時
            if (healingPlayers.containsKey(e.getPlayer())) {
                e.getPlayer().sendMessage(ChatColor.GRAY + "回復を中断しました");
                healingPlayers.remove(e.getPlayer());
            }
            return;
        }
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        //ゲーム中じゃなきゃ関係ないね
        if (BeGame.getPhase() == 0) { return; }
        //すでに体力満タン
        if (e.getPlayer().getHealth() == e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue()) { return; }

        //座標の取得(ブロック単位でやらないとシビアすぎる)
        Location from = e.getFrom().getBlock().getLocation();
        Location to = e.getTo().getBlock().getLocation();
        //移動したブロック数0
        if (from.equals(to)) { return; }

        //動いたらhealingリストから削除して回復できなくしてやる、フハハ
        if (healingPlayers.containsKey(e.getPlayer())) {
            e.getPlayer().sendMessage(ChatColor.GRAY + "回復を中断しました");
            healingPlayers.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void PlayerLogoutEvent(PlayerQuitEvent e) {
        //回復中のまま抜けるなんてやめてほしいよね
        if (healingPlayers.containsKey(e.getPlayer())) {
            healingPlayers.remove(e.getPlayer());
        }
    }

    //Thread用の内部クラス
    private class HealingThread {
        private Player player;
        private int count = 0;
        private int period = 0; //連打対策用

        private HealingThread(Player player) {
            this.player = player;
            countHealStart();
        }

        public void countHealStart() {
            //bePlayer取得
            BePlayer bePlayer = BePlayerList.getBePlayer(player);
            if (bePlayer == null) { return; }

            Timer timer = new Timer();
            //5秒止まると1ずつHPが回復
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    //healingリストにいない
                    if (!healingPlayers.containsKey(player)) { this.cancel(); }
                    //連打対策に1/1000秒ごとに判定してやるぜ
                    if (period % 1000 == 0) {
                        //3秒以上経った
                        if (count >= 3) {
                            double amount;
                            //HP1回復
                            if (bePlayer.isBattleClass(BattleClass.ARMOR_KNIGHT)) {
                                //重鎧兵はHPが通常の3倍で回復量2倍
                                amount = 2.0 / 3.0;
                            } else {
                                //他はHPが通常の2倍
                                amount = 1.0 / 2.0;
                            }
                            //効果音
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,5.0F,5.0F);

                            double hp = player.getHealth();
                            double max = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue();
                            if (max - hp > amount) {
                                //上限超えない
                                player.setHealth(player.getHealth() + amount);
                            } else {
                                //上限超えちゃう
                                player.setHealth(max);
                                player.sendMessage(ChatColor.YELLOW + "回復が終了しました");
                                this.cancel();
                            }

                        } else {
                            //経過秒数を1増やす
                            count++;
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE,5.0F,5.0F);
                        }
                    }
                    period++;
                }
            }, 0, 1);
        }
    }
}
