package me.rejomy.actions.util.condition;

import lombok.experimental.UtilityClass;
import me.rejomy.actions.hook.PAPIHook;
import me.rejomy.actions.util.CollectionUtil;
import me.rejomy.actions.util.ReflectionUtil;
import me.rejomy.actions.util.data.ConditionData;
import me.rejomy.actions.util.interfaces.Expression;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

@UtilityClass
public class ConditionChecker {

    public boolean checkConditions(List<ConditionData> conditions, Event event) {
        return checkConditions(conditions, event, null);
    }

    public boolean checkConditions(List<ConditionData> conditions, Event event, Object target) {
        for (ConditionData condition : conditions) {
            Object firstPart = parse(condition.getFirstPart(), event, target);
            Object secondPart = parse(condition.getSecondPart(), event, target);

            List<Object> firstPartValues = CollectionUtil.toList(firstPart);
            List<Object> secondPartValue = CollectionUtil.toList(secondPart);

            for (Expression expression : condition.getExpressions()) {

                // This solution solve problem where two elements is arrays.
                for (Object firstPartObject : firstPartValues) {
                    for (Object secondPartObject : secondPartValue) {
                        boolean passed = expression.test(firstPartObject, secondPartObject);

                        if (!passed)
                            return false;
                    }
                }
            }
        }

        return true;
    }

    private Object parse(ConditionData.ConditionPart part, Event event, Object target) {
        Object value = part.getObject();

        switch (part.getConditionType()) {
            case INVOKE -> {
                String path = (String) value;
                return ReflectionUtil.getObject(path, part.isForTarget()? target : event, true);
            }
            case CLASS -> {
                return ReflectionUtil.getClassByPath((String) value);
            }
            case CONTAINER -> {
                return value;
            }
            case NOTHING -> {
                // Ignore this block.
            }
            case PLACEHOLDER -> {
                if (PAPIHook.enable) {
                    // TODO: Add choice and settings for damager or entity as example.
                    Object player = ReflectionUtil.getOrderValues(event, false,
                            "getPlayer", "getEntity");

                    if (player instanceof Player) {
                        return PAPIHook.setPlaceholders((Player) player, part.getObject().toString());
                    }
                }
            }
        }

        return part.getObject();
    }
}
