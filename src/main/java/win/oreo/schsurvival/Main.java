package win.oreo.schsurvival;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import win.oreo.schsurvival.command.Command;
import win.oreo.schsurvival.command.Tabcomplete;
import win.oreo.schsurvival.util.Color;

public final class Main extends JavaPlugin {
    public FileConfiguration config;

    @Override
    public void onEnable() {
        config = this.getConfig();
        getCommand("meta").setExecutor(new Command());
        getCommand("meta").setTabCompleter(new Tabcomplete());
    }

    @Override
    public void onDisable() {
    }
}
