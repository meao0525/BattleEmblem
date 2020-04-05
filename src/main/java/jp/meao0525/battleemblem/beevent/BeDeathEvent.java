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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import static jp.meao0525.battleemblem.begame.BeLocation.coliseum;

public class BeDeathEvent implements Listener {

    public BeDeathEvent() { }

//    @EventHandler
//    public void DeathEvent(EntityDamageEvent e) {
//        //ロビー中
//        if (BeGame.getPhase() == 0) { return; }
//
//        Player player = e.getEntity();
//        BePlayer bePlayer = BePlayerList.getBePlayer(player);
//        //ゲーム参加者じゃない
//        if (bePlayer == null) { return; }
//
//        //キープインベントリ
//        e.setKeepInventory(true);
//        //HP全回復
//        player.setHealth(player.getHealthScale());
//
//        //残機はなんぼ?
//        if (bePlayer.getLife() != 0) {
//            //ライフを1減らす
//            bePlayer.setLife(bePlayer.getLife() - 1);
//            //初期位置にTP
//            player.teleport(coliseum);
//            //TODO: ゲームモードをアドベンチャーにする(できてない)
//            player.setGameMode(GameMode.ADVENTURE);
//            //残機を教えてあげて
//            player.sendMessage(ChatColor.GOLD + "[BattleEmblem]"
//                    + ChatColor.RESET + "残機は"
//                    + ChatColor.AQUA + bePlayer.getLife()
//                    + ChatColor.RESET + "です");
//        } else {
//            //バトルクラスを外す
//            bePlayer.removeBattleClass();
//            //プレイヤーリストから外す
//            BePlayerList.getBePlayerList().remove(bePlayer);
//            //デスメッセージ
//            e.setDeathMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET +player.getPlayerListName() + " が脱落しました");
//
//            //残り人数が一人以下ならゲーム終了
//            if (BePlayerList.getBePlayerList().size() <= 1) {
//                BeGame.End();
//            }
//        }
//    }
}
