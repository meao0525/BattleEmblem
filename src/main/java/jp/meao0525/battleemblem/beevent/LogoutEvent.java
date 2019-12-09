package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beplayer.BePlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class LogoutEvent implements Listener {
    private BeGame game;

    public LogoutEvent(BeGame game) {
        this.game = game;
    }

    @EventHandler
    public void PlayerLogoutEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        BePlayer bePlayer = new BePlayer(player);
        //バトルクラスをremoveして持ち物も空にする
        bePlayer.removeBattleClass();
        player.getInventory().clear();
        //プレイヤーリストを減らす
        game.getBePlayerList().remove(player);

        //TODO: 残り1人の処理はここでするのか？

    }
}
