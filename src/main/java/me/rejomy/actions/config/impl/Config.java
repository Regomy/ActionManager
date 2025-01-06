package me.rejomy.actions.config.impl;

import lombok.Getter;
import me.rejomy.actions.config.YamlConfig;
import me.rejomy.actions.util.ActionFiller;
import me.rejomy.actions.util.Logger;
import me.rejomy.actions.util.condition.ConditionParser;
import me.rejomy.actions.util.data.ActionData;
import me.rejomy.actions.util.data.ConditionData;
import me.rejomy.actions.util.data.EventData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;

import java.util.*;

@Getter
public class Config extends YamlConfig {

    HashMap<String, Object> containers = new HashMap<>();
    // In condition there is sections, in section there is a list of conditions.
    HashMap<String, List<ConditionData>> conditions = new HashMap<>();
    List<ActionData> actions = new ArrayList<>();

    /**
     * Used for increase performance, we dont need to check activators in all actions, we just need
     *  fill links to actions by event.
     */
    HashMap<EventData, List<ActionData>> actionsByEvent = new HashMap<>();

    public Config(FileConfiguration config) {
        super(config);
    }

    @Override
    public void load() {
        // Clear all containers if anyone do reload command or this links saved in the memory
        //  with previous config data.
        conditions.clear();
        containers.clear();
        actions.clear();
        actionsByEvent.clear();

        fillContainer();
        fillCondition();
        fillAction();

        for (ActionData action : actions) {
            for (EventData event : action.activators()) {
                actionsByEvent.computeIfAbsent(event, e -> new ArrayList<>()).add(action);
            }
        }
    }

    void fillContainer() {
        Set<String> containerKeys =  config.getConfigurationSection("container").getKeys(false);

        for (String containerName : containerKeys) {
            containers.put(containerName, config.get("container." + containerName));
        }

        Logger.info(String.format("Containers loaded %s from %s", containers.size(), containerKeys.size()));
    }

    void fillCondition() {
        Set<String> conditionNames =  config.getConfigurationSection("condition").getKeys(false);

        for (String conditionName : conditionNames) {
            int conditionNumber = 0;

            List<ConditionData> conditionDataList = new ArrayList<>();
            List<String> conditionStrings =  config.getStringList("condition." + conditionName);

            for (String conditionString : conditionStrings) {

                ConditionData conditionData = ConditionParser.parseCondition(conditionString);

                if (conditionData == null) {
                    Logger.error(String.format("Condition with number %s in section %s is incorrect!",
                            conditionNumber, conditionName));
                } else {
                    conditionDataList.add(conditionData);
                }
            }

            if (!conditionDataList.isEmpty()) {
                conditions.put(conditionName, conditionDataList);
            }

            Logger.info(String.format("Conditions for section %s loaded %s from %s", conditionName,
                    conditions.size(), conditionStrings.size()));
        }

        Logger.info(String.format("Sections loaded %s from %s", conditions.size(), conditionNames.size()));
    }

    void fillAction() {
        List<Map<?, ?>> actionSection = config.getMapList("action");
        int actionCount = 0;

        for (Map<?, ?> actionMap : actionSection) {
            actionCount++;

            List<String> activator = (List<String>) actionMap.get("activator");
            List<String> cfgCondition = getFromMap("condition", actionMap);
            List<String> command = (List<String>) actionMap.get("command");

            ActionData actionData = ActionFiller.newActionFromConfig(activator, cfgCondition, command);

            if (actionData == null) {
                Logger.error(String.format("Action with number %s is incorrect!", actionCount));
            } else {
                actions.add(actionData);
            }
        }

        Logger.info(String.format("Actions: loaded %s from %s", actions.size(), actionSection.size()));
    }

    /**
     * Return value from map and try cast it, if it does not exist or cast exception return null
     */
    <T> T getFromMap(String path, Map<?, ?> map) {
        try {
            return (T) map.get(path);
        } catch (ClassCastException | NullPointerException exception) {
            return null;
        }
    }
}
