package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beitem.BeItemName;
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
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.MASTER,5.0F,5.0F);
                break;
            case BERSERKER_AXE_NAME:
                /* ==狂戦士アビリティ==
                 * 次に殴った人を2秒スタンできる(CD:30s)
                 * AttackEvent.javaで記述
                 */
                bePlayer.setAbilityFlag(true);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.MASTER,5.0F,5.0F);
                break;
            case KNIGHT_AXE_NAME:
                /* ==重鎧兵アビリティ==
                 * 半径5メートル以内の敵の動きを5秒間止める(CD:30s)
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
                 * 被ダメージの50%回復(CD:15s)
                 */
                BraveHeal(bePlayer);
                bePlayer.setCooldown(15);
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
                player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, SoundCategory.MASTER,5.0F,5.0F);
                break;
        }
    }

    public void BraveHeal(BePlayer hero) {
        //勇者流回復術
        double health = hero.getPlayer().getHealth();
        double max = hero.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue();
        //回復量の求め方
        double amount = (max - health) / 2.0;
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
        for (BePlayer bp : BePlayerList.getBePlayerList()) {
            Location pLocation = bp.getPlayer().getLocation();
            if (pLocation.distance(tLocation) < 5.0) {
                //自分にかけないようにねｗ
                if (bp != knight) {
                    //5メートル以内のプレイヤーを鈍化
                    bp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60,10));
                    bp.getPlayer().playSound(bp.getPlayer().getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.MASTER,5.0F,5.0F);
                    bp.getPlayer().playSound(bp.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.MASTER,2.5F,2.5F);
                }
            }
        }
        return true;
    }
}
