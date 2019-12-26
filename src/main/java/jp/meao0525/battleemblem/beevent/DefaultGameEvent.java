package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.beitem.BeItems;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;

public class DefaultGameEvent implements Listener {
    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent e) {
        //空腹ゲージが変わらない
        e.setCancelled(true);
    }

    @EventHandler
    public void FallDamageEvent(EntityDamageEvent e) {
        //落下ダメージを食らわない
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void DropItemEvent(PlayerDropItemEvent e) {
        //アイテムを捨てられない
        if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void PickUpEvent(EntityPickupItemEvent e) {
        //アイテムを拾えない
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void PickUpArrowEvent(PlayerPickupArrowEvent e) {
        //矢を拾えない
        e.setCancelled(true);
    }

    @EventHandler
    public void RegainHealthBySatiatedEvent(EntityRegainHealthEvent e) {
        //満腹による回復をキャンセル
        if (e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void CantTakeOffEvent(InventoryClickEvent e) {
        //装備を脱げないよ
        //TODO: できてない
        ItemStack item = e.getCurrentItem();
        if (item == null) { return; }

        if ((item.equals(BeItems.BRAVE_BOOTS))
        ||(item.equals(BeItems.BRAVE_CHESTPLATE))
        ||(item.equals(BeItems.BRAVE_HELMET))
        ||(item.equals(BeItems.BRAVE_LEGGINGS))
        ||(item.equals(BeItems.KNIGHT_BOOTS))
        ||(item.equals(BeItems.KNIGHT_CHESTPLATE))
        ||(item.equals(BeItems.KNIGHT_HELMET))
        ||(item.equals(BeItems.KNIGHT_LEGGINGS))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void cantTradeEvent(PlayerInteractEntityEvent e) {
        //商人とトレードできない
        if (e.getRightClicked() instanceof Villager) {
            e.setCancelled(true);
        }
    }

    //TODO: 村人を殴れない処理はいりますか?
}
