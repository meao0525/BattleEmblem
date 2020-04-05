package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beitem.BeItems;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.*;
import static org.bukkit.Material.DIAMOND_AXE;

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
        ItemStack item = e.getCurrentItem();
        if (item == null) { return; }

        if ((item.equals(BeItems.BRAVE_BOOTS.toItemStack()))
        ||(item.equals(BeItems.BRAVE_CHESTPLATE.toItemStack()))
        ||(item.equals(BeItems.BRAVE_HELMET.toItemStack()))
        ||(item.equals(BeItems.BRAVE_LEGGINGS.toItemStack()))
        ||(item.equals(BeItems.KNIGHT_BOOTS.toItemStack()))
        ||(item.equals(BeItems.KNIGHT_CHESTPLATE.toItemStack()))
        ||(item.equals(BeItems.KNIGHT_HELMET.toItemStack()))
        ||(item.equals(BeItems.KNIGHT_LEGGINGS.toItemStack()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void CantTradeEvent(PlayerInteractEntityEvent e) {
        //商人とトレードできない
        if (e.getRightClicked() instanceof WanderingTrader) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void LeftClickCoolDown(PlayerInteractEvent e) {
        //剣、弓、斧を持って左クリックするとクールダウン
        if ((e.getAction().equals(Action.LEFT_CLICK_AIR)) || (e.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
            Player player = e.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            //クールダウン中ならキャンセル
            if (player.getCooldown(item.getType()) > 0) {
                e.setCancelled(true);
            }

            switch (item.getType()) {
                case DIAMOND_AXE:
                case IRON_AXE:
                case GOLDEN_AXE:
                case STONE_AXE:
                    player.setCooldown(item.getType(),20);
                    break;
                case DIAMOND_SWORD:
                case IRON_SWORD:
                case GOLDEN_SWORD:
                case STONE_SWORD:
                case BOW:
                    player.setCooldown(item.getType(),10);
                    break;
            }
        }
    }

    @EventHandler
    public void DeathEvent(EntityDamageEvent e) {
        //死んだらすぐリスポ(デスカウントは増やさない)
        if (!(e.getEntity() instanceof Player)) { return; }
        Player player = (Player) e.getEntity();

        if (e.getDamage() >= player.getHealth()) {
            if ((BeGame.getPhase() != 2) || (BePlayerList.getBePlayer(player) == null)) {
                e.setCancelled(true);
                player.setGameMode(GameMode.SPECTATOR);
            }
        }
    }
}
