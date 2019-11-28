package jp.meao0525.battleemblem;

import jp.meao0525.battleemblem.beevent.OpenSelectorEvent;
import jp.meao0525.battleemblem.beevent.SelectLoadOutEvent;
import jp.meao0525.battleemblem.begame.BeGame;
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

    BeGame game = new BeGame();
    ArrayList<Player> bePlayerList = new ArrayList<>();

    @Override
    public void onEnable() {
        getLogger().info("plugin enabled");
        getCommand("be").setExecutor(this);

        getServer().getPluginManager().registerEvents(new OpenSelectorEvent(), this);
        getServer().getPluginManager().registerEvents(new SelectLoadOutEvent(), this);
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
                    sender.sendMessage("/be give <アイテム名> <プレイヤー名> <個数> - 指定したアイテムを渡す");
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
                if (game.getPhase() != 0) {
                    sender.sendMessage(ChatColor.DARK_RED + "ただいまゲーム中です");
                    return true;
                }

                //プレイヤーリストの作成
                bePlayerList = game.createPlayerList();

                if (bePlayerList.size() == 0) { sender.sendMessage("誰もいないね"); }

                if (args.length == 1) {
                    //TODO: ロードアウトセレクターを与える
                    //クラスを選択してないプレイヤーにランダムクラスを与える
                    for (Player p : bePlayerList) {
                        if ((p.getPlayerListHeader() == null)||(p.getPlayerListHeader().isEmpty())) {
                            BePlayer bp = new BePlayer(p);
                            bp.setBattleClass(game.getRandomClass());
                        }
                    }
                    /*===ゲームスタート===*/
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "ゲームスタート");
                    game.Start(bePlayerList);

                } else if (args.length == 2){
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[BattleEmblem]" + ChatColor.RESET + "全員" + ChatColor.AQUA + args[1] + ChatColor.RESET + "でゲームスタート");
                } else {
                    return false; //引数3つ以上とかありえない
                }
                return true;

            case "give" : //beアイテムを渡す
                //TODO: helpの通りにコマンド動かすよ
        }

        return false;
    }
}
