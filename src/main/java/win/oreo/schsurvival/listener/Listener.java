package win.oreo.schsurvival.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import win.oreo.schsurvival.Main;
import win.oreo.schsurvival.util.Util;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getType().equals(Material.CHEST)) {
                if (e.getClickedBlock().hasMetadata("data")) {
                    if (e.getClickedBlock().getMetadata("data").get(0).asString().equals("sch")) {
                        String[] args = new String[1];
                        if (Util.boxTick >= JavaPlugin.getPlugin(Main.class).config.getInt("settings.box-time")) {
                            player.sendMessage(Util.getConfigMessage("interact.add", args));
                        } else if (Util.boxTick == -1) {
                            player.sendMessage(Util.getConfigMessage("interact.time-over", args));
                            e.setCancelled(true);
                        }
                        else {
                            int timeLeft = (JavaPlugin.getPlugin(Main.class).config.getInt("settings.box-time") - Util.boxTick) / 20;
                            int m = timeLeft / 60;
                            int s = timeLeft - (60 * m);
                            args[0] = m + "분 " + s + "초";
                            player.sendMessage(Util.getConfigMessage("interact.locked", args));
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    public void run() {
        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(Main.class), () -> {

        },0 ,1);
    }
}
