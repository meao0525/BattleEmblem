package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.battleclass.BattleClass;
import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beitem.BeItems;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OpenSelectorEvent implements Listener {
    public static final String BATTLE_CLASS_INV_NAME = "バトルクラス";

    @EventHandler
    public void openSelectorEvent(PlayerInteractEvent e) {
        //みぎくりっく以外
        if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR))
                && !(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }

        Player player = e.getPlayer();
        BePlayer bePlayer = new BePlayer(player);

        //おぬしが持っておるのはもしやロードアウトセレクターか？
        if (!bePlayer.hasBeItem(BeItems.LOADOUT_SELECTOR)) { return; }

        //ゲーム中じゃダメだよ
        if (BeGame.getPhase() != 0) {
            player.sendMessage(ChatColor.GRAY + "ゲーム中は使用できません。大人しく観戦してください");
            return;
        }

        player.openInventory(getClassInventory());
    }

    public Inventory getClassInventory() {
        Inventory inv = Bukkit.createInventory(null,9, BATTLE_CLASS_INV_NAME);

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

        item.setItemMeta(meta);
        return item;
    }
}
