package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.battleclass.BattleClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClassSelectEvent implements Listener {
    public static String BATTLE_CLASS_INV_NAME = "バトルクラス";

    @EventHandler
    public void ClassSelectEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        //おぬしが持っておるのはもしやロードアウトセレクターか？
        if (!(item.getType().equals(Material.EMERALD))
                ||!(item.getItemMeta().getDisplayName().equalsIgnoreCase("ロードアウトセレクター"))) {
            return;
        }

        player.openInventory(getClassInventory());
    }

    public Inventory getClassInventory() {
        Inventory inv = Bukkit.createInventory(null,9, "バトルクラス");

        inv.setItem(1,getIcon(BattleClass.SWORD_MASTER));
        inv.setItem(2,getIcon(BattleClass.BERSERKER));
        inv.setItem(3,getIcon(BattleClass.ARMOR_KNIGHT));
        inv.setItem(4,getIcon(BattleClass.BRAVE_HERO));
        inv.setItem(5,getIcon(BattleClass.SNIPER));
        inv.setItem(6,getIcon(BattleClass.ASSASSIN));

        return inv;
    }

    //Inventoryに並べる用のアイコン取得
    public ItemStack getIcon(BattleClass battleClass) {
        ItemStack item = new ItemStack(battleClass.getIcon(), 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(battleClass.getName());
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS,
                ItemFlag.HIDE_UNBREAKABLE);

        return item;
    }
}
