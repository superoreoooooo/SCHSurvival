package win.oreo.schsurvival.util;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import win.oreo.schsurvival.Main;

import java.util.*;

public class Util {
    public static HashMap<Player, Integer> playerTimeMap;
    private static boolean isStarted;
    private static BukkitTask t;
    private static BukkitTask et;
    public static int mainTick; //36000 : 30분
    public static Inventory inv;
    public static Location loc;
    public static Block block;


    public Util() {
        isStarted = false;
    }

    public void start() {
        if (!isStarted) {
            mainTick = 0;
            isStarted = true;
            if (playerTimeMap == null) {
                playerTimeMap = new HashMap<>();
            }
            run();
        }
    }

    public void clear() {
        if (playerTimeMap != null) {
            playerTimeMap.clear();
        }
        isStarted = false;
        mainTick = 0;
        try {
            Bukkit.getScheduler().cancelTask(t.getTaskId());
            Bukkit.getScheduler().cancelTask(et.getTaskId());
        } catch (Exception ignored) {

        }
    }

    public void stop() {
        isStarted = false;
        mainTick = 0;
        try {
            Bukkit.getScheduler().cancelTask(t.getTaskId());
            Bukkit.getScheduler().cancelTask(et.getTaskId());
        } catch (Exception ignored) {

        }
    }

    public void pause() {
        if (!isStarted) return;
        isStarted = false;
        Bukkit.getScheduler().cancelTask(t.getTaskId());
        Bukkit.getScheduler().cancelTask(et.getTaskId());
    }

    public void resume() {
        if (isStarted) return;
        isStarted = true;
        run();
        effect();
    }

    public void showResult() {
        if (playerTimeMap == null) return;
        List<Map.Entry<Player, Integer>> entryList = new LinkedList<>(playerTimeMap.entrySet());
        entryList.sort(Map.Entry.comparingByValue());

        for (Map.Entry<Player, Integer> entry : entryList) {
            Player player = entry.getKey();
            int t = entry.getValue();

            String[] args = new String[2];
            args[0] = player.getName();
            args[1] = getTimeAsString(t);
            Bukkit.broadcastMessage(Util.getConfigMessage("commands.result-p", args));
        }
    }

    public void checkInv() {

    }

    public void run() {
        t = Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(Main.class), () -> {
            mainTick += 1;

            if (mainTick >= JavaPlugin.getPlugin(Main.class).config.getInt("settings.game-time")) {
                mainTick = -1;
                Bukkit.getScheduler().cancelTask(t.getTaskId());
                Bukkit.broadcastMessage(Util.getConfigMessage("commands.stop", new String[]{}));

                reset();
            }
        },0 ,1);
    }

    private void reset() {
        block.setType(Material.AIR);
        loc.getWorld().getEntitiesByClass(ArmorStand.class).forEach(Entity::remove);
    }

    public void effect() {
        et = Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(Main.class), () -> {
            if (isStarted) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                        if (player.getInventory().contains(Material.DIAMOND)) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10000, 1));
                        } else {
                            player.removePotionEffect(PotionEffectType.GLOWING);
                        }
                    }
                }
            }
        },0 ,1);
    }

    public static String getConfigMessage(String path, String[] args) {
        FileConfiguration config = Main.getPlugin(Main.class).config;
        String text = config.getString(path);
        String prefix = config.getString("prefix");
        if (text == null) {
            return ChatColor.RED +"ERROR";
        }

        boolean open = false;
        StringBuilder chars = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c == '%') {
                if (open) {
                    final char[] CHARACTERS = chars.toString().toCharArray();
                    if (CHARACTERS[0] == 'a' && CHARACTERS[1] == 'r' && CHARACTERS[2] == 'g') {
                        final int ARG = Integer.parseInt(String.valueOf(CHARACTERS[3]));

                        text = text.replace(chars.toString(), args[ARG]);

                        chars = new StringBuilder();
                    }
                    open = false;
                } else {
                    open = true;
                }
                continue;
            }

            if (open) {
                chars.append(c);
            }
        }

        return Color.format(prefix + " " + text.replace("%", ""));
    }

    public static String getTimeAsString() {
        if (mainTick == -1) {
            return "게임이 종료되었습니다.";
        }

        int totalSec = mainTick / 20;
        int min = totalSec / 60;
        int sec = totalSec - (60 * min);

        return min + "분 " + sec + "초";
    }

    public static String getTimeAsString(int time) {
        int totalSec = time / 20;
        int min = totalSec / 60;
        int sec = totalSec - (60 * min);

        return min + "분 " + sec + "초";
    }
}
