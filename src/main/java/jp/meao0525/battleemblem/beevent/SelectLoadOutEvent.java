package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.battleclass.BattleClass;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import static jp.meao0525.battleemblem.battleclass.BattleClassName.*;
import static jp.meao0525.battleemblem.beevent.OpenSelectorEvent.BATTLE_CLASS_INV_NAME;

public class SelectLoadOutEvent implements Listener {
    @EventHandler
    public void SelectLoadOutEvent(InventoryClickEvent e) {
        //ロードアウトセレクターのインベントリか?
        String invName = e.getView().getTitle();
        if (!invName.equalsIgnoreCase(BATTLE_CLASS_INV_NAME)) {
            return;
        }
        //クリックしたのはPlayerか?
        if (!(e.getWhoClicked() instanceof Player)) { return; }

        Player player = (Player) e.getWhoClicked();
        BePlayer bePlayer = new BePlayer(player);

        if (e.getCurrentItem() == null) { return; } //空欄のクリック時
        String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
        //何クリックしたのー?
        switch (itemName) {
            case "剣聖":
                bePlayer.setBattleClass(BattleClass.SWORD_MASTER);
                break;
            case "狂戦士":
                bePlayer.setBattleClass(BattleClass.BERSERKER);
                break;
            case "重鎧兵":
                bePlayer.setBattleClass(BattleClass.ARMOR_KNIGHT);
                break;
            case "勇者":
                bePlayer.setBattleClass(BattleClass.BRAVE_HERO);
                break;
            case "狙撃手":
                bePlayer.setBattleClass(BattleClass.SNIPER);
                break;
            case "暗殺者":
                bePlayer.setBattleClass(BattleClass.ASSASSIN);
                break;
            default:
                break;
        }

        //イベントキャンセル
        e.setCancelled(true);
    }
}
