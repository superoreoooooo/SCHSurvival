package win.oreo.schsurvival.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import win.oreo.schsurvival.util.Util;

import java.util.HashSet;
import java.util.Set;

public class Command implements CommandExecutor {

    private final Util util;
    private static Set<Player> showSet;

    public Command() {
        this.util = new Util();
    }

    private void init() {
        if (showSet == null) {
            showSet = new HashSet<>();
        }
        for (Player player : showSet) {
            player.sendMessage("Time now : " + Util.timeNow);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender.hasPermission("administrators")) {
            if (sender instanceof Player player) {
                if (args.length > 0) {
                    switch (args[0]) {
                        case "showTime" -> {
                            if (!showSet.contains(player)) {
                                showSet.add(player);
                                player.sendMessage("added!");
                            } else {
                                showSet.remove(player);
                                player.sendMessage("removed!");
                            }
                        }
                        case "start" -> {
                            util.start();
                            player.sendMessage("Started!");
                        }
                        case "clear" -> {
                            player.sendMessage("elapsed time : " + Util.timeNow);
                            util.clear();
                            player.sendMessage("Cleared!");
                        }
                        case "pause" -> {
                            player.sendMessage("elapsed time : " + Util.timeNow);
                            util.pause();
                            player.sendMessage("Paused!");
                        }
                        case "resume" -> {
                            player.sendMessage("time now : " + Util.timeNow);
                            util.resume();
                            player.sendMessage("Resumed!");
                        }
                        case "result", "show" -> {
                            player.sendMessage("total Time : " + Util.timeNow);
                            util.showResult();
                            player.sendMessage("Printed Result!");
                        }
                    }
                }
            }
        }
        return false;
    }
}
