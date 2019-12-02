package jp.meao0525.battleemblem.begame;


import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BeItem {
    private final Material type;
    private final String name;

    public BeItem(Material material, String name) {
        this.type = material;
        this.name = name;
    }

    public ItemStack toItem() {
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
