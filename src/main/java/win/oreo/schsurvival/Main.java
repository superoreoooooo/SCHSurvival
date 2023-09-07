package win.oreo.schsurvival;

import org.bukkit.plugin.java.JavaPlugin;
import win.oreo.schsurvival.command.Command;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("meta").setExecutor(new Command());
    }

    @Override
    public void onDisable() {
    }
}
