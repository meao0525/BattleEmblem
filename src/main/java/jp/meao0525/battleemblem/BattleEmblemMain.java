package jp.meao0525.battleemblem;

import jp.meao0525.battleemblem.begame.BeGame;
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

    @Override
    public void onEnable() {
        getLogger().info("plugin enabled");
        getCommand("be").setExecutor(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("plugin disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length > 1) { return false; }

        switch (args[0]) {
            case "":
            case "help" : //helpの表示
                sender.sendMessage(ChatColor.GOLD + "=== Battle Emblem ===");
                sender.sendMessage("/be help - このプラグインのコマンド一覧");
                sender.sendMessage("/be score - 自分のスコアを表示");
                if (sender.isOp()) {
                    sender.sendMessage("/be score <プレイヤー名> - 指定したプレイヤーのスコアを表示");
                    sender.sendMessage("/be start - ゲームスタート");
                    sender.sendMessage("/be start <バトルクラス名> - 指定したバトルクラスに統一してスタート");
                }
                sender.sendMessage(ChatColor.GOLD + "=====================");
                return true;

            case "score" : //スコアの表示
                //TODO:
                return true;

            case "start" : //ゲームを始める
                BeGame bg = new BeGame();

                if (args.length == 1) {
                    //TODO: ロードアウトセレクターを与える
                    //TODO: クラスを選択してないプレイヤーにランダムクラスを与える
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "ゲームスタート");
                    bg.Start();
                }


                return true;
        }

        return false;
    }
}
