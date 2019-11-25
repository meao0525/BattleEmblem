package jp.meao0525.battleemblem.battleclass;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static jp.meao0525.battleemblem.battleclass.BattleClassName.*;

public enum BattleClass {
    SWORD_MASTER(SWORD_MASTER_NAME, Material.DIAMOND_SWORD, ClassStatus.SWORD_MASTER_STATUS),
    BERSERKER(BERSERKER_NAME, Material.DIAMOND_AXE, ClassStatus.BERSERKER_STATUS),
    ARMOR_KNIGHT(ARMOR_KNIGHT_NAME, Material.DIAMOND_CHESTPLATE, ClassStatus.ARMOR_KNIGHT_STATUS),
    BRAVE_HERO(BRAVE_HERO_NAME, Material.GOLDEN_CHESTPLATE, ClassStatus.BRAVE_HERO_STATUS),
    SNIPER(SNIPER_NAME, Material.BOW, ClassStatus.SNIPER_STATUS),
    ASSASSIN(ASSASSIN_NAME, Material.IRON_SWORD, ClassStatus.ASSASSIN_STATUS);

    private final Material icon;
    private final String name;
    private final ClassStatus status;

    private boolean used = false;

    private BattleClass (String name, Material icon, ClassStatus status) {
        this.icon = icon;
        this.name = name;
        this.status = status;
    }

    public Material getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public ClassStatus getStatus() {
        return status;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
