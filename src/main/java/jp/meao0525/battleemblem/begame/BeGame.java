package jp.meao0525.battleemblem.begame;

import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public class BeGame {
    //フェーズ
    private int phase = 0;

    //プレイヤーリスト
    private ArrayList<Player> bePlayerList = new ArrayList<>();

    //カウントダウン用変数
    int count = 30;

    //コンストラクター
    public BeGame() {
    }

    public void Start() {
        //bePlayerListの作成
        createPlayerList();
        //準備時間
        phase = 1;

        //30秒カウントダウンする
        Timer timer = new Timer();
        Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "ゲーム開始まで");
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (count >= 10) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "あと" + ChatColor.AQUA + count + ChatColor.RESET + "秒");
                } else if (count > 5) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "あと " + ChatColor.RED + count + ChatColor.RESET + "秒");
                } else if (count > 0) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "あと " + ChatColor.RED + count + ChatColor.RESET + "秒");
                    bePlayerList.forEach(p -> {p.playSound(p.getLocation(),Sound.BLOCK_ANVIL_FALL,SoundCategory.MASTER,1.0F,1.0F);});
                } else {
                    /*=======カウントダウン終了=======*/
                    //全員をランダムなlocationにテレポート
                    ArrayList<Player> list = bePlayerList;
                    Collections.shuffle(list);

                    int n = 0;
                    for (Player p : list) {
                        //TODO: ランダムにスポーン
                        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, SoundCategory.MASTER,1.0F,1.0F);
                        //TODO: ロードアウトセレクターを取り上げる
                    }
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "開戦");
                    timer.cancel();
                }
                count--;
            }
        }, 0, 1000);

        //残り人数が一人になったら終わる
        while (bePlayerList.size() == 1) {
            //TODO:
        }
    }

    public void End() {
        //TODO: 終了処理
    }

    public void createPlayerList() {
        ArrayList<Player> list = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                list.add(p);
            }
        }
        setBePlayerList(list);
    }

    public ArrayList<Player> getBePlayerList() {
        return bePlayerList;
    }

    public void setBePlayerList(ArrayList<Player> bePlayerList) {
        this.bePlayerList = bePlayerList;
    }

    public int getPhase() {
        return phase;
    }
}
