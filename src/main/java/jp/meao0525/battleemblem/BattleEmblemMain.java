package jp.meao0525.battleemblem;

import jp.meao0525.battleemblem.beevent.*;
import jp.meao0525.battleemblem.begame.BeGame;
import jp.meao0525.battleemblem.beitem.BeItems;
import jp.meao0525.battleemblem.beplayer.BePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BattleEmblemMain extends JavaPlugin implements CommandExecutor {

    private BeGame game;

    @Override
    public void onEnable() {
        getLogger().info("plugin enabled");
        getCommand("be").setExecutor(this);

        getServer().getPluginManager().registerEvents(new DefaultGameEvent(), this);
        getServer().getPluginManager().registerEvents(new OpenSelectorEvent(), this);
        getServer().getPluginManager().registerEvents(new LoginEvent(), this);
        getServer().getPluginManager().registerEvents(new SelectLoadOutEvent(), this);
        getServer().getPluginManager().registerEvents(new BeDeathEvent(),this);
        getServer().getPluginManager().registerEvents(new LogoutEvent(), this);
        getServer().getPluginManager().registerEvents(new AttackEvent(),this);
    }

    @Override
    public void onDisable() {
        getLogger().info("plugin disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        switch (args[0]) {
            case "help" :
                //不正なコマンドじゃない?
                if (args.length > 1) { return false;}

                sender.sendMessage(ChatColor.GOLD + "===== Battle Emblem =====");
                sender.sendMessage("/be help - このプラグインのコマンド一覧");
                sender.sendMessage("/be score - 自分のスコアを表示");
                if (sender.isOp()) {
                    sender.sendMessage("/be score <プレイヤー名> - 指定したプレイヤーのスコアを表示");
                    sender.sendMessage("/be start - ゲームスタート");
                    sender.sendMessage("/be start <バトルクラス名> - 指定したバトルクラスに統一してスタート");
                    sender.sendMessage("/be end - ゲームを強制終了させる");
                    sender.sendMessage("/be give selector <プレイヤー名> - 指定したプレイヤーにロードアウトセレクターを渡す");
                }
                sender.sendMessage(ChatColor.GOLD + "======================");
                return true;

            case "score" : //スコアの表示
                //TODO:
                sender.sendMessage("プレイヤーのスコアを表示します");
                return true;

            case "start" : //ゲームを始める
                //あなたはOPですか?
                if (!(sender.isOp())) {
                    sender.sendMessage(ChatColor.DARK_RED + "このコマンドはOP権限がないと実行できません。残念だったな!!!");
                    return true;
                }
                //え？ゲーム中じゃね？
                if (BeGame.getPhase() != 0) {
                    sender.sendMessage(ChatColor.DARK_RED + "ただいまゲーム中です");
                    return true;
                }

                //ニューゲーム
                game = new BeGame();

                //引数はありますか
                if (args.length == 1) {
                    /*===ゲームスタート===*/
                    game.Start(null);

                } else if (args.length == 2){
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "全員" + ChatColor.AQUA + args[1] + ChatColor.RESET + "でゲームスタート");
                } else {
                    return false; //引数3つ以上とかありえない
                }
                return true;

            case "end" : //ゲームを強制終了する
                //あなたはOPですかぁ?
                if (!(sender.isOp())) {
                    sender.sendMessage(ChatColor.DARK_RED + "このコマンドはOP権限がないと実行できません。");
                    return true;
                }
                //ゲーム始まってないやんけ！
                if (BeGame.getPhase() == 0) {
                    sender.sendMessage(ChatColor.DARK_RED + "強制終了するゲームが見つかりません");
                    return true;
                }
                //ゲームを強制終了
                Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "ゲームを強制終了します");
                BeGame.End();
                return true;

            case "give" : //beアイテムを渡す
                Player player;
                if (args.length == 2) { //プレイヤー指定なし
                    if (sender instanceof Player) {
                        player = (Player) sender; //自分を指定
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "このコマンドはゲーム内から行ってください");
                        return true;
                    }
                } else if (args.length == 3) { //プレイヤー指定あり
                    player = Bukkit.getPlayerExact(args[2]);
                    if (player == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "そんな人いないよ...");
                        return true;
                    }
                } else { //それ以外の引数はあり得ない
                    return false;
                }
                //渡すのは何かな？
                switch (args[1]) {
                    case "selector" :
                        //指定したプレイヤーに渡すよ
                        player.getInventory().addItem(BeItems.LOADOUT_SELECTOR.toItemStack());
                        sender.sendMessage(player.getDisplayName() + "　にロードアウトセレクターを渡しました");
                        return true;

                    case "book" :
                        //TODO: バトルクラス一覧を渡す
                        return true;
                }
        }

        return false;
    }

}
