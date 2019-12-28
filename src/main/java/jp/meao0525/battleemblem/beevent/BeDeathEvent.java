package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.BattleEmblemMain;
import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BeDeathEvent implements Listener {

    private BeGame game;

    public BeDeathEvent(BattleEmblemMain main) { this.game = main.getGame(); }

    @EventHandler
    public void DeathEvent(PlayerDeathEvent e) {
        //ゲーム中じゃない
        if (game.getPhase() == 0) { return; }

        Player player = e.getEntity();
        BePlayer bePlayer = BePlayerList.getBePlayer(player);
        //ゲーム参加者じゃない
        if (bePlayer == null) { return; }

        //キープインベントリ
        e.setKeepInventory(true);
        //HP全回復
        player.setHealth(player.getHealthScale());

        //残機はなんぼ?
        if (bePlayer.getLife() != 0) {
            //ライフを1減らす
            bePlayer.setLife(bePlayer.getLife() - 1);
            //TODO: 初期位置にTP
            //残機を教えてあげて
            player.sendMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "残機は" + bePlayer.getLife() + "です");
        } else {
            //バトルクラスを外す
            bePlayer.removeBattleClass();
            //プレイヤーリストから外す
            BePlayerList.getBePlayerList().remove(bePlayer);
            //観戦者にする
            player.setGameMode(GameMode.SPECTATOR);
            //デスメッセージ
            e.setDeathMessage(ChatColor.GOLD + "[BattleEmblem]" + player.getPlayerListName() + " が脱落しました");
        }
    }
}
