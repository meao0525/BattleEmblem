package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.BattleEmblemMain;
import jp.meao0525.battleemblem.battleclass.BattleClass;
import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import org.bukkit.ChatColor;
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

    //こんすとあｒくた
    public SelectLoadOutEvent() { }

    @EventHandler
    public void SelectEvent(InventoryClickEvent e) {
        //空欄のクリック時
        if (e.getCurrentItem() == null) { return; }

        //ロードアウトセレクターのインベントリか?
        String invName = e.getView().getTitle();
        if (!invName.equalsIgnoreCase(BATTLE_CLASS_INV_NAME)) {
            return;
        }
        //クリックしたのはPlayerか?
        if (!(e.getWhoClicked() instanceof Player)) { return; }

        Player player = (Player) e.getWhoClicked();
        //とりあえずのnewBePlayer
        BePlayer bePlayer = new BePlayer(player);

        //バトルクラスを持っている人はダメよ
        if (bePlayer.isBattleClass()) { return; }

        String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
        BattleClass battleClass = null;
        //何クリックしたのー?
        switch (itemName) {
            case "剣聖":
                battleClass = BattleClass.SWORD_MASTER;
                break;
            case "狂戦士":
                battleClass = BattleClass.BERSERKER;
                break;
            case "重鎧兵":
                 battleClass = BattleClass.ARMOR_KNIGHT;
                break;
            case "勇者":
                battleClass = BattleClass.BRAVE_HERO;
                break;
            case "狙撃手":
                battleClass = BattleClass.SNIPER;
                break;
            case "暗殺者":
                battleClass = BattleClass.ASSASSIN;
                break;
            default:
                break;
        }
        if (battleClass == null) { return; }

        //イベントキャンセル
        e.setCancelled(true);

        //使われてないといいね
        if (battleClass.isUsed()) {
            player.sendMessage(battleClass.getName() + ChatColor.DARK_RED + "は使用中です");
        } else {
            bePlayer.setBattleClass(battleClass);
            player.closeInventory();
        }
    }
}
