package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beitem.BeItemName;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static jp.meao0525.battleemblem.beitem.BeItemName.*;

public class BeAbilityEvent implements Listener {

    public BeAbilityEvent() { }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        //ゲーム中しか使えない
        if (BeGame.getPhase() != 2) { return; }

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        String iName = item.getItemMeta().getDisplayName();
        switch(iName) {
            case MASTER_SWORD_NAME:
                /* ==剣聖アビリティ==
                 * 5秒間スピードを1段階あげる(CD:15s)
                 */
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100,1));
                break;
            case BERSERKER_AXE_NAME:
                /* ==狂戦士アビリティ==
                 * 殴った人を2秒スタンorめっちゃノックバック(CD:30s)
                 */
                break;
            case KNIGHT_AXE_NAME:
                /* ==重鎧兵アビリティ==
                 * 半径5メートル以内の敵の動きを5秒間止める(CD:30s)
                 */
                break;
            case BRAVE_SWORD_NAME:
                /* ==勇者アビリティ==
                 * ここどうしよう...
                 */
                break;
            case SNIPER_BOW_NAME:
                /* ==狙撃手アビリティ==
                 * なんか特殊な矢でも渡します？
                 */
                break;
            case ASSASSIN_DAGGER_NAME:
                /* ==暗殺者アビリティ==
                 * 10秒間透明化する
                 * 攻撃をすると透明化が解除される(CD:15s)
                 */
                break;
            default:
                return;
        }
    }
}
