package win.oreo.schsurvival.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import win.oreo.schsurvival.Main;

import java.util.HashMap;

public class Util {
    public static HashMap<Player, Integer> playerTimeMap;
    private static boolean isStarted;
    private static BukkitTask t;
    private static BukkitTask et;
    public static int mainTick; //36000 : 30분


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
        for (Player player : playerTimeMap.keySet()) {
            int t = playerTimeMap.get(player);
            int m = t >= 60 ? t/60 : 0;
            int s = t >= 60 ? t - (60 * m) : t;
            Bukkit.broadcastMessage("Player : " + player.getName() + " Time : " + m + "min " + s + "sec");
        }
    }

    public void run() {
        t = Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(Main.class), () -> {
            mainTick += 1;

            if (mainTick >= 48000) {
                mainTick = -1;
                Bukkit.getScheduler().cancelTask(t.getTaskId());
                //TODO broadcast? end message
            }
        },0 ,1);
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
        int totalSec = mainTick / 20;
        int min = totalSec / 60;
        int sec = totalSec - (60 * min);

        return min + "분 " + sec + "초";
    }
}
