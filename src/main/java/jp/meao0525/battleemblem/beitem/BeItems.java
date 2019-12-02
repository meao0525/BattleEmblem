package jp.meao0525.battleemblem.beitem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static jp.meao0525.battleemblem.beitem.BeItemName.*;

public enum BeItems {
    /*=====ゲームアイテム=====*/
    LOADOUT_SELECTOR(Material.EMERALD, LOADOUT_SELECTOR_NAME),
    /*=====クラスアイテム=====*/
    MASTER_SWORD(Material.DIAMOND_SWORD, MASTER_SWORD_NAME),
    BERSERKER_AXE(Material.DIAMOND_AXE, BERSERKER_AXE_NAME),
    KNIGHT_AXE(Material.DIAMOND_AXE, KNIGHT_AXE_NAME),
    BRAVE_SWORD(Material.IRON_SWORD, BRAVE_SWORD_NAME),
    SNIPER_BOW(Material.BOW, SNIPER_BOW_NAME),
    ASSASSIN_DAGGER(Material.IRON_SWORD, ASSASSIN_DAGGER_NAME);

    private final Material type;
    private final String name;

    private BeItems(Material type, String name) {
        this.type = type;
        this.name = name;
    }

    public ItemStack toItemStack() {
        ItemStack beItem = new ItemStack(getType());
        ItemMeta meta = beItem.getItemMeta();

        //オリジナル名前付きアイテムを作るよ
        meta.setDisplayName(getName());
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS,
                ItemFlag.HIDE_UNBREAKABLE);

        beItem.setItemMeta(meta);
        return beItem;
    }

    public Material getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
