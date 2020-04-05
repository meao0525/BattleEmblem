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
            bePlayer.death();
        }
    }
}
