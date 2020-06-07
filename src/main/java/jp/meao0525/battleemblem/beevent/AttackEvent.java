package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.BattleEmblemMain;
import jp.meao0525.battleemblem.battleclass.BattleClass;
import jp.meao0525.battleemblem.battleclass.ClassStatus;
import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beitem.BeItemName;
import jp.meao0525.battleemblem.beitem.BeItems;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

        //重鎧兵の音
        if (beDefender.isBattleClass(BattleClass.ARMOR_KNIGHT)) { defender.getWorld().playSound(defender.getLocation(), Sound.ENTITY_BLAZE_HURT, 4.0f,4.0F); }

        //ダメージ格納用変数
        double totalDamage = 0.0;
        //攻撃プレイヤー格納用
        Player attacker = null;

        /* 攻撃したのがプレイヤー -> if文の中
         * 攻撃したのが矢 -> else ifの中
         * それ以外のEntity -> デス判定だけすればいいよね
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
            //何も持ってない
            if (item.getType().equals(Material.AIR)) {
                return;
            }
            //ダメージ計算
            totalDamage = calcDamage(beAttacker,beDefender,item);

            //その他の効果
            attackEffect(attacker,defender,beAttacker,beDefender);
            //パンチクールダウン
            beAttacker.setIndicator(item);

        } else if (e.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getDamager();
            //BeSnipeEventからArrowを受け取ってダメージを設定する
            totalDamage = arrow.getDamage() - beDefender.getDefence();
            //ウルト?
            if (arrow.getColor().equals(Color.YELLOW)) {
                defender.getWorld().strikeLightningEffect(defender.getLocation());
            }
            //矢をこれで消す
            arrow.remove();
            if (arrow.getShooter() instanceof Player) {
                //狙撃者取得
                attacker = (Player) arrow.getShooter();
                //ノックバック
                knockback(attacker, beDefender, 2);
            }
        }

        //無敵中
        if (beDefender.isUltimate()) {
            if (beDefender.isBattleClass(BattleClass.ARMOR_KNIGHT) || beDefender.isBattleClass(BattleClass.BRAVE_HERO)) {
                if (totalDamage < 8000.0) {
                    defender.playEffect(EntityEffect.HURT);
                    defender.getWorld().playSound(defender.getLocation(), Sound.ENTITY_PLAYER_HURT, 4.0F, 4.0F);
                    return;
                }
            }
        }
        //最後に攻撃した人
        beDefender.setLastDamager(attacker);

        //ダメージ与える
        beDefender.damage(totalDamage);
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

        //これがウルト弓を撃ったもの
        if (shooter.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(LIGHTNING_BOW_NAME)) {
            arrow.setDamage(30.0);
            //色付けて判別しよう
            arrow.setColor(Color.YELLOW);
            //何回目ですこ
            if (beShooter.getUltCount() < 2) {
                beShooter.setUltCount(beShooter.getUltCount() + 1);
            } else {
                //アイテム消す
                shooter.getInventory().remove(shooter.getInventory().getItemInMainHand());
                //ウルトカウント戻す
                beShooter.setUltCount(0);
                //ウルトゲージリセット
                beShooter.resetUltimatebar();
            }
        } else {
            arrow.setColor(Color.WHITE);
        }
        //矢をこれにしよう
        e.setProjectile(arrow);

        //矢を渡しましょう
        shooter.getInventory().remove(Material.ARROW);
        shooter.getInventory().addItem(new ItemStack(Material.ARROW));
    }

    public void attackEffect(Player attacker, Player defender, BePlayer beAttacker, BePlayer beDefender) {
        //ノックバックの強さ
        int knockbackStrength = 0;
        if (attacker.isSprinting()) {
            //ダッシュ中
            if (beAttacker.isBattleClass(BattleClass.BERSERKER)) {
                knockbackStrength = 3;
            } else {
                knockbackStrength = 2;
            }
        } else {
            //ダッシュしてない
            if (beAttacker.isBattleClass(BattleClass.BERSERKER)) {
                knockbackStrength = 2;
            }
        }

        //透明中(暗殺者)に攻撃すると解除
        if (attacker.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            beAttacker.stopAbilityTime();
            beAttacker.setCooldown(10);
            attacker.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        //スタンorノックバック(狂戦士)
        if (beAttacker.isBattleClass(BattleClass.BERSERKER)) {
            if (beAttacker.isAbilityFlag()) {
                //殴られた人の移動速度をめちゃ下げる
                defender.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,60,10));
                defender.playSound(defender.getLocation(),Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR,5.0F,5.0F);
                attacker.playSound(defender.getLocation(),Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR,5.0F,5.0F);
                //能力終了
                beAttacker.setAbilityFlag(false);
                //クールダウン
                beAttacker.setCooldown(30);
                //ノックバック消す
                knockbackStrength = 0;
            }
        }
        //ノックバック
        knockback(attacker,beDefender,knockbackStrength);
    }

    public double calcDamage(BePlayer beAttacker, BePlayer beDefender, ItemStack item) {
        //アイテム名取得
        String itemName = item.getItemMeta().getDisplayName();
        //攻撃力、防御力の取得
        double attack = beAttacker.getAttack();
        double defence = beDefender.getDefence();
        double damage = 0.0;

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
                double decrement = (max - hp) * 2.0; //スケール調整
                damage = attack - defence + (decrement * 0.2);
                break;

            case ASSASSIN_DAGGER_NAME:
                //背後からの攻撃か?
                if (isBackAttack(beAttacker,beDefender)) {
                    //背後からだと防御無視+追加ダメージ
                    damage = attack + 6;
                    beAttacker.getPlayer().playSound(beAttacker.getPlayer().getLocation(), Sound.ITEM_TRIDENT_RETURN, 4.0F, 4.0F);
                } else {
                    //通常ダメージ
                    damage = attack - defence;
                }
                break;

            case LIGHTNING_SWORD_NAME:
            case LIGHTNING_AXE_NAME:
                //ウルトによる特大ダメージ
                damage = 30.0;
                //雷エフェクト
                beDefender.getPlayer().getWorld().strikeLightningEffect(beDefender.getPlayer().getLocation());
                //アイテム消す
                beAttacker.getPlayer().getInventory().remove(item);
                //ウルトゲージリセット
                beAttacker.resetUltimatebar();
                break;

            case DEADLY_DAGGER_NAME:
                //背後からの攻撃か
                if (isBackAttack(beAttacker,beDefender)) {
                    beAttacker.getPlayer().sendMessage(ChatColor.DARK_RED + "暗殺成功...");
                    damage = 8000.0;
                }
                //アイテム消す
                beAttacker.getPlayer().getInventory().remove(item);
                beAttacker.getPlayer().playSound(beAttacker.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 4.0F, 4.0F);
                //ウルトゲージリセット
                beAttacker.resetUltimatebar();
                break;
        }

        return damage;
    }

    private boolean isBackAttack(BePlayer beAttacker, BePlayer beDefender) {
        /* プレイヤーの視線をベクトルで取得
         * 2人の視線の角度が60度以下なら背後からの攻撃
         * マイナスのことも考慮してcosが1/2以下かどうかで判定
         */
        float angle = beAttacker.getEyeVector().angle(beDefender.getEyeVector());
        if (Math.cos(angle) >= 0.5) { return true; }

        return false;
    }

    private void knockback(Player attacker, BePlayer beDefender, int strength) {
        //相手が無敵中はノックバックしないでサヨナラ
        if (beDefender.isUltimate()) {
            if (beDefender.isBattleClass(BattleClass.ARMOR_KNIGHT) || beDefender.isBattleClass(BattleClass.BRAVE_HERO)) {
                return;
            }
        }
        //プレイヤーをノックバックさせる
        beDefender.getPlayer().setVelocity(new Vector(0, 2, 0));
        beDefender.getPlayer().setVelocity(attacker.getLocation().getDirection().setY(0).normalize().multiply(strength));
    }
}
