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
            "剣を持って右クリックすると5秒間素早さが1段階あがる(CD:15s)";
    private final String PAGE3 = ChatColor.BLUE + "狂戦士 (BERSERKER)\n\n" +
            ChatColor.RESET + "HP     --- 40\n" +
            "素早さ --- 2\n" +
            "攻撃力 --- 8\n" +
            "防御力 --- 0\n\n" +
            "斧を持って右クリックすると次に攻撃した敵を3秒スタンさせる(CD:30s)";
    private final String PAGE4 = ChatColor.BLUE + "重鎧兵 (ARMOR KNIGHT)\n\n" +
            ChatColor.RESET + "HP     --- 60\n" +
            "素早さ --- 1\n" +
            "攻撃力 --- 6\n" +
            "防御力 --- 2\n\n" +
            "斧を持って地面を右クリックすると5メートル以内の敵を3秒スタンさせる(CD:30s)";
    private final String PAGE5 = ChatColor.BLUE + "勇者 (BRAVE HERO)\n\n" +
            ChatColor.RESET + "HP     --- 40\n" +
            "素早さ --- 2\n" +
            "攻撃力 --- 4+\n" +
            "防御力 --- 1\n\n" +
            "勇者は攻撃時、被ダメージの30%をダメージに追加する\n" +
            "剣を持って右クリックすると被ダメージの30%のHPを回復する(CD:30s)";
    private final String PAGE6 = ChatColor.BLUE + "狙撃手 (SNIPER)\n\n" +
            ChatColor.RESET + "HP     --- 40\n" +
            "素早さ --- 2\n" +
            "攻撃力 --- 3\n" +
            "防御力 --- 0\n\n" +
            "弓のチャージが最大の時、矢の与えるダメージは10\n" +
            "最大でない時のダメージは5";
    private final String PAGE7 = ChatColor.BLUE + "暗殺者 (ASSASSIN)\n\n" +
            ChatColor.RESET + "HP     --- 40\n" +
            "素早さ --- 2\n" +
            "攻撃力 --- 4+\n" +
            "防御力 --- 0\n\n" +
            "暗殺者は背後から攻撃した時、防御無視の10ダメージを与える\n" +
            "剣を持って右クリックすると10秒間透明化する(CD:10s)";

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
