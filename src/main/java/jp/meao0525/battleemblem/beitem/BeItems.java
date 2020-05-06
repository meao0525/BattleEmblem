package jp.meao0525.battleemblem.beitem;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;

import static jp.meao0525.battleemblem.beitem.BeItemName.*;

public enum BeItems {
    /*=====ゲームアイテム=====*/
    LOADOUT_SELECTOR(Material.EMERALD, LOADOUT_SELECTOR_NAME, BeItemType.OTHERS),
    /*=====クラスアイテム=====*/
    MASTER_SWORD(Material.DIAMOND_SWORD, MASTER_SWORD_NAME, BeItemType.NORMAL),

    BERSERKER_AXE(Material.DIAMOND_AXE, BERSERKER_AXE_NAME, BeItemType.NORMAL),

    KNIGHT_AXE(Material.IRON_AXE, KNIGHT_AXE_NAME, BeItemType.NORMAL),
    KNIGHT_HELMET(Material.DIAMOND_HELMET, KNIGHT_ARMOR_NAME, BeItemType.NORMAL),
    KNIGHT_CHESTPLATE(Material.DIAMOND_CHESTPLATE, KNIGHT_ARMOR_NAME, BeItemType.NORMAL),
    KNIGHT_LEGGINGS(Material.DIAMOND_LEGGINGS, KNIGHT_ARMOR_NAME, BeItemType.NORMAL),
    KNIGHT_BOOTS(Material.DIAMOND_BOOTS, KNIGHT_ARMOR_NAME, BeItemType.NORMAL),

    BRAVE_SWORD(Material.IRON_SWORD, BRAVE_SWORD_NAME, BeItemType.NORMAL),
    BRAVE_HELMET(Material.GOLDEN_HELMET, BRAVE_ARMOR_NAME, BeItemType.NORMAL),
    BRAVE_CHESTPLATE(Material.GOLDEN_CHESTPLATE, BRAVE_ARMOR_NAME, BeItemType.NORMAL),
    BRAVE_LEGGINGS(Material.GOLDEN_LEGGINGS, BRAVE_ARMOR_NAME, BeItemType.NORMAL),
    BRAVE_BOOTS(Material.GOLDEN_BOOTS, BRAVE_ARMOR_NAME, BeItemType.NORMAL),

    SNIPER_BOW(Material.BOW, SNIPER_BOW_NAME, BeItemType.NORMAL),

    ASSASSIN_DAGGER(Material.IRON_SWORD, ASSASSIN_DAGGER_NAME, BeItemType.NORMAL),
    /*=====ウルトアイテム=====*/
    LIGHTNING_SWORD(Material.DIAMOND_SWORD, LIGHTNING_SWORD_NAME, BeItemType.ULTIMATE),

    LIGHTNING_AXE(Material.DIAMOND_AXE, LIGHTNING_AXE_NAME, BeItemType.ULTIMATE),

    COUNTER_ARMOR(Material.DIAMOND, COUNTER_ARMOR_NAME, BeItemType.ULTIMATE),
    COUNTER_HELMET(Material.DIAMOND_HELMET, COUNTER_ARMOR_NAME, BeItemType.ULTIMATE),
    COUNTER_CHESTPLATE(Material.DIAMOND_CHESTPLATE, COUNTER_ARMOR_NAME, BeItemType.ULTIMATE),
    COUNTER_LEGGINGS(Material.DIAMOND_LEGGINGS, COUNTER_ARMOR_NAME, BeItemType.ULTIMATE),
    COUNTER_BOOTS(Material.DIAMOND_BOOTS, COUNTER_ARMOR_NAME, BeItemType.ULTIMATE),

    INVINCIBLE_ARMOR(Material.BLAZE_POWDER, INVINCIBLE_ARMOR_NAME, BeItemType.ULTIMATE),
    INVINCIBLE_HELMET(Material.GOLDEN_HELMET, INVINCIBLE_ARMOR_NAME, BeItemType.ULTIMATE),
    INVINCIBLE_CHESTPLATE(Material.GOLDEN_CHESTPLATE, INVINCIBLE_ARMOR_NAME, BeItemType.ULTIMATE),
    INVINCIBLE_LEGGINGS(Material.GOLDEN_LEGGINGS, INVINCIBLE_ARMOR_NAME, BeItemType.ULTIMATE),
    INVINCIBLE_BOOTS(Material.GOLDEN_BOOTS, INVINCIBLE_ARMOR_NAME, BeItemType.ULTIMATE),

    LIGHTNING_BOW(Material.BOW, LIGHTNING_BOW_NAME, BeItemType.ULTIMATE),

    DEADLY_DAGGER(Material.IRON_SWORD, DEADLY_DAGGER_NAME, BeItemType.ULTIMATE);


    private final Material type;
    private final String name;
    private final BeItemType itemType;

    private BeItems(Material type, String name, BeItemType itemType) {
        this.type = type;
        this.name = name;
        this.itemType = itemType;
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

        if (itemType.equals(BeItemType.ULTIMATE)) {
            //ウルトアイテムにはエンチャントの光
            meta.addEnchant(Enchantment.DURABILITY, 1, false); //テキトーに耐久３つけるよ
        }

        beItem.setItemMeta(meta);
        return beItem;
    }

    public Material getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public BeItemType getItemType() {
        return itemType;
    }

    public static ArrayList<ItemStack> getUltItem() {
        ArrayList<ItemStack> itemSet = new ArrayList<>();
        //ウルトアイテムを取り出す
        for (BeItems item : BeItems.values()) {
            if (item.getItemType().equals(BeItemType.ULTIMATE)) {
                //タイプがウルトのときはハッシュセットに追加
                itemSet.add(item.toItemStack());
            }
        }
        return itemSet;
    }
}
