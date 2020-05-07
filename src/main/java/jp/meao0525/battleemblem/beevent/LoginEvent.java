package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.beitem.BeItems;
import jp.meao0525.battleemblem.beitem.BeRuleBook;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static jp.meao0525.battleemblem.begame.BeLocation.lobby;

public class LoginEvent implements Listener {
    @EventHandler
    public void LoginEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        //アドベンチャーモードの人はインベントリを空にする
        if (!player.getGameMode().equals(GameMode.CREATIVE)) { player.getInventory().clear(); }
        //ロードアウトセレクターを渡す
        player.getInventory().addItem(BeItems.LOADOUT_SELECTOR.toItemStack());
        //ルールブックを渡す
        player.getInventory().addItem(new BeRuleBook().toItemStack());
        //ロビーに飛ばす
        player.teleport(lobby);
    }
}
