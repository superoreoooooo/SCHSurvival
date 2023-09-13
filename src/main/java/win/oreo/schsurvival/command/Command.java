package win.oreo.schsurvival.command;

import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import win.oreo.schsurvival.Main;
import win.oreo.schsurvival.util.Util;

import java.util.HashSet;
import java.util.Set;

public class Command implements CommandExecutor {

    private final Util util;
    private static Set<Player> showSet;
    public static boolean isSet;

    public Command() {
        this.util = new Util();
        init();
        isSet = false;
    }

    private void init() {
        if (showSet == null) {
            showSet = new HashSet<>();
        }
        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(Main.class), () -> {
            for (Player player : showSet) {
                player.sendMessage("Time now : " + Util.getTimeAsString());
            }
        }, 0, 20);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        String[] msg = new String[1];
        if (sender.hasPermission("administrators")) {
            if (sender instanceof Player player) {
                if (args.length > 0) {
                    switch (args[0]) {
                        case "tick" -> {
                            if (args.length > 1) {
                                Util.mainTick = Integer.parseInt(args[1]);
                                args[0] = String.valueOf(Util.mainTick);
                                player.sendMessage(Util.getConfigMessage("commands.set-tick", msg));
                            } else {
                                player.sendMessage(Util.getConfigMessage("commands.wrong-command", msg));
                            }
                        }
                        case "set" -> {
                            isSet = true;
                            Location location = player.getLocation().toBlockLocation();
                            location.getBlock().setType(Material.CHEST);
                            location.getBlock().setMetadata("data", new FixedMetadataValue(JavaPlugin.getPlugin(Main.class), "sch"));

                            BlockState state = location.getBlock().getState();
                            Chest chest = (Chest) state;

                            Util.inv = chest.getInventory();
                            Util.loc = location;
                            Util.block = location.getBlock();

                            ArmorStand checker = (ArmorStand) player.getWorld().spawnEntity(location.add(0.5, 1.5, 0.5), EntityType.ARMOR_STAND);
                            checker.setCustomName(Util.getConfigMessage("commands.box-name", msg));
                            checker.setGravity(false);
                            checker.setCanPickupItems(false);
                            checker.setVisible(false);
                            checker.setCanMove(false);
                            checker.setMarker(true);
                            checker.setCustomNameVisible(true);
                            player.sendMessage(Util.getConfigMessage("commands.set", msg));
                        }
                        case "teleport" -> {
                            int x = JavaPlugin.getPlugin(Main.class).config.getInt("settings.tpX");
                            int y = JavaPlugin.getPlugin(Main.class).config.getInt("settings.tpY");
                            int z = JavaPlugin.getPlugin(Main.class).config.getInt("settings.tpZ");

                            for (Player players : Bukkit.getOnlinePlayers()) {
                                if (players.getGameMode().equals(GameMode.SURVIVAL) || players.getGameMode().equals(GameMode.ADVENTURE)) {
                                    players.teleport(new Location(players.getWorld(), x, y, z));
                                    players.sendMessage(Util.getConfigMessage("commands.teleport", msg));
                                }
                            }
                        }
                        case "showtick" -> {
                            for (Player player1 : Util.playerTimeMap.keySet()) {
                                player.sendMessage(player1.getName() + " " + Util.playerTimeMap.get(player1));
                            }
                        }
                        case "showtime" -> {
                            if (!showSet.contains(player)) {
                                showSet.add(player);
                                player.sendMessage(Util.getConfigMessage("commands.show-add", msg));
                            } else {
                                showSet.remove(player);
                                player.sendMessage(Util.getConfigMessage("commands.show-remove", msg));
                            }
                        }
                        case "start" -> { //TODO 스타트시 타이틀 출력
                            if (isSet) {
                                util.start();
                                util.effect();
                                player.sendMessage(Util.getConfigMessage("commands.start", msg));
                                if (JavaPlugin.getPlugin(Main.class).config.getBoolean("settings.title-start")) {
                                    Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendTitle(Util.getConfigMessage("commands.start", msg), ""));
                                }
                            } else {
                                player.sendMessage("/meta set 먼저 치랫잖아 바부야");
                            }
                        }
                        case "clear" -> {
                            msg[0] = Util.getTimeAsString();
                            util.clear();
                            player.sendMessage(Util.getConfigMessage("commands.clear", msg));
                            player.sendMessage(Util.getConfigMessage("commands.time", msg));
                            if (JavaPlugin.getPlugin(Main.class).config.getBoolean("settings.title-clear")) {
                                Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendTitle(Util.getConfigMessage("commands.clear", msg), ""));
                            }
                        }
                        case "pause" -> {
                            msg[0] = Util.getTimeAsString();
                            util.pause();
                            player.sendMessage(Util.getConfigMessage("commands.pause", msg));
                            player.sendMessage(Util.getConfigMessage("commands.time", msg));
                            if (JavaPlugin.getPlugin(Main.class).config.getBoolean("settings.title-pause")) {
                                Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendTitle(Util.getConfigMessage("commands.pause", msg), ""));
                            }
                        }
                        case "resume" -> {
                            msg[0] = Util.getTimeAsString();
                            util.resume();
                            player.sendMessage(Util.getConfigMessage("commands.resume", msg));
                            player.sendMessage(Util.getConfigMessage("commands.time", msg));
                            if (JavaPlugin.getPlugin(Main.class).config.getBoolean("settings.title-resume")) {
                                Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendTitle(Util.getConfigMessage("commands.resume", msg), ""));
                            }
                        }
                        case "result", "show" -> {//1등만보이게 + 1등부터 나머지 채팅으로
                            msg[0] = Util.getTimeAsString();
                            player.sendMessage(Util.getConfigMessage("commands.result", msg));
                            if (JavaPlugin.getPlugin(Main.class).config.getBoolean("settings.title-result")) {
                                Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendMessage(Util.getConfigMessage("commands.result", msg), ""));
                            }
                            util.showResult();
                            for (Player player1 : Util.playerTimeMap.keySet()) {
                                //Bukkit.broadcastMessage(player1.getName() + " " + Util.playerTimeMap.get(player1));
                            }
                        }
                        case "stop" -> {
                            msg[0] = Util.getTimeAsString();
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
