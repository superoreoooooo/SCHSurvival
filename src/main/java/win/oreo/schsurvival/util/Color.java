package win.oreo.schsurvival.util;

import org.bukkit.ChatColor;

public class Color {
    public static String format(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
