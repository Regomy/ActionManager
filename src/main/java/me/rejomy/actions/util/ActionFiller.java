package me.rejomy.actions.util;

import lombok.experimental.UtilityClass;
import me.rejomy.actions.ActionsAPI;
import me.rejomy.actions.util.command.Command;
import me.rejomy.actions.util.condition.ConditionParser;
import me.rejomy.actions.util.data.ActionData;
import me.rejomy.actions.util.data.ConditionData;
import me.rejomy.actions.util.data.EventData;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@UtilityClass
public class ActionFiller {

    public ActionData newActionFromConfig(List<String> cfgActivator, List<String> condition, List<String> commands) {
        // Parse activator to Event.
        List<EventData> activators = getEventsFromActivator(cfgActivator);

        List<ConditionData> conditions = parseConditions(condition);

        // Do color for commands
        commands.replaceAll(ColorUtil::apply);

        List<Command> parsedCommands = parseCommands(commands);

        // Validate lists
        if (activators.isEmpty()) {
            Logger.error("Activators for some action is empty.",
                    "Try to find this action in config, if is this mistake, report to github.",
                    "Skipping some action...");
            return null;
        }

        if (commands.isEmpty()) {
            Logger.error("Commands for some action is empty.",
                    "Try to find this action in config, if is this mistake, report to github.",
                    "Skipping some action...");
            return null;
        }
        //

        return new ActionData(activators, conditions, parsedCommands);
    }

    /**
     * Here we check condition and add this to ActionsData list.
     * @param cfgConditions Condition at action.condition
     * @return List of valid conditions
     */
    public List<ConditionData> parseConditions(List<String> cfgConditions) {
        List<ConditionData> conditions = new ArrayList<>();

        // If condition section does not exist, return empty conditions.
        if (cfgConditions == null) {
            return conditions;
        }

        for (String condition : cfgConditions) {
            String[] parts = condition.split("\\s");

            // If this condition from conditions list, we should get this from map
            if (condition.startsWith("$") && parts.length == 1) {
                String conditionName = condition.substring(1);
                List<ConditionData> conditionsData =
                        ActionsAPI.INSTANCE.getConfig().getConditions().get(conditionName);

                if (conditionsData != null) {
                    conditions.addAll(conditionsData);
                }
            }
            // Otherwise load this as new condition.
            else {
                ConditionData conditionData = ConditionParser.parseCondition(condition);
                conditions.add(conditionData);
            }
        }

        return conditions;
    }

    public List<Command> parseCommands(List<String> textCommands) {
        return textCommands.stream().map(Command::new).toList();
    }

    public List<EventData> getEventsFromActivator(List<String> activators) {
        List<EventData> events = new ArrayList<>();

        for (String activator : activators) {
            // Do lower case for activator.
            activator = activator.toLowerCase();
            String activatorName = activator.replaceAll("\\(.+\\)", "");
            EventPriority priority = EventPriority.NORMAL;
            boolean ignoreCancelled = false;

            String arguments = RegexUtil.findFirst(activator, "\\(([^)]+)\\)");

            if (arguments != null) {
                String[] args = arguments.split(",? ");

                for (String argument : args) {
                    if (argument.startsWith("priority=")) {
                        String priorityName = argument.replace("priority=", "").toUpperCase();

                        try {
                            priority = EventPriority.valueOf(priorityName);
                        } catch (Exception exception) {
                            Logger.error("Cant find priority with name=" + priorityName);
                        }
                    } else if (argument.startsWith("ignorecancelled")) {
                        String cancelledType = argument.replace("ignorecancelled=", "").toLowerCase();

                        // If starts with t that means our type is true.
                        ignoreCancelled = cancelledType.startsWith("t");
                    }
                }
            }

            for (Class<? extends Event> clazz : EventContainer.ALL_BUKKIT_EVENTS) {
                if (clazz.getSimpleName().toLowerCase().contains(activatorName)) {
                    events.add(new EventData(clazz, priority, ignoreCancelled));
                }
            }
        }

        return events;
    }

}
