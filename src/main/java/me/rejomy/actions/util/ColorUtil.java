package me.rejomy.actions.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class ColorUtil {

    // TODO: 1.16.5 hex colors support.
    public String apply(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
