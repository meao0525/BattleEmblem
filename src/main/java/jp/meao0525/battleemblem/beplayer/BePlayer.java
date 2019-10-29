package jp.meao0525.battleemblem.beplayer;

import org.bukkit.entity.Player;

public class BePlayer {
    private Player player;

    public BePlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
