package jp.meao0525.battleemblem.beevent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class RegainHealthBySatiatedEvent implements Listener {
    @EventHandler
    public void RegainHealthBySatiatedEvent(EntityRegainHealthEvent e) {
        //満腹による回復をキャンセル
        if (e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)) {
            e.setCancelled(true);
        }
    }
}
