package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beitem.BeItemName;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.ChatColor;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static jp.meao0525.battleemblem.beitem.BeItemName.*;

public class BeAbilityEvent implements Listener {

    private Plugin plugin;

    public BeAbilityEvent(Plugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        //ゲーム中しか使えない
        if (BeGame.getPhase() != 2) { return; }

        Player player = e.getPlayer();
        BePlayer bePlayer = BePlayerList.getBePlayer(player);

        //ゲームプレイヤーじゃないやんけぇ
        if (bePlayer == null) { return; }
        //アビリティ使用中ですよ
        if (bePlayer.isUsingAbility()) {
            player.sendMessage(ChatColor.GRAY + "アビリティを使用中です");
            return;
        }
        //くぉーるだぅん
        if (bePlayer.getCooldown() != 0) {
            player.sendMessage(ChatColor.GRAY + "クールダウン中です");
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        String iName = item.getItemMeta().getDisplayName();

        switch(iName) {
            case MASTER_SWORD_NAME:
                /* ==剣聖アビリティ==
                 * 5秒間スピードを1段階あげる(CD:15s)
                 */
                bePlayer.setCooldown(15);
                bePlayer.runTaskTimer(plugin,0,20); //TODO: なにかがnullらしい
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100,1));
                return;
            case BERSERKER_AXE_NAME:
                /* ==狂戦士アビリティ==
                 * 殴った人を2秒スタンorめっちゃノックバック(CD:30s)
                 */
                return;
            case KNIGHT_AXE_NAME:
                /* ==重鎧兵アビリティ==
                 * 半径5メートル以内の敵の動きを5秒間止める(CD:30s)
                 */
                return;
            case BRAVE_SWORD_NAME:
                /* ==勇者アビリティ==
                 * 被ダメージの50%回復(CD:30s)
                 */
                return;
            case SNIPER_BOW_NAME:
                /* ==狙撃手アビリティ==
                 * なんか特殊な矢でも渡します？
                 */
                return;
            case ASSASSIN_DAGGER_NAME:
                /* ==暗殺者アビリティ==
                 * 10秒間透明化する
                 * 攻撃をすると透明化が解除される(CD:10s)
                 */
                return;
            default:
                return;
        }
    }

    //TODO: アビリティのタイマーはこっちに書かなきゃいけないのでは
}
