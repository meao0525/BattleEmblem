//package jp.meao0525.battleemblem.beevent;
//
//import jp.meao0525.battleemblem.begame.BeGame;
//import jp.meao0525.battleemblem.beitem.BeItems;
//import jp.meao0525.battleemblem.beplayer.BePlayer;
//import jp.meao0525.battleemblem.beplayer.BePlayerList;
//import org.bukkit.ChatColor;
//import org.bukkit.Material;
//import org.bukkit.Sound;
//import org.bukkit.entity.Item;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.block.Action;
//import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.potion.PotionEffect;
//import org.bukkit.potion.PotionEffectType;
//
//import static jp.meao0525.battleemblem.beitem.BeItemName.COUNTER_ARMOR_NAME;
//import static jp.meao0525.battleemblem.beitem.BeItemName.INVINCIBLE_ARMOR_NAME;
//
//public class BeUltimateEvent implements Listener {
//
//    public BeUltimateEvent() {}
//
//    @EventHandler
//    public void PlayerInteractEvent(PlayerInteractEvent e) {
//        //ゲーム中しか使えない
//        if (BeGame.getPhase() != 2) { return; }
//
//        Player player = e.getPlayer();
//        //みぎくりっく以外
//        if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR))
//                && !(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
//            return;
//        }
//
//        //手に持ってるアイテムは?
//        ItemStack item = player.getInventory().getItemInMainHand();
//        //何も持ってない
//        if (item.getType().equals(Material.AIR)) { return; }
//
//        BePlayer bePlayer = BePlayerList.getBePlayer(player);
//        //ゲームプレイヤーじゃないやんけぇ
//        if (bePlayer == null) { return; }
//        //ウルトアイテムを持ってるときのみ
//        for (ItemStack i : BeItems.getUltItem()) {
//            if (item.equals(i)) {
//                //ウルト発動!
//                activateUltimate(bePlayer, item);
//                //アイテム消す
//                player.getInventory().remove(item);
//            }
//        }
//    }
//
//
//}
