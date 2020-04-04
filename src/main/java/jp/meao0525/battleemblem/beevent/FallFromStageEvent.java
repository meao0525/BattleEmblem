package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import static jp.meao0525.battleemblem.begame.BeLocation.coliseum;

public class FallFromStageEvent implements Listener {

    @EventHandler
    public void PlayerSwimEvent(PlayerMoveEvent e) {
        //ゲーム中に参加者が水に落ちると死ぬ
        Player player = e.getPlayer();
        BePlayer bePlayer = BePlayerList.getBePlayer(player);
        if ((BeGame.getPhase() == 0) || (bePlayer == null)) {
            return;
        }

        World world = player.getWorld();
        //プレイヤーの現在地のブロックを取得
        Block block = world.getBlockAt(player.getLocation());
        //現在地が水
        if (block.isLiquid()) {
            //残機はなんぼ?
            if (bePlayer.getLife() > 0) {
                //ライフを1減らす
                bePlayer.setLife(bePlayer.getLife() - 1);
                //初期位置にTP
                player.teleport(coliseum);
                //HPを回復
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                //残機を教えてあげて
                player.sendMessage(ChatColor.GOLD + "[BattleEmblem]"
                        + ChatColor.RESET + "残機は"
                        + ChatColor.AQUA + bePlayer.getLife()
                        + ChatColor.RESET + "です");
            } else {
                //バトルクラスを外す
                bePlayer.removeBattleClass();
                //プレイヤーリストから外す
                BePlayerList.getBePlayerList().remove(bePlayer);
                //観戦者にする
                player.setGameMode(GameMode.SPECTATOR);
                //初期位置にTP
                player.teleport(coliseum);
                //デスメッセージ
                Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + player.getPlayerListName() + " が脱落しました");

                //残り人数が一人以下ならゲーム終了
                if (BePlayerList.getBePlayerList().size() <= 1) {
                    BeGame.End();
                }
            }
        }
    }
}
