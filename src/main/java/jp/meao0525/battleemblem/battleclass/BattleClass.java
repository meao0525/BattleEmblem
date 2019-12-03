package jp.meao0525.battleemblem.battleclass;

import jp.meao0525.battleemblem.beitem.BeItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static jp.meao0525.battleemblem.battleclass.BattleClassName.*;

public enum BattleClass {
    SWORD_MASTER(SWORD_MASTER_NAME, Material.DIAMOND_SWORD, ClassStatus.SWORD_MASTER_STATUS, BeItems.MASTER_SWORD),
    BERSERKER(BERSERKER_NAME, Material.DIAMOND_AXE, ClassStatus.BERSERKER_STATUS, BeItems.BERSERKER_AXE),
    ARMOR_KNIGHT(ARMOR_KNIGHT_NAME, Material.DIAMOND_CHESTPLATE, ClassStatus.ARMOR_KNIGHT_STATUS, BeItems.KNIGHT_AXE),
    BRAVE_HERO(BRAVE_HERO_NAME, Material.GOLDEN_CHESTPLATE, ClassStatus.BRAVE_HERO_STATUS, BeItems.BRAVE_SWORD),
    SNIPER(SNIPER_NAME, Material.BOW, ClassStatus.SNIPER_STATUS, BeItems.SNIPER_BOW),
    ASSASSIN(ASSASSIN_NAME, Material.IRON_SWORD, ClassStatus.ASSASSIN_STATUS, BeItems.ASSASSIN_DAGGER);

    private final Material icon;
    private final String name;
    private final ClassStatus status;
    private final BeItems item;

    private boolean used = false;

    private BattleClass (String name, Material icon, ClassStatus status, BeItems item) {
        this.icon = icon;
        this.name = name;
        this.status = status;
        this.item = item;
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

    public BeItems getItem() { return item; }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
