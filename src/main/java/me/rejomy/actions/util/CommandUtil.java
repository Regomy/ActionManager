package me.rejomy.actions.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import java.util.List;

@UtilityClass
public class CommandUtil {

    public void execute(String command, Object... objects) {
        if (objects.length > 1) {
            for (int index = 1; index < objects.length; index+=2) {
                command = command.replace("$" + objects[index - 1], String.valueOf(objects[index]));
            }
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public <T extends Event> void executeAll(List<String> commands, T event) {
        // TODO: Add choice and settings for damager or entity as example.
        String killer = (String) ReflectionUtil.getObject("damager#getName()", event, false);
        String player = (String) ReflectionUtil.getObject("getEntity()#getName()", event, false);

        // Event may not contains player as getEntity, for example PlayerMoveEvent has getPlayer()
        if (player == null) player = (String) ReflectionUtil.getObject("getPlayer()#getName()", event, false);

        for (String command : commands) {
            execute(command, "player", player, "killer", killer);
        }
    }
}
