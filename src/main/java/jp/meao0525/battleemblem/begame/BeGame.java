package jp.meao0525.battleemblem.begame;

import jp.meao0525.battleemblem.battleclass.BattleClass;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collector;

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
        /*=======準備時間=======*/
        setPhase(1);

        //bePlayerListの作成
        createPlayerList();

        //未選択の人にランダムクラスを付与
        bePlayerList.forEach(p -> {
            if (p.getPlayerListHeader().isEmpty()) {
                BePlayer bp = new BePlayer(p);
                bp.setBattleClass(getRandomClass());
            }
        });

        //30秒カウントダウンする
        Timer timer = new Timer();
        Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "ゲーム開始まで");
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (count >= 10) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "あと" + ChatColor.AQUA + count + ChatColor.RESET + "秒");
                    bePlayerList.forEach(p -> {p.playSound(p.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,SoundCategory.MASTER,5.0F,5.0F);});

                } else if (count > 5) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "あと " + ChatColor.RED + count + ChatColor.RESET + "秒");
                    bePlayerList.forEach(p -> {p.playSound(p.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,SoundCategory.MASTER,5.0F,5.0F);});

                } else if (count > 0) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "あと " + ChatColor.RED + count + ChatColor.RESET + "秒");
                    bePlayerList.forEach(p -> {p.playSound(p.getLocation(),Sound.BLOCK_ANVIL_PLACE,SoundCategory.MASTER,5.0F,5.0F);});

                } else {
                    /*=======開戦=======*/
                    setPhase(2);
                    //全員をランダムなlocationにテレポート
                    ArrayList<Player> list = bePlayerList;
                    Collections.shuffle(list);

                    int n = 0; //テレポートロケーション用
                    for (Player p : list) {
                        //TODO: ランダムにスポーン
                        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, SoundCategory.MASTER,5.0F,5.0F);
                        p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.MASTER,5.0F,5.0F);
                        //TODO: 「開戦」のタイトル表示
                        //TODO: ロードアウトセレクターを取り上げる
                    }
                    Bukkit.broadcastMessage(ChatColor.AQUA + "=====" + ChatColor.RESET + "開戦" + ChatColor.AQUA + "=====");
                    End(); //TODO: ここはあとで消す
                    timer.cancel();
                }
                count--;
            }
        }, 0, 1000);

        //残り人数が一人になったら終わる
//        while (bePlayerList.size() == 1) {
//        }
    }

    public void End() {
        setPhase(0);
        //TODO: 終了処理
    }

    private void createPlayerList() {
        ArrayList<Player> list = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                list.add(p);
            }
        }
        setBePlayerList(list);
    }

    private BattleClass getRandomClass() {
        //未使用のクラスのリストを作成
        ArrayList<BattleClass> classList = new ArrayList<>();
        for (BattleClass bc : BattleClass.values()) {
            if (!bc.isUsed()) {
                classList.add(bc);
            }
        }
        //リストをシャッフル
        Collections.shuffle(classList);

        return classList.get(1);
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

    public void setPhase(int phase) {
        this.phase = phase;
    }
}
