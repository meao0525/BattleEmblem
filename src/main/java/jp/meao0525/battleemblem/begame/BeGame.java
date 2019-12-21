package jp.meao0525.battleemblem.begame;

import com.sun.istack.internal.Nullable;
import jp.meao0525.battleemblem.battleclass.BattleClass;
import jp.meao0525.battleemblem.beitem.BeItems;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BeGame {
    //フェーズ
    private int phase = 0;

    //プレイヤーリスト
    private ArrayList<BePlayer> bePlayerList = new ArrayList<>();

    //カウントダウン用変数
    int count;

    //コンストラクター
    public BeGame() { }

    public void Start(@Nullable BattleClass battleClass) {
        //bePlayerListの取得
        this.bePlayerList = createPlayerList();

        //プレイヤーがいない
        if (bePlayerList.size() == 0) {
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "誰もいないね...");
            return;
        }

        if (battleClass == null) {
            //クラスを選択してないプレイヤーにランダムクラスを与える
            for (BePlayer bp : bePlayerList) {
                if (bp.getPlayer().getPlayerListHeader().isEmpty()) {
                    bp.setBattleClass(getRandomClass());
                }
            }
        } else {
            for (BePlayer bp : bePlayerList) {
                bp.setBattleClass(battleClass);
            }
        }

        /*=======準備時間=======*/
        setPhase(1);

        //30秒カウントダウンする
        count = 30;
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
                    //全員をランダムなlocationにテレポート
                    ArrayList<BePlayer> list = bePlayerList;
                    Collections.shuffle(list);

                    int n = 0; //テレポートロケーション用
                    for (BePlayer bp : list) {
                        //TODO: ランダムにスポーン
                        //開戦のエフェクト
                        bp.getPlayer().playSound(bp.getPlayer().getLocation(), Sound.ENTITY_WITHER_SPAWN, SoundCategory.MASTER,5.0F,5.0F);
                        bp.getPlayer().playSound(bp.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.MASTER,5.0F,5.0F);
                        bp.getPlayer().sendTitle(ChatColor.AQUA + "-開戦-",null,1,60,1);
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

        BePlayer winner = bePlayerList.get(0);

        //結果発表おおおおおおおおおおおおおお
        Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]"
                + ChatColor.RESET + "勝者は"
                + ChatColor.AQUA + "[" + winner.getPlayer().getPlayerListHeader() + "]"
                + ChatColor.GOLD + winner.getPlayer().getDisplayName()
                + ChatColor.RESET + "です");
        Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "おめでとうございます");

        //removeBattleClassをする
        for (BePlayer bp : bePlayerList) {
            bp.removeBattleClass();
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            //TODO: 初期リスに飛ばす
            //ロードアウトセレクターを渡す
            p.getInventory().addItem(BeItems.LOADOUT_SELECTOR.toItemStack());
        }
    }

    public ArrayList<BePlayer> createPlayerList() {
        ArrayList<BePlayer> list = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                list.add(new BePlayer(p));
            }
        }
        return list;
    }

    public BattleClass getRandomClass() {
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

    public BePlayer getBePlayer(Player player) {
        BePlayer bePlayer = null;
        for (BePlayer bp : bePlayerList) {
            if (bp.getPlayer().equals(player)) {
                bePlayer = bp;
            }
        }
        return bePlayer;
    }

    public ArrayList<BePlayer> getBePlayerList() {
        return bePlayerList;
    }

    public void setBePlayerList(ArrayList<BePlayer> bePlayerList) {
        this.bePlayerList = bePlayerList;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }
}
