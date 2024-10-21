package me.rejomy.actions.util;

import lombok.experimental.UtilityClass;
import me.rejomy.actions.ActionsAPI;
import me.rejomy.actions.util.condition.ConditionChecker;
import me.rejomy.actions.util.data.ActionData;
import me.rejomy.actions.util.data.EventData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

@UtilityClass
public class EventHandler {

    private final JavaPlugin plugin = ActionsAPI.INSTANCE.getPlugin();

    public <T extends Event> void handleEvent(T event, EventData eventData) {
        List<ActionData> actions = ActionsAPI.INSTANCE.getConfig().getActionsByEvent().get(eventData);

        // If no one actions there is this event.
        if (actions == null) {
            return;
        }

        for (ActionData action : actions) {
            // Check if conditions are valid.
            if (ConditionChecker.checkConditions(action.conditions(), event)) {
                // Execute commands
                CommandUtil.executeAll(action.commands(), event);
            }
        }
    }
}
