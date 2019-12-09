package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.beplayer.BePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class LogoutEvent implements Listener {
    private Plugin plugin;

    public LogoutEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerLogoutEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        BePlayer bePlayer = new BePlayer(player);
        //バトルクラスをremoveして持ち物も空にする
        bePlayer.removeBattleClass();
        player.getInventory().clear();

        //TODO: プレイヤーリストを減らす処理はどうしようか
    }
}
