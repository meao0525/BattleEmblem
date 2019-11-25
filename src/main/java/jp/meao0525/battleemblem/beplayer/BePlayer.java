package jp.meao0525.battleemblem.beplayer;

import jp.meao0525.battleemblem.battleclass.BattleClass;
import org.bukkit.entity.Player;

public class BePlayer {
    private Player player;
    private int attack;
    private int defence;

    public BePlayer(Player player) {
        this.player = player;
    }

    public void setBattleClass(BattleClass battleClass) {
        player.setPlayerListHeader(battleClass.getName());
    }

    public Player getPlayer() {
        return player;
    }
}
