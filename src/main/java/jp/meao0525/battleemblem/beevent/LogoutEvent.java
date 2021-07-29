package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.BattleEmblemMain;
import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beitem.BeItems;
import jp.meao0525.battleemblem.beitem.BeRuleBook;
import jp.meao0525.battleemblem.beplayer.BePlayer;

import jp.meao0525.battleemblem.beplayer.BePlayerList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LogoutEvent implements Listener {

    public LogoutEvent() { }

    @EventHandler
    public void PlayerLogoutEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        //BeGameからBePlayerのインスタンスを取得
        BePlayer bePlayer = BePlayerList.getBePlayer(player);

        //持ち物消す
        if (player.isOp()) {
            //ロードアウトセレクターとルールブックだけ消す
            player.getInventory().removeItem(BeItems.LOADOUT_SELECTOR.toItemStack(), new BeRuleBook().toItemStack());
        } else {
            //全部消す
            player.getInventory().clear();
        }

        //ログアウトしたのがBePlayerじゃない
        if (bePlayer == null) { return; }

        //バトルクラスをremoveする
        bePlayer.removeBattleClass();
        //プレイヤーリストを減らす
        BePlayerList.getBePlayerList().remove(bePlayer);

        //残り人数が一人以下ならゲーム終了
        if ((BeGame.getPhase() != 0) && (BePlayerList.getBePlayerList().size() <= 1)) {
            BeGame.End();
        }
    }
}
