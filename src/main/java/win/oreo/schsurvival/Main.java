package win.oreo.schsurvival;

import org.bukkit.plugin.java.JavaPlugin;
import win.oreo.schsurvival.command.Command;
import win.oreo.schsurvival.command.Tabcomplete;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("meta").setExecutor(new Command());
        getCommand("meta").setTabCompleter(new Tabcomplete());
    }

    @Override
    public void onDisable() {
    }
}
