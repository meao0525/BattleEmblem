package jp.meao0525.battleemblem.beevent;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ClassSelectEvent implements Listener {
    @EventHandler
    public void ClassSelectEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        //おぬしが持っておるのはもしやロードアウトセレクターか？
        if (!(item.getType().equals(Material.EMERALD))
                ||!(item.getItemMeta().getDisplayName().equals("ロードアウトセレクター"))) {
            return;
        }


    }
}
