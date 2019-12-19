package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.beitem.BeItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginEvent implements Listener {
    @EventHandler
    public void LoginEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        //headerをnullにならないように空欄にする
        player.setPlayerListHeader("");
        //ロードアウトセレクターを渡す
        player.getInventory().addItem(BeItems.LOADOUT_SELECTOR.toItemStack());
        //TODO: 初期リスに飛ばす
    }
}