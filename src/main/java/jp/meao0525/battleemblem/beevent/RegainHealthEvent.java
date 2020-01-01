package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.BattleEmblemMain;
import jp.meao0525.battleemblem.begame.BeGame;
import org.bukkit.Bukkit;
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

import java.util.ArrayList;
import java.util.HashMap;

public class RegainHealthEvent implements Listener {

    private Plugin plugin;
    private static HashMap<Player,Integer> healingPlayer = new HashMap<Player, Integer>();

    public RegainHealthEvent(Plugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void PlayerStopEvent(PlayerToggleSneakEvent e) {
        //ゲーム中じゃない
        if (BeGame.getPhase() == 0) { return; }

        //5秒止まると1ずつHPが回復
        e.getPlayer().sendMessage("できてる?");
        healingPlayer.put(e.getPlayer(),0);
        countHealStart(e.getPlayer());
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

    public void countHealStart(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                //healingリストにいないか、HPが満タン
                if ((!healingPlayer.containsKey(player) ) || (player.getHealth() == player.getHealthScale())) {
                    cancel();
                    return;
                }
                //5秒以上経った
                if (healingPlayer.get(player) >= 5) {
                    //HP1回復
                    player.setHealth(player.getHealth() + 1.0);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,5.0F,5.0F);
                }
                //経過秒数を1増やす
                healingPlayer.put(player, healingPlayer.get(player) + 1);
            }
        }.runTaskTimer(plugin,0,20);
    }
}
