package jp.meao0525.battleemblem.beevent;

import jp.meao0525.battleemblem.BattleEmblemMain;
import jp.meao0525.battleemblem.begame.BeGame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BeDeathEvent implements Listener {

    private BeGame game;

    public BeDeathEvent(BattleEmblemMain main) { this.game = main.getGame(); }

    @EventHandler
    public void DeathEvent(PlayerDeathEvent e) {
        //ゲーム中じゃない
        if (game.getPhase() == 0) { return; }

        
    }
}
