package win.oreo.schsurvival.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Tabcomplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("start");
            completions.add("clear");
            completions.add("pause");
            completions.add("resume");
            completions.add("result");
            completions.add("showtime");
            completions.add("set");
            completions.add("tick");
            completions.add("teleport");
            completions.add("show");
            completions.add("showtick");

            return completions;
        }
        return null;
    }
}
