package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.BattleEmblemMain;
import jp.meao0525.battleemblem.battleclass.ClassStatus;
import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beitem.BeItemName;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import static jp.meao0525.battleemblem.beitem.BeItemName.*;

public class AttackEvent implements Listener {

    private BeGame game;

    public AttackEvent(BattleEmblemMain main) {
        this.game = main.getGame();
    }

    @EventHandler
    public void BeAttackEvent(EntityDamageByEntityEvent e) {
        //フェーズが2以外の時は攻撃できない
        if (game.getPhase() != 2) {
            e.setCancelled(true);
            return;
        }

        //攻撃したのもダメージ受けたのもPlayerか?
        if (!(e.getDamager() instanceof Player) && !(e.getEntity() instanceof Player)) {
            return;
        }

        Player attacker = (Player) e.getDamager();
        Player defender = (Player) e.getEntity();
        BePlayer beAttacker = BePlayerList.getBePlayer(attacker);
        BePlayer beDefender = BePlayerList.getBePlayer(defender);

        //ゲーム参加者じゃなかったわー笑
        if ((beAttacker == null) || (beDefender == null)) {
            return;
        }

        ItemStack item = attacker.getInventory().getItemInMainHand();
        //素手で殴ってやがるよ笑
        if (item == null) {
            e.setCancelled(true);
            return;
        }

        String itemName = item.getItemMeta().getDisplayName();
        double attack = beAttacker.getAttack();
        double defence = beDefender.getDefence();
        switch (itemName) {
            case MASTER_SWORD_NAME:
            case KNIGHT_AXE_NAME:
                e.setDamage(attack - defence);
                return;
            case BERSERKER_AXE_NAME:
            case SNIPER_BOW_NAME:
                e.setDamage(attack - defence);
                KnockBack(attacker,defender);
                return;
            case BRAVE_SWORD_NAME:
                //HP減少分の追加ダメージ
                double decrement = ClassStatus.BRAVE_HERO_STATUS.getHp() - attacker.getHealth();
                e.setDamage(attack - defence + decrement);
                return;
            case ASSASSIN_DAGGER_NAME:
                //背後からの攻撃か?
                if (isBackAttack(attacker,defender)) {
                    //背後からだと防御無視+追加ダメージ
                    attacker.sendMessage("背中に命中！");
                    e.setDamage(attack + 6);
                } else {
                    e.setDamage(attack - defence);
                }
                return;
            default:
                e.setCancelled(true);
        }

    }

    private boolean isBackAttack(Player attacker, Player defender) {
        //攻撃は背後からの攻撃か?
        Vector attackerDirection = attacker.getFacing().getDirection();
        Vector defenderDirection = defender.getFacing().getDirection();
        if (attackerDirection.equals(defenderDirection)) {
            return true;
        }
        return false;
    }

    private void KnockBack(Player attacker, Player defender) {
        //プレイヤーをノックバックさせる
        defender.setVelocity(attacker.getLocation().getDirection().setY(0).normalize().multiply(5));
    }
}
