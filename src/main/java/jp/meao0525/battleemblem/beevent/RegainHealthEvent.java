package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.BattleEmblemMain;
import jp.meao0525.battleemblem.battleclass.BattleClass;
import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RegainHealthEvent implements Listener {

    private Plugin plugin;
    private static HashMap<Player,HealingThread> healingPlayer = new HashMap<>();

    public RegainHealthEvent(Plugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void PlayerSneakEvent(PlayerToggleSneakEvent e) {
        //ロビー中
        if (BeGame.getPhase() == 0) { return; }

        if (!e.getPlayer().isSneaking()) {
            //スニーク時
            Player player = e.getPlayer();
            player.sendMessage(ChatColor.YELLOW + "回復中...");

            //新しいスレッド用インスタンス
            HealingThread thread = new HealingThread(player);
            //healingPlayerに登録
            healingPlayer.put(player,thread);
            return;

        } else {
            //スニーク解除時
            if (healingPlayer.containsKey(e.getPlayer())) {
                e.getPlayer().sendMessage(ChatColor.GRAY + "回復を中断しました");
                healingPlayer.remove(e.getPlayer());
            }
            return;
        }
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        //ゲーム中じゃなきゃ関係ないね
        if (BeGame.getPhase() == 0) { return; }

        //移動距離0
        if (e.getFrom().equals(e.getTo())) {
            return;
        }

        //動いたらhealingリストから削除して回復できなくしてやる、フハハ
        if (healingPlayer.containsKey(e.getPlayer())) {
            e.getPlayer().sendMessage(ChatColor.GRAY + "回復を中断しました");
            healingPlayer.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void PlayerLogoutEvent(PlayerQuitEvent e) {
        //回復中のまま抜けるなんてやめてほしいよね
        if (healingPlayer.containsKey(e.getPlayer())) {
            healingPlayer.remove(e.getPlayer());
        }
    }

    //Thread用の内部クラス
    private class HealingThread {
        private Player player;
        private int count = 0;

        private HealingThread(Player player) {
            this.player = player;
            countHealStart();
        }

        public void countHealStart() {
            //bePlayer取得
            BePlayer bePlayer = BePlayerList.getBePlayer(player);

            //5秒止まると1ずつHPが回復
            new BukkitRunnable() {
                @Override
                public void run() {
                    //healingリストにいないかHPが満タン
                    if ((!healingPlayer.containsKey(player) ) || (player.getHealth() == player.getHealthScale())) {
                        this.cancel();
                    }

                    //5秒以上経った
                    if (count >= 5) {
                        //HP1回復
                        player.setHealth(player.getHealth() + 1.0); //TODO: ここでエラー
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,5.0F,5.0F);
                    } else {
                        //経過秒数を1増やす
                        count++;
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,5.0F,5.0F);
                    }
                }
            }.runTaskTimer(plugin,0,20);
        }
    }

//    public void countHealStart(Player player) {
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                //healingリストにいないか、HPが満タン
//                if ((!healingPlayer.containsKey(player) ) || (player.getHealth() == player.getHealthScale())) {
//                    cancel();
//                    return;
//                }
//                //5秒以上経った
//                if (healingPlayer.get(player) >= 5) {
//                    //HP1回復
//                    player.setHealth(player.getHealth() + 1.0);
//                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,5.0F,5.0F);
//                }
//                //経過秒数を1増やす
//                healingPlayer.put(player, healingPlayer.get(player) + 1);
//            }
//        }.runTaskTimer(plugin,0,20);
//    }
}
