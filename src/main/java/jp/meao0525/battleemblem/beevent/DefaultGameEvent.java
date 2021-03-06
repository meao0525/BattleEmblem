package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beitem.BeItems;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
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
    public void ArrowHitEvent(ProjectileHitEvent e) {
        //矢が当たったら消える
        if (e.getHitBlock() != null) {
            e.getEntity().remove();
        }
    }

    @EventHandler
    public void CantTakeOffEvent(InventoryClickEvent e) {
        //オペレーターはなにしてもいい
        if (e.getWhoClicked().isOp()) { return; }
        //装備を脱げないよ
        ItemStack item = e.getCurrentItem();
        if (item == null) { return; }

        if (e.getSlotType().equals(InventoryType.SlotType.ARMOR) || !e.getInventory().getType().equals(InventoryType.PLAYER))  {
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
    public void ShiftToOffhand(PlayerSwapHandItemsEvent e) {
        //BeItemはオフハンドに持ち替えれないよ
        ItemStack item = e.getOffHandItem();
        for (BeItems bi : BeItems.values()) {
            //ゲーム内アイテムだったら持ち替えさせない
            if (bi.toItemStack().equals(item)) { e.setCancelled(true); }
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

            BePlayer bp = BePlayerList.getBePlayer(player);
            if (bp != null) {
                //持ち物にクールダウンをセット
                bp.setIndicator(item);
            }
        }
    }

    @EventHandler
    public void DeathEvent(EntityDamageEvent e) {
        //プレイヤー攻撃・落下ダメージは何も起きない
        if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)
                || e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)
                || e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            e.setCancelled(true);
            return;
        }

        //死んだらすぐリスポ(デスカウントは増やさない)
        if (!(e.getEntity() instanceof Player)) { return; }
        Player player = (Player) e.getEntity();
        //ゲーム中じゃないときやゲームプレイヤー以外はダメージ受けない(エフェクトだけ)
        if ((BeGame.getPhase() != 2) || (BePlayerList.getBePlayer(player) == null)) {
            e.setCancelled(true);
            player.playEffect(EntityEffect.HURT);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 4.0F, 4.0F);
            return;
        }

        //ダメージが残りHPを超えてる
        if (e.getDamage() >= player.getHealth()) {
            //ゲーム中プレイヤーならデス処理
            BePlayer bePlayer = BePlayerList.getBePlayer(player);
            bePlayer.death();
        }
    }

    @EventHandler
    public void dontDeathCountEvent(PlayerDeathEvent e) {
        //事故死してもデスカウント増やさない
        e.getEntity().setStatistic(Statistic.DEATHS, e.getEntity().getStatistic(Statistic.DEATHS) - 1);
    }
}
