package jp.meao0525.battleemblem.battleclass;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum BattleClass {
    Sword_Master("剣聖", Material.DIAMOND_SWORD, ClassStatus.SWORD_MASTER_STATUS),
    BERSERKER("狂戦士", Material.DIAMOND_AXE, ClassStatus.BERSERKER_STATUS),
    ARMOR_KNIGHT("重鎧兵", Material.DIAMOND_CHESTPLATE, ClassStatus.ARMOR_KNIGHT_STATUS),
    BRAVE_HERO("勇者", Material.GOLDEN_CHESTPLATE, ClassStatus.BRAVE_HERO_STATUS),
    SNIPER("狙撃手", Material.BOW, ClassStatus.SNIPER_STATUS),
    ASSASSIN("暗殺者", Material.IRON_SWORD, ClassStatus.ASSASSIN_STATUS);

    private final Material icon;
    private final String name;
    private final ClassStatus status;

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
}
