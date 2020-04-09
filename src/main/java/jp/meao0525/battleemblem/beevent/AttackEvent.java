package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.BattleEmblemMain;
import jp.meao0525.battleemblem.battleclass.BattleClass;
import jp.meao0525.battleemblem.battleclass.ClassStatus;
import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beitem.BeItemName;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import static jp.meao0525.battleemblem.begame.BeLocation.coliseum;
import static jp.meao0525.battleemblem.beitem.BeItemName.*;

public class AttackEvent implements Listener {

    public AttackEvent() { }

    @EventHandler
    public void BeAttackEvent(EntityDamageByEntityEvent e) {
        //鎧ダメージを無効化するためにイベントをキャンセル
        e.setCancelled(true);

        //フェーズが2以外の時は攻撃できない
        if (BeGame.getPhase() != 2) { return; }

        //ダメージ受けたのがPlayerか?
        if (!(e.getEntity() instanceof Player)) { return; }
        //被ダメージプレイヤーの取得
        Player defender = (Player) e.getEntity();
        BePlayer beDefender = BePlayerList.getBePlayer(defender);
        //被ダメージプレイヤーは参加者じゃない
        if (beDefender == null) { return; }

        //ダメージ格納用変数
        double totalDamage = 0.0;
        //攻撃プレイヤー格納用
        Player attacker = null;

        /* 攻撃したのがプレイヤー -> if文の中
         * 攻撃したのが矢 -> else ifの中
         * それ以外のEntity -> デス判定だけすればいいよね
         *
         * 矢で撃たれた時はBeSnipeEventからArrowを受け取ってダメージを設定する
         */
        if (e.getDamager() instanceof Player) {
            //攻撃したプレイヤーの取得
            attacker = (Player) e.getDamager();
            BePlayer beAttacker = BePlayerList.getBePlayer(attacker);
            //ゲーム参加者じゃなかったわー笑
            if (beAttacker == null) { return; }

            //所持アイテム取得
            ItemStack item = attacker.getInventory().getItemInMainHand();
            //クールダウン中
            if (attacker.getCooldown(item.getType()) > 0) {
                //所持アイテムに改めてクールダウンを設定
                beAttacker.setIndicator(item);
                return;
            }

            //アイテム名取得
            String itemName = item.getItemMeta().getDisplayName();
            //ダメージ計算
            totalDamage = calcDamage(beAttacker,beDefender,itemName);

            //透明中(暗殺者)
            if (attacker.hasPotionEffect(PotionEffectType.INVISIBILITY)) { return; }
            //ノックバック(狙撃手)
            if ((itemName.equalsIgnoreCase(SNIPER_BOW_NAME))) { knockback(attacker,defender); }
            //スタンorノックバック(狂戦士)
            if (beAttacker.isBattleClass(BattleClass.BERSERKER)) {
                if (beAttacker.isAbilityFlag()) {
                    //殴られた人の移動速度をめちゃ下げる
                    defender.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,60,10));
                    //能力終了
                    beAttacker.setAbilityFlag(false);
                    //クールダウン
                    beAttacker.setCooldown(30);
                } else {
                    //アビリティ中じゃなきゃノックバック
                    knockback(attacker,defender);
                }
            }
            //パンチクールダウン
            setPlayerCoolDown(attacker);

        } else if (e.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getDamager();
            //矢からダメージをとりだすことはできますか?
            totalDamage = arrow.getDamage() - beDefender.getDefence();
            //矢をこれで消す
            arrow.remove();
            //狙撃者取得
            if (arrow.getShooter() instanceof Player) { attacker = (Player) arrow.getShooter(); }
        }

        /* HPは40に拡張されているのではなく見た目上引き伸ばされている
         *　ダメージのスケールも2倍になっているため2.0で割る
         * 被ダメージプレイヤーが重鎧兵の時、HPのスケールが60(通常の3倍)
         * に引き伸ばされているためダメージを3.0で割る
         */
        if (beDefender.isBattleClass(BattleClass.ARMOR_KNIGHT)) {
            totalDamage /= 3.0;
        } else {
            totalDamage /= 2.0;
        }

        //ダメージが残りHP以上ならデス処理するのだ
        if (totalDamage >= defender.getHealth()) {
            //キルとデスの追加
            if (attacker != null) {
                attacker.sendMessage(ChatColor.AQUA + defender.getDisplayName() + ChatColor.RESET + " をキルしました");
                attacker.setStatistic(Statistic.PLAYER_KILLS, attacker.getStatistic(Statistic.PLAYER_KILLS) + 1);
                defender.setStatistic(Statistic.DEATHS,defender.getStatistic(Statistic.DEATHS) + 1);
            }
            //死ぬ
            beDefender.death();
        } else {
            //ダメージを与える
            defender.damage(totalDamage);
        }
    }

    @EventHandler
    public void BeSnipeEvent(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getProjectile() instanceof Arrow)) {
            return;
        }
        //ゲーム中じゃない
        if (BeGame.getPhase() != 2) {
            e.setCancelled(true);
            return;
        }

        Arrow arrow = (Arrow) e.getProjectile();
        Player shooter = (Player) e.getEntity();
        BePlayer beShooter = BePlayerList.getBePlayer(shooter);
        //参加者じゃないやんけぇ
        if (beShooter == null) {
            e.setCancelled(true);
            return;
        }

        float force = e.getForce();
        //どんだけ引き絞った?
        if (force == 1.0F) {
            arrow.setDamage(10.0);
        } else {
            arrow.setDamage(5.0);
        }
        //ノックバック
        arrow.setKnockbackStrength(2);
        //矢をこれにしよう
        e.setProjectile(arrow);

        //矢を渡しましょう
        shooter.getInventory().remove(Material.ARROW);
        shooter.getInventory().addItem(new ItemStack(Material.ARROW));
    }

    public double calcDamage(BePlayer beAttacker, BePlayer beDefender, String itemName) {
        //攻撃力、防御力の取得
        double attack = beAttacker.getAttack();
        double defence = beDefender.getDefence();
        double damage = 0;

        //所持アイテムの名前で判定
        switch (itemName) {
            case MASTER_SWORD_NAME:
            case KNIGHT_AXE_NAME:
            case BERSERKER_AXE_NAME:
            case SNIPER_BOW_NAME:
                //通常ダメージ
                damage = attack - defence;
                break;

            case BRAVE_SWORD_NAME:
                //HP減少分の半分の追加ダメージ
                double max = beAttacker.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue();
                double hp = beAttacker.getPlayer().getHealth();
                double decrement = max - hp;
                damage = attack - defence + (decrement / 2.0);
                break;

            case ASSASSIN_DAGGER_NAME:
                //背後からの攻撃か?
                if (isBackAttack(beAttacker,beDefender)) {
                    //背後からだと防御無視+追加ダメージ
                    damage = attack + 6;
                } else {
                    //通常ダメージ
                    damage = attack - defence;
                }
                break;
        }

        return damage;
    }

//    public void PlayerDeath(BePlayer bePlayer) {
//        //プレイヤーの取得
//        Player p = bePlayer.getPlayer();
//        //残機はなんぼ?
//        if (bePlayer.getLife() > 0) {
//            //ライフを1減らす
//            bePlayer.setLife(bePlayer.getLife() - 1);
//            //初期位置にTP
//            p.teleport(coliseum);
//            //HPを回復
//            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
//            //残機を教えてあげて
//            p.sendMessage(ChatColor.GOLD + "[BattleEmblem]"
//                    + ChatColor.RESET + "残機は"
//                    + ChatColor.AQUA + bePlayer.getLife()
//                    + ChatColor.RESET + "です");
//        } else {
//            //バトルクラスを外す
//            bePlayer.removeBattleClass();
//            //プレイヤーリストから外す
//            BePlayerList.getBePlayerList().remove(bePlayer);
//            //観戦者にする
//            p.setGameMode(GameMode.SPECTATOR);
//            //初期位置にTP
//            p.teleport(coliseum);
//            //デスメッセージ
//            Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + p.getPlayerListName() + " が脱落しました");
//
//            //残り人数が一人以下ならゲーム終了
//            if (BePlayerList.getBePlayerList().size() <= 1) {
//                BeGame.End();
//            }
//        }
//    }

    private boolean isBackAttack(BePlayer beAttacker, BePlayer beDefender) {
        /*
         * プレイヤーの視点先のブロック表面の法線ベクトルを取得
         * これを比較して一致すればとりあえず背後からの攻撃とする(これでは厳密な背後判定ではない)
         */
        Vector attackerDirection = beAttacker.getPlayer().getFacing().getDirection();
        Vector defenderDirection = beDefender.getPlayer().getFacing().getDirection();
        //攻撃は背後からの攻撃か?
        if (attackerDirection.equals(defenderDirection)) {
            return true;
        }
        return false;
    }

    private void knockback(Player attacker, Player defender) {
        //プレイヤーをノックバックさせる
        defender.setVelocity(attacker.getLocation().getDirection().setY(0).normalize().multiply(3));
    }

    private void setPlayerCoolDown(Player player) {
        //アイテムによってクールダウンを変える
        ItemStack item = player.getInventory().getItemInMainHand();
        switch (item.getType()) {
            case DIAMOND_AXE:
            case IRON_AXE:
            case GOLDEN_AXE:
            case STONE_AXE:
                player.setCooldown(item.getType(), 20);
                break;
            case DIAMOND_SWORD:
            case IRON_SWORD:
            case GOLDEN_SWORD:
            case STONE_SWORD:
            case BOW:
                player.setCooldown(item.getType(), 10);
                break;
        }
    }
}
