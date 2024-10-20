package me.rejomy.actions.util;

import lombok.experimental.UtilityClass;
import me.rejomy.actions.ActionsAPI;
import me.rejomy.actions.util.command.Command;
import me.rejomy.actions.util.condition.ConditionChecker;
import me.rejomy.actions.util.data.ConditionData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

    public <T extends Event> void executeAll(List<Command> commands, T event) {
        // TODO: Add choice and settings for damager or entity as example.
        String killer = (String) ReflectionUtil.getObject("getDamager()#getName()", event, false);
        String player = (String) ReflectionUtil.getObject("getEntity()#getName()", event, false);

        // Event may not contains player as getEntity, for example PlayerMoveEvent has getPlayer()
        if (player == null) player = (String) ReflectionUtil.getObject("getPlayer()#getName()", event, false);

        for (Command commandObject : commands) {
            String command = commandObject.getCommand();
            String[] commandParts = command.split(" ");

            if (commandObject.isIterate()) {
                for (Player target : Bukkit.getOnlinePlayers()) {
                    boolean valid = true;

                    for (String conditionName : commandObject.getConditions()) {
                        List<ConditionData> conditions = ActionsAPI.INSTANCE.getConfig().getConditions().get(conditionName);

                        if (conditions != null) {
                            if (!ConditionChecker.checkConditions(conditions, event, target)) {
                                valid = false;
                                break;
                            }
                        }
                    }

                    if (valid)
                        execute(command, "target", target.getName(), "player", player, "killer", killer);
                }
            } else {
                execute(command, "player", player, "killer", killer);
            }
        }
    }
}
