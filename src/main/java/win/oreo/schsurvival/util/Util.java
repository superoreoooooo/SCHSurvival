package win.oreo.schsurvival.util;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import win.oreo.schsurvival.Main;

import java.util.HashMap;

public class Util {
    public static HashMap<Player, Integer> playerTimeMap;
    public static int timeNow;
    private static boolean isStarted;
    private static BukkitTask t;

    public Util() {
        isStarted = false;
    }

    public void start() {
        if (!isStarted) {
            timeNow = 0;
            isStarted = true;
            if (playerTimeMap == null) {
                playerTimeMap = new HashMap<>();
            }
            timer();
        }
    }

    public void clear() {
        playerTimeMap.clear();
        isStarted = false;
        timeNow = 0;
        try {
            Bukkit.getScheduler().cancelTask(t.getTaskId());
        } catch (Exception ignored) {

        }
    }

    public void stop() {
        isStarted = false;
        timeNow = 0;
        try {
            Bukkit.getScheduler().cancelTask(t.getTaskId());
        } catch (Exception ignored) {

        }
    }

    public void pause() {
        if (!isStarted) return;
        isStarted = false;
        Bukkit.getScheduler().cancelTask(t.getTaskId());
    }

    public void resume() {
        if (isStarted) return;
        isStarted = true;
        timer();
    }

    private void timer() {
        t = Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(Main.class), () -> {
            timeNow += 1;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                    if (!playerTimeMap.containsKey(player)) {
                        if (player.getInventory().contains(Material.DIAMOND)) {
                            playerTimeMap.put(player, timeNow);
                            int t = playerTimeMap.get(player);
                            int m = t >= 60 ? t/60 : 0;
                            int s = t >= 60 ? t - (60 * m) : t;
                            Bukkit.broadcastMessage("Player : " + player.getName() + " Time : " + m + "min " + s + "sec");
                        }
                    }
                }
            }
        }, 0, 20);
    }

    public void showResult() {
        for (Player player : playerTimeMap.keySet()) {
            int t = playerTimeMap.get(player);
            int m = t >= 60 ? t/60 : 0;
            int s = t >= 60 ? t - (60 * m) : t;
            Bukkit.broadcastMessage("Player : " + player.getName() + " Time : " + m + "min " + s + "sec");
        }
    }
}
