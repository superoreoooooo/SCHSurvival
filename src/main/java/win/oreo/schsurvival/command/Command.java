package win.oreo.schsurvival.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import win.oreo.schsurvival.Main;
import win.oreo.schsurvival.util.Util;

import java.util.HashSet;
import java.util.Set;

public class Command implements CommandExecutor {

    private final Util util;
    private static Set<Player> showSet;

    public Command() {
        this.util = new Util();
        init();
    }

    private void init() {
        if (showSet == null) {
            showSet = new HashSet<>();
        }
        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(Main.class), () -> {
            for (Player player : showSet) {
                player.sendMessage("Time now : " + Util.timeNow);
            }
        },0, 20);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        String[] msg = new String[1];
        if (sender.hasPermission("administrators")) {
            if (sender instanceof Player player) {
                if (args.length > 0) {
                    switch (args[0]) {
                        case "showTime", "showtime" -> {
                            if (!showSet.contains(player)) {
                                showSet.add(player);
                                player.sendMessage(Util.getConfigMessage("commands.show-add", msg));
                            } else {
                                showSet.remove(player);
                                player.sendMessage(Util.getConfigMessage("commands.show-remove", msg));
                            }
                        }
                        case "start" -> { //TODO 스타트시 타이틀 출력
                            util.start();
                            player.sendMessage(Util.getConfigMessage("commands.start", msg));
                            if (JavaPlugin.getPlugin(Main.class).config.getBoolean("settings.title-start")) {
                                Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendTitle(Util.getConfigMessage("commands.start", msg), ""));
                            }
                        }
                        case "clear" -> {
                            msg[0] = String.valueOf(Util.timeNow);
                            util.clear();
                            player.sendMessage(Util.getConfigMessage("commands.clear", msg));
                            player.sendMessage(Util.getConfigMessage("commands.time", msg));
                            if (JavaPlugin.getPlugin(Main.class).config.getBoolean("settings.title-clear")) {
                                Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendTitle(Util.getConfigMessage("commands.clear", msg), ""));
                            }
                        }
                        case "pause" -> {
                            msg[0] = String.valueOf(Util.timeNow);
                            util.pause();
                            player.sendMessage(Util.getConfigMessage("commands.pause", msg));
                            player.sendMessage(Util.getConfigMessage("commands.time", msg));
                            if (JavaPlugin.getPlugin(Main.class).config.getBoolean("settings.title-pause")) {
                                Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendTitle(Util.getConfigMessage("commands.pause", msg), ""));
                            }
                        }
                        case "resume" -> {
                            msg[0] = String.valueOf(Util.timeNow);
                            util.resume();
                            player.sendMessage(Util.getConfigMessage("commands.resume", msg));
                            player.sendMessage(Util.getConfigMessage("commands.time", msg));
                            if (JavaPlugin.getPlugin(Main.class).config.getBoolean("settings.title-resume")) {
                                Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendTitle(Util.getConfigMessage("commands.resume", msg), ""));
                            }
                        }
                        case "result", "show" -> {//1등만보이게 + 1등부터 나머지 채팅으로
                            msg[0] = String.valueOf(Util.timeNow);
                            util.showResult();
                            player.sendMessage(Util.getConfigMessage("commands.result", msg));
                            player.sendMessage(Util.getConfigMessage("commands.time", msg));
                            if (JavaPlugin.getPlugin(Main.class).config.getBoolean("settings.title-result")) {
                                Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendTitle(Util.getConfigMessage("commands.result", msg), ""));
                            }
                        }
                        case "stop" -> {
                            msg[0] = String.valueOf(Util.timeNow);
                            util.stop();
                            player.sendMessage(Util.getConfigMessage("commands.stop", msg));
                            player.sendMessage(Util.getConfigMessage("commands.time", msg));
                            if (JavaPlugin.getPlugin(Main.class).config.getBoolean("settings.title-stop")) {
                                Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendTitle(Util.getConfigMessage("commands.stop", msg), ""));
                            }
                        }
                        default -> {
                            player.sendMessage(Util.getConfigMessage("commands.wrong-command", msg));
                        }
                    }
                }
            }
        }
        return false;
    }
}
