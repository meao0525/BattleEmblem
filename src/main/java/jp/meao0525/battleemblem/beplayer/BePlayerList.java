package jp.meao0525.battleemblem.beplayer;

import jp.meao0525.battleemblem.battleclass.BattleClass;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BePlayerList {
    private static ArrayList<BePlayer> bePlayerList = new ArrayList<>();

    public BePlayerList() { }

    public static void createPlayerList() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            //アドベンチャーモードでクラスを選択していない人
            if ((p.getGameMode().equals(GameMode.ADVENTURE)) && (p.getPlayerListName().equalsIgnoreCase(p.getDisplayName()))) {
                BePlayer bp = new BePlayer(p);
                //ランダムクラスを与える
                bp.setBattleClass(getRandomClass());
                //プレイヤーリストに追加
                bePlayerList.add(bp);
            }
        }
    }

    public static BePlayer getBePlayer(Player player) {
        BePlayer bePlayer = null;
        for (BePlayer bp : bePlayerList) {
            if (bp.getPlayer().equals(player)) {
                bePlayer = bp;
            }
        }
        return bePlayer;
    }

    public static ArrayList<BePlayer> getBePlayerList() {
        return bePlayerList;
    }

    private static BattleClass getRandomClass() {
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
}
