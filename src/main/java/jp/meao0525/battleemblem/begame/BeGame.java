package jp.meao0525.battleemblem.begame;

import jp.meao0525.battleemblem.battleclass.BattleClass;
import jp.meao0525.battleemblem.beevent.RegainHealthEvent;
import jp.meao0525.battleemblem.beitem.BeItems;
import jp.meao0525.battleemblem.beitem.BeRuleBook;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static jp.meao0525.battleemblem.beevent.RegainHealthEvent.healingPlayers;
import static jp.meao0525.battleemblem.begame.BeLocation.*;

public class BeGame {
    //フェーズ
    private static int phase = 0;
    /*
     * phase = 0 ロビー時間
     * phase = 1 準備時間
     * phase = 2 ゲーム時間
     */

    //プレイヤーリスト
    private ArrayList<BePlayer> bePlayerList = new ArrayList<>();

    //カウントダウン用変数
    int count;

    //アルティメットゲージ用変数
    private static Timer ultTimer;

    //コンストラクター
    public BeGame() { }

    public void Start(BattleClass battleClass) {
        //プレイヤーリスト作成
        BePlayerList.createPlayerList(battleClass);

        //プレイヤーリストの取得
        bePlayerList = BePlayerList.getBePlayerList();

        //プレイヤーがいない
        if (bePlayerList.size() == 0) { //TODO: ここは最終的には1以下
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "誰もいないね...");
            return;
        }

        for (BePlayer bp : bePlayerList) {
            //リスポーンを指定
            bp.getPlayer().setBedSpawnLocation(re_coliseum,true);
            //ステージにスポーン
            bp.getPlayer().teleport(coliseum);
            //ルールブック回収
            bp.getPlayer().getInventory().remove(new BeRuleBook().toItemStack());
        }
        //観戦者も飛ばしてアイテム回収
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getGameMode().equals(GameMode.SPECTATOR)) {
                p.teleport(coliseum);
                p.getInventory().remove(new BeRuleBook().toItemStack());
                p.getInventory().remove(BeItems.LOADOUT_SELECTOR.toItemStack());
            }
        }

        /*=======準備時間=======*/
        setPhase(1);

        //20秒カウントダウンする
        count = 20;
        Timer timer = new Timer();
        Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "ゲーム開始まで");
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (count >= 10) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "あと" + ChatColor.AQUA + count + ChatColor.RESET + "秒");
                    bePlayerList.forEach(bp -> {bp.getPlayer().playSound(bp.getPlayer().getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,SoundCategory.MASTER,5.0F,5.0F);});

                } else if (count > 5) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "あと " + ChatColor.RED + count + ChatColor.RESET + "秒");
                    bePlayerList.forEach(bp -> {bp.getPlayer().playSound(bp.getPlayer().getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,SoundCategory.MASTER,5.0F,5.0F);});

                } else if (count > 0) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "あと " + ChatColor.RED + count + ChatColor.RESET + "秒");
                    bePlayerList.forEach(bp -> {bp.getPlayer().playSound(bp.getPlayer().getLocation(),Sound.BLOCK_ANVIL_PLACE,SoundCategory.MASTER,5.0F,5.0F);});

                } else {
                    /*=======開戦=======*/
                    setPhase(2);

                    for (BePlayer bp : bePlayerList) {
                        //開戦のエフェクト
                        bp.getPlayer().playSound(bp.getPlayer().getLocation(), Sound.ENTITY_WITHER_SPAWN, SoundCategory.MASTER,4.0F,4.0F);
                        bp.getPlayer().playSound(bp.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.MASTER,5.0F,5.0F);
                        bp.getPlayer().sendTitle(ChatColor.AQUA + "-開戦-",null,1,60,1);
                    }
                    Bukkit.broadcastMessage(ChatColor.AQUA + "=====" + ChatColor.RESET + "開戦" + ChatColor.AQUA + "=====");
                    //ウルトゲージタイマー開始
                    startUltTimer();
                    timer.cancel();
                }
                count--;
            }
        }, 0, 1000);

    }

    public static void End() {
        if (BePlayerList.getBePlayerList().size() != 0) {
            //最後の一人が勝者だよね
            BePlayer winner = BePlayerList.getBePlayerList().get(0);

            if (winner != null) {
                //結果発表おおおおおおおおおおおおおお
                Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "勝者は ");
                Bukkit.broadcastMessage(winner.getPlayer().getPlayerListName() + "です");
                Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "おめでとうございます!");
            }

            for (BePlayer bp : BePlayerList.getBePlayerList()) {
                //BattleClassを剥ぎ取る(一応全員)
                bp.removeBattleClass();
            }
            //リストを空にする
            BePlayerList.getBePlayerList().clear();
        } else {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "勝者はいませんでした");
        }

        //リジェネのMAPを消し飛ばす
        healingPlayers.clear();

        //ウルトタイマー止める
        ultTimer.cancel();

        for (Player p : Bukkit.getOnlinePlayers()) {
            //ゲームモードをアドベンチャーにする
            if (!p.getGameMode().equals(GameMode.CREATIVE)) { p.setGameMode(GameMode.ADVENTURE); }
            //ロビーに飛ばす
            p.teleport(lobby);
            //リスポーンを指定
            p.setBedSpawnLocation(lobby,true);
            //一旦アイテム消す
            p.getInventory().clear();
            //ロードアウトセレクターを渡す
            p.getInventory().addItem(BeItems.LOADOUT_SELECTOR.toItemStack());
            //ルールブックを渡す
            p.getInventory().addItem(new BeRuleBook().toItemStack());
        }

        //バトルクラスの使用をすべて許可する
        for (BattleClass bc : BattleClass.values()) {
            bc.setUsed(false);
        }

        //フェーズを0に戻す
        setPhase(0);
    }

    private void startUltTimer() {
        ultTimer = new Timer();
        ultTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (BePlayer bp : BePlayerList.getBePlayerList()) {
                    Player p = bp.getPlayer();
                    //レベルが1未満の人に経験値
                    if (p.getLevel() < 1) {
                        float exp = 0;
                        //経験値を渡す
                        if ((bp.isBattleClass(BattleClass.SWORD_MASTER))
                                || (bp.isBattleClass(BattleClass.BERSERKER))
                                || (bp.isBattleClass(BattleClass.SNIPER))) {
                            //狂戦士、剣聖のウルトは125秒後
                            exp = 0.008F;
                        } else if ((bp.isBattleClass(BattleClass.ARMOR_KNIGHT))
                                || (bp.isBattleClass(BattleClass.BRAVE_HERO))
                                || (bp.isBattleClass(BattleClass.ASSASSIN))) {
                            //重鎧兵、勇者、狙撃手、暗殺者のウルトは100秒後
                            exp = 0.01F;
                        }

                        if (1.0F - p.getExp() > exp) {
                            //まだ足せる
                            p.setExp(p.getExp() + exp);
                        } else {
                            //あふれる
                            p.setExp(0);
                            p.setLevel(1);
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 4.0F, 4.0F);
                        }

                        //レベルが1になったらアイテム渡す
                        if (p.getLevel() == 1) {
                            if (bp.isBattleClass(BattleClass.SWORD_MASTER)) {
                                p.getInventory().addItem(BeItems.LIGHTNING_SWORD.toItemStack());
                            } else if (bp.isBattleClass(BattleClass.BERSERKER)) {
                                p.getInventory().addItem(BeItems.LIGHTNING_AXE.toItemStack());
                            } else if (bp.isBattleClass(BattleClass.ARMOR_KNIGHT)) {
                                p.getInventory().addItem(BeItems.COUNTER_ARMOR.toItemStack());
                            } else if (bp.isBattleClass(BattleClass.BRAVE_HERO)) {
                                p.getInventory().addItem(BeItems.INVINCIBLE_ARMOR.toItemStack());
                            } else if (bp.isBattleClass(BattleClass.SNIPER)) {
                                p.getInventory().addItem(BeItems.LIGHTNING_BOW.toItemStack());
                            } else if (bp.isBattleClass(BattleClass.ASSASSIN)) {
                                p.getInventory().addItem(BeItems.DEADLY_DAGGER.toItemStack());
                            }
                        }
                    }
                }
            }
        }, 0, 1000);
    }

    public static int getPhase() {
        return phase;
    }

    public static void setPhase(int phase) {
        BeGame.phase = phase;
    }
}
