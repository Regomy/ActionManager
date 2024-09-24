package me.rejomy.actions.hook;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
@UtilityClass
public class PAPIHook {

    public boolean enable = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

    public String setPlaceholders(Player player, String string) {
        return PlaceholderAPI.setPlaceholders(player, string);
    }
}
