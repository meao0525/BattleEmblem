package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beitem.BeItemName;
import jp.meao0525.battleemblem.beitem.BeItems;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

import static jp.meao0525.battleemblem.beitem.BeItemName.*;

public class BeAbilityEvent implements Listener {

    private Plugin plugin;

    public BeAbilityEvent(Plugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        //ゲーム中しか使えない
        if (BeGame.getPhase() != 2) { return; }

        Player player = e.getPlayer();
        //みぎくりっく以外
        if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR))
                && !(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }

        //手に持ってるアイテムは?
        ItemStack item = player.getInventory().getItemInMainHand();
        //何も持ってない
        if (item.getType().equals(Material.AIR)) { return; }

        BePlayer bePlayer = BePlayerList.getBePlayer(player);
        //ゲームプレイヤーじゃないやんけぇ
        if (bePlayer == null) { return; }

        //持ってるのがウルトアイテム
        for (ItemStack i : BeItems.getUltItem()) {
            if (item.equals(i) && !(item.getItemMeta().getDisplayName().equalsIgnoreCase(LIGHTNING_BOW_NAME))) {
                activateUltimate(bePlayer, item);
                return;
            }
        }

        //アビリティ使用できない
        if (bePlayer.isAbilityFlag()) {
            player.sendMessage(ChatColor.GRAY + "能力を使用中です");
            return;
        } else if (bePlayer.isCooldown()) {
            player.sendMessage(ChatColor.GRAY + "クールダウン中です");
            return;
        }

        //能力発動!!!
        activateAbility(bePlayer,item);
    }

    //アビリティ内容
    public void activateAbility(BePlayer bePlayer, ItemStack item) {
        //プレイヤー取得
        Player player = bePlayer.getPlayer();
        //アイテム名取得
        String iName = item.getItemMeta().getDisplayName();
        //名前で判定
        switch(iName) {
            case MASTER_SWORD_NAME:
                /* ==剣聖アビリティ==
                 * 5秒間スピードを1段階あげる(CD:15s)
                 */
                bePlayer.setAbilityTime(5, 15);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100,1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,100,2));
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.MASTER,5.0F,5.0F);
                break;
            case BERSERKER_AXE_NAME:
                /* ==狂戦士アビリティ==
                 * 次に殴った人を3秒スタンできる(CD:30s)
                 * AttackEvent.javaで記述
                 */
                bePlayer.setAbilityFlag(true);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.MASTER,5.0F,5.0F);
                break;
            case KNIGHT_AXE_NAME:
                /* ==重鎧兵アビリティ==
                 * 半径5メートル以内の敵の動きを3秒間止める(CD:30s)
                 */
                boolean flag = KnightCrash(bePlayer);
                //アビリティが発動した
                if (flag) {
                    bePlayer.setCooldown(30);
                    player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.MASTER,5.0F,5.0F);
                    player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.MASTER,2.5F,2.5F);
                }
                break;
            case BRAVE_SWORD_NAME:
                /* ==勇者アビリティ==
                 * 被ダメージの30%回復(CD:30s)
                 */
                BraveHeal(bePlayer);
                bePlayer.setCooldown(30);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER,5.0F,5.0F);
                break;
            case SNIPER_BOW_NAME:
                /* ==狙撃手アビリティ==
                 * なんか特殊な矢でも渡します？
                 */
                break;
            case ASSASSIN_DAGGER_NAME:
                /* ==暗殺者アビリティ==
                 * 10秒間透明化する
                 * 透明中は攻撃できない(CD:10s)
                 */
                bePlayer.setAbilityTime(10,10);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,200,1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
                player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_DEACTIVATE, SoundCategory.MASTER,5.0F,5.0F);
                break;
        }
    }

    public void activateUltimate(BePlayer bePlayer, ItemStack item) {
        Player player = bePlayer.getPlayer();
        String itemName = item.getItemMeta().getDisplayName();
        //ウルトオン
        bePlayer.setUltimate(true);

        switch (itemName) {
            case COUNTER_ARMOR_NAME:
                //足が速くなる
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 2));
                //装備付ける
                player.getInventory().setHelmet(BeItems.COUNTER_HELMET.toItemStack());
                player.getInventory().setChestplate(BeItems.COUNTER_CHESTPLATE.toItemStack());
                player.getInventory().setLeggings(BeItems.COUNTER_LEGGINGS.toItemStack());
                player.getInventory().setBoots(BeItems.COUNTER_BOOTS.toItemStack());
                //ウルト時間は20秒
                bePlayer.setUltTimer(20);
                //アイテム消す
                player.getInventory().remove(item);
                //効果音
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 4.0F, 4.0F);
                break;
            case INVINCIBLE_ARMOR_NAME:
                //装備付ける
                player.getInventory().setHelmet(BeItems.INVINCIBLE_HELMET.toItemStack());
                player.getInventory().setChestplate(BeItems.INVINCIBLE_CHESTPLATE.toItemStack());
                player.getInventory().setLeggings(BeItems.INVINCIBLE_LEGGINGS.toItemStack());
                player.getInventory().setBoots(BeItems.INVINCIBLE_BOOTS.toItemStack());
                //ウルト時間は20秒
                bePlayer.setUltTimer(30);
                //アイテム消す
                player.getInventory().remove(item);
                //効果音
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 4.0F, 4.0F);
                break;
            case LIGHTNING_SWORD_NAME:
            case LIGHTNING_AXE_NAME:
            case LIGHTNING_BOW_NAME:
                bePlayer.setUltimate(false);
        }
    }

    public void BraveHeal(BePlayer hero) {
        //勇者流回復術
        double health = hero.getPlayer().getHealth();
        double max = hero.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue();
        //回復量の求め方
        double amount = (max - health) * 0.3;
        //HPに足してあげよう!
        hero.getPlayer().setHealth(health + amount);
    }

    public boolean KnightCrash(BePlayer knight) {
        //地面を揺らして敵の足を止める
        Block target = knight.getPlayer().getTargetBlock(null,3);
        //3マス以内にターゲットブロックがない
        if (target.isEmpty()) { return false; }

        //ターゲットブロックの座標
        Location tLocation = target.getLocation();
        //演出大事
        tLocation.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, tLocation, 1);
        tLocation.getWorld().playSound(tLocation,Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.MASTER,5.0F,5.0F);
        tLocation.getWorld().playSound(tLocation,Sound.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.MASTER,2.5F,2.5F);
        for (BePlayer bp : BePlayerList.getBePlayerList()) {
            Location pLocation = bp.getPlayer().getLocation();
            if (pLocation.distance(tLocation) < 5.0) {
                //自分にかけないようにねｗ
                if (bp != knight) {
                    //5メートル以内のプレイヤーを鈍化
                    bp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60,10));
                }
            }
        }
        return true;
    }
}
