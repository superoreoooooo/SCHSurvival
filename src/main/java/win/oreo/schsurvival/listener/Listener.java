package win.oreo.schsurvival.listener;

import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import win.oreo.schsurvival.Main;
import win.oreo.schsurvival.util.Util;

import java.util.HashSet;
import java.util.Set;

public class Listener implements org.bukkit.event.Listener {
    private static Set<Player> playerSet;

    public Listener() {
        playerSet = new HashSet<>();
    }

    @EventHandler
    public void onGet(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player player) {
            if (e.getItem().getItemStack().getType().equals(Material.DIAMOND)) {
                String[] args = new String[]{player.getName(), String.valueOf(player.getLocation().getBlockX()), String.valueOf(player.getLocation().getBlockY()), String.valueOf(player.getLocation().getBlockZ())};
                Bukkit.broadcastMessage(Util.getConfigMessage("interact.get-diamond", args));
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        String[] args = new String[1];
        Player player = e.getPlayer();

        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getType().equals(Material.CHEST)) {
                if (e.getClickedBlock().hasMetadata("data")) {
                    if (e.getClickedBlock().getMetadata("data").get(0).asString().equals("sch")) {
                        if (playerSet.contains(player)) {
                            e.setCancelled(true);
                            return;
                        }
                        if (Util.mainTick >= JavaPlugin.getPlugin(Main.class).config.getInt("settings.box-time")) {
                            player.sendMessage(Util.getConfigMessage("interact.add", args));
                        } else if (Util.mainTick == -1) {
                            player.sendMessage(Util.getConfigMessage("interact.time-over", args));
                            e.setCancelled(true);
                        } else {
                            int timeLeft = (JavaPlugin.getPlugin(Main.class).config.getInt("settings.box-time") - Util.mainTick) / 20;
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

    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        if (e.getInventory().equals(Util.inv)) {
            Player player = (Player) e.getPlayer();
            if (playerSet.contains(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        if (e.getBlock().getType().equals(Material.CHEST)) {
            if (e.getBlock().hasMetadata("data")) {
                if (e.getBlock().getMetadata("data").get(0).asString().equals("sch")) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onFight(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            e.setDamage(e.getDamage() * 2);
        }
    }

    @EventHandler
    public void onClickItem(InventoryClickEvent e) {
        if (Util.inv == null) return;
        String[] args = new String[1];
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().equals(Util.inv)) {
            Player player = (Player) e.getWhoClicked();
            if (e.getCursor() == null) return;
            ItemStack is = e.getCursor();
            if (e.getCursor().getType().equals(Material.DIAMOND)) {
                e.getCursor().setAmount(0);
                args[0] = Util.getTimeAsString();
                player.sendMessage(Util.getConfigMessage("interact.put", args));
                Util.playerTimeMap.put(player, Util.mainTick);
                playerSet.add(player);
                player.closeInventory();
            } else {
                player.sendMessage(Util.getConfigMessage("interact.put-error", args));
                e.setCancelled(true);
                player.closeInventory();
                player.getInventory().addItem(is);
            }
        }
        else {
            if (e.getView().getTopInventory().equals(Util.inv) && e.isShiftClick()) {
                e.getWhoClicked().sendMessage(Util.getConfigMessage("interact.clk-error", new String[]{}));
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();
            }
        }
    }

    @EventHandler
    public void onClickItem(InventoryDragEvent e) {
        if (Util.inv == null) return;
        String[] args = new String[1];
        if (e.getInventory() == null) return;
        if (e.getInventory().equals(Util.inv)) {
            Player player = (Player) e.getWhoClicked();
            player.sendMessage(Util.getConfigMessage("interact.drg-error", args));
            e.setCancelled(true);
            /**
            if (e.getCursor() == null) return;
            ItemStack is = e.getCursor();
            if (e.getCursor().getType().equals(Material.DIAMOND)) {
                e.getCursor().setAmount(0);
                args[0] = Util.getTimeAsString();
                player.sendMessage(Util.getConfigMessage("interact.put", args));
                Util.playerTimeMap.put(player, Util.mainTick);
                playerSet.add(player);
                player.closeInventory();
            } else {
                player.sendMessage(Util.getConfigMessage("interact.put-error", args));
                e.setCancelled(true);
                player.getInventory().addItem(is);
                player.closeInventory();
            } **/
        }
    }
}