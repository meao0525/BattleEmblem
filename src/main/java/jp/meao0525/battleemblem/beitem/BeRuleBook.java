package jp.meao0525.battleemblem.beitem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class BeRuleBook {

    private BookMeta meta;

    private final String TITLE = "Battle Emblem ルールブック";
    private final String AUTHOR = "MEAO0525";

    private final String PAGE1 = ChatColor.GOLD + "===Battle Emblem！===\n\n" +
            ChatColor.RESET +"ロードアウトセレクターを右クリックして、バトルクラスを選択しよう\n" +
            "（それぞれのバトルクラスの能力は次ページ以降）\n\n" +
            "/be score\n自分のスコアを確認できます";

    private final String PAGE2 = ChatColor.BLUE + "剣聖 (SWORD MASTER)\n\n" +
            ChatColor.RESET + "HP     --- 40\n" +
            "素早さ --- 2\n" +
            "攻撃力 --- 6\n" +
            "防御力 --- 0\n\n" +
            "能力： 15秒間素早さ・ジャンプ上昇(CD:15s)\n" +
            ChatColor.GOLD + "雷の剣: 敵に雷を落とし、防御無視の30ダメージ";
    private final String PAGE3 = ChatColor.BLUE + "狂戦士 (BERSERKER)\n\n" +
            ChatColor.RESET + "HP     --- 40\n" +
            "素早さ --- 2\n" +
            "攻撃力 --- 8\n" +
            "防御力 --- 0\n\n" +
            "能力: 次の攻撃にスタン効果を付与(CD:30s)\n" +
            ChatColor.GOLD + "雷の斧: 敵に雷を落とし、防御無視の30ダメージ";
    private final String PAGE4 = ChatColor.BLUE + "重鎧兵 (ARMOR KNIGHT)\n\n" +
            ChatColor.RESET + "HP     --- 60\n" +
            "素早さ --- 1\n" +
            "攻撃力 --- 6\n" +
            "防御力 --- 2\n\n" +
            "能力: 地面に向けて発動することで半径5メートル以内の敵にスタン効果を付与(CD:30s)\n" +
            ChatColor.DARK_AQUA + "反撃: 20秒間無敵+素早さ上昇";
    private final String PAGE5 = ChatColor.BLUE + "勇者 (BRAVE HERO)\n\n" +
            ChatColor.RESET + "HP     --- 40\n" +
            "素早さ --- 2\n" +
            "攻撃力 --- 4+\n" +
            "防御力 --- 1\n" +
            "減少したHPの30%ダメージ追加\n\n" +
            "能力: 減少したHPの30%回復(CD:30s)\n" +
            ChatColor.RED + "英雄: 30秒間無敵";
    private final String PAGE6 = ChatColor.BLUE + "狙撃手 (SNIPER)\n\n" +
            ChatColor.RESET + "HP     --- 40\n" +
            "素早さ --- 2\n" +
            "攻撃力 --- 3\n" +
            "防御力 --- 0\n\n" +
            "能力: 矢を放ち、相手に5ダメージ(最大チャージ時は10ダメージ)\n" +
            ChatColor.GOLD + "雷の弓: 敵に雷を落とし、防御無視の30ダメージ(3回)";
    private final String PAGE7 = ChatColor.BLUE + "暗殺者 (ASSASSIN)\n\n" +
            ChatColor.RESET + "HP     --- 40\n" +
            "素早さ --- 2\n" +
            "攻撃力 --- 4+\n" +
            "防御力 --- 0\n" +
            "背後からの攻撃は防御無視の10ダメージ\n\n" +
            "能力: 10秒間透明化+移動上昇(CD:10s)\n" +
            ChatColor.DARK_PURPLE + "滅殺: 背後からの攻撃で確殺";

    //コンストラクちゃん
    public BeRuleBook() { }

    public ItemStack toItemStack() {
        //本を作るよ
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        meta = (BookMeta) item.getItemMeta();
        //中身を書き込むよ
        meta.setTitle(TITLE);
        meta.setAuthor(AUTHOR);
        meta.addPage(PAGE1);
        meta.addPage(PAGE2);
        meta.addPage(PAGE3);
        meta.addPage(PAGE4);
        meta.addPage(PAGE5);
        meta.addPage(PAGE6);
        meta.addPage(PAGE7);


        //本にセット
        item.setItemMeta(meta);

        return item;
    }
}
