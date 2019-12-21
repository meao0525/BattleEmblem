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
        //フェーズが0ならこの処理はいらないよね
        if (game.getPhase() == 0) { return; }

        Player player = e.getPlayer();
        //BeGameからBePlayerのインスタンスを取得
        BePlayer bePlayer = game.getBePlayer(player);
        //ログアウトしたのがBePlayerじゃない
        if (bePlayer == null) { return; }

        //バトルクラスをremoveする
        bePlayer.removeBattleClass();
        //プレイヤーリストを減らす
        game.getBePlayerList().remove(bePlayer);

        //残り人数が一人以下ならゲーム終了
        if (game.getBePlayerList().size() <= 1) {
            game.End();
        }
    }
}
