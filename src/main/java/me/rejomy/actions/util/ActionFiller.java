package me.rejomy.actions.util;

import lombok.experimental.UtilityClass;
import me.rejomy.actions.ActionsAPI;
import me.rejomy.actions.util.condition.ConditionParser;
import me.rejomy.actions.util.data.ActionData;
import me.rejomy.actions.util.data.ConditionData;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@UtilityClass
public class ActionFiller {

    final Set<Class<? extends Event>> ALL_BUKKIT_EVENTS = ReflectionUtil
            .findClasses(Event.class, "org.bukkit.event");

    public ActionData newActionFromConfig(List<String> cfgActivator, List<String> condition, List<String> commands) {
        // Parse activator to Event.
        List<Class<? extends Event>> activators = getEventsFromActivator(cfgActivator);

        List<ConditionData> conditions = parseConditions(condition);

        // Do color for commands
        commands.replaceAll(ColorUtil::apply);

        // Validate lists
        if (activators.isEmpty()) {
            Logger.error("Activators for some action is empty.",
                    "Try to find this action in config, if is this mistake, report to github.",
                    "Skipping some action...");
            return null;
        }

        if (conditions.isEmpty()) {
            Logger.error("Conditions for some action is empty.",
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

        return new ActionData(activators, conditions, commands);
    }

    /**
     * Here we check condition and add this to ActionsData list.
     * @param cfgConditions Condition at action.condition
     * @return List of valid conditions
     */
    public List<ConditionData> parseConditions(List<String> cfgConditions) {
        List<ConditionData> conditions = new ArrayList<>();

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

    public List<Class<? extends Event>> getEventsFromActivator(List<String> activators) {
        List<Class<? extends Event>> events = new ArrayList<>();

        for (String activator : activators) {
            // Do lower case for activator.
            activator = activator.toLowerCase();

            for (Class<? extends Event> clazz : ALL_BUKKIT_EVENTS) {
                if (clazz.getSimpleName().toLowerCase().contains(activator)) {
                    events.add(clazz);
                }
            }
        }

        return events;
    }

}
