package jp.meao0525.battleemblem.begame;

import jp.meao0525.battleemblem.battleclass.BattleClass;
import jp.meao0525.battleemblem.beitem.BeItems;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static jp.meao0525.battleemblem.begame.BeLocation.coliseum;
import static jp.meao0525.battleemblem.begame.BeLocation.lobby;

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

    //コンストラクター
    public BeGame() { }

    public void Start(BattleClass battleClass) {
        //プレイヤーリスト作成
        BePlayerList.createPlayerList();

        //プレイヤーリストの取得
        bePlayerList = BePlayerList.getBePlayerList();

        //プレイヤーがいない
        if (bePlayerList.size() == 0) { //TODO: ここは最終的には1以下
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "誰もいないね...");
            return;
        }

        if (battleClass != null) {
            for (BePlayer bp : bePlayerList) {
                bp.setBattleClass(battleClass);
            }
        }

        for (BePlayer bp : bePlayerList) {
            //ロードアウトセレクターは没収だあ！！！
            bp.getPlayer().getInventory().remove(BeItems.LOADOUT_SELECTOR.toItemStack());
            //リスポーンを指定
            bp.getPlayer().setBedSpawnLocation(coliseum,true);
            //ステージにスポーン
            bp.getPlayer().teleport(coliseum);
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
                        bp.getPlayer().playSound(bp.getPlayer().getLocation(), Sound.ENTITY_WITHER_SPAWN, SoundCategory.MASTER,5.0F,5.0F);
                        bp.getPlayer().playSound(bp.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.MASTER,5.0F,5.0F);
                        bp.getPlayer().sendTitle(ChatColor.AQUA + "-開戦-",null,1,60,1);
                    }
                    Bukkit.broadcastMessage(ChatColor.AQUA + "=====" + ChatColor.RESET + "開戦" + ChatColor.AQUA + "=====");
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

            //BattleClassを剥ぎ取る(一応全員)
            for (BePlayer bp : BePlayerList.getBePlayerList()) {
                bp.removeBattleClass();
            }
            //リストを空にする
            BePlayerList.getBePlayerList().clear();
        } else {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "勝者はいませんでした");
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            //ゲームモードをアドベンチャーにする
            if (!p.getGameMode().equals(GameMode.CREATIVE)) { p.setGameMode(GameMode.ADVENTURE); }
            //ロビーに飛ばす
            p.teleport(lobby);
            //リスポーンを指定
            p.setBedSpawnLocation(lobby,true);
            //ロードアウトセレクターを渡す
            p.getInventory().addItem(BeItems.LOADOUT_SELECTOR.toItemStack());
        }

        //バトルクラスの使用をすべて許可する
        for (BattleClass bc : BattleClass.values()) {
            bc.setUsed(false);
        }

        //フェーズを0に戻す
        setPhase(0);
    }

    public static int getPhase() {
        return phase;
    }

    public static void setPhase(int phase) {
        BeGame.phase = phase;
    }
}
