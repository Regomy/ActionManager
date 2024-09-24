package me.rejomy.actions.util.condition;

import lombok.experimental.UtilityClass;
import me.rejomy.actions.ActionsAPI;
import me.rejomy.actions.hook.PAPIHook;
import me.rejomy.actions.util.Logger;
import me.rejomy.actions.util.condition.expression.*;
import me.rejomy.actions.util.data.ConditionData;
import me.rejomy.actions.util.interfaces.Expression;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ConditionParser {

    public ConditionData parseCondition(String condition) {
        String[] parts = condition.split("\\s");

        if (parts.length != 3) {
            Logger.error(String.format("Error when parsing %s condition, missing parts %s/3!",
                    condition, parts.length));
            return null;
        }

        // Fill expression from condition.
        List<Expression> expressions = new ArrayList<>();
        {
            String expression = parts[1];

            boolean large = expression.contains(">");
            boolean small = expression.contains("<");
            boolean notEquals = expression.contains("!=");
            boolean equals = expression.equals("=") || expression.equals("==");
            boolean containsIgnoreCase = expression.contains("=~");
            boolean instanceofEx = expression.contains("instanceof");
            boolean permission = expression.contains("permission");

            // Validate expression
            if (large && small) {
                Logger.error(String.format(
                        "Condition %s contains large and small modifier, these modifiers are not compatible.",
                            condition));
                return null;
            }

            if (expression.length() > 2 && !instanceofEx && !permission) {
                Logger.error(String.format(
                        "Condition %s contains too big size %s, you can use only one or two characters.",
                        condition, expression.length()));
                return null;
            }
            //

            if (instanceofEx) {
                expressions.add(new ExpressionInstanceof());
            } else if (permission) {
                expressions.add(new ExpressionPermission());
            } else {
                if (large) {
                    expressions.add(new ExpressionLarge());
                } else if (small) {
                    expressions.add(new ExpressionSmall());
                } else if (notEquals) {
                    expressions.add(new ExpressionNotEquals());
                } else if (containsIgnoreCase) {
                    expressions.add(new ExpressionContains());
                }

                if (equals) {
                    expressions.add(new ExpressionEquals());
                }
            }
        }

        ConditionData.ConditionPart firstPart = validatePart(parts[0]);
        ConditionData.ConditionPart secondPart = validatePart(parts[2]);

        ConditionData conditionData = new ConditionData(firstPart, secondPart, expressions.toArray(new Expression[0]));

        return conditionData;
    }

    ConditionData.ConditionPart validatePart(String string) {
        // Try to find value in container
        if (string.startsWith("$")) {
            Object valueFromContainer = ActionsAPI.INSTANCE.getConfig().getContainers()
                    .get(string.substring(1));

            if (valueFromContainer == null) {
                Logger.error("Cant find value calls " + string);
            }

            return new ConditionData.ConditionPart(ConditionData.ConditionType.CONTAINER,
                    valueFromContainer == null? string : valueFromContainer);
        }

        // Check if this is placeholder
        if (PAPIHook.enable && string.startsWith("%") && string.endsWith("%")) {
            return new ConditionData.ConditionPart(ConditionData.ConditionType.PLACEHOLDER, string);
        }

        // Check if this is reflection invoke task
        if (string.startsWith("{") && string.endsWith("}")) {
            // Remove indicators
            string = string.substring(1, string.length() - 1);
            return new ConditionData.ConditionPart(ConditionData.ConditionType.INVOKE, string);
        }

        // Check if this is reflection class task
        if (string.startsWith("[") && string.endsWith("]")) {
            // Remove indicators
            string = string.substring(1, string.length() - 1);
            return new ConditionData.ConditionPart(ConditionData.ConditionType.CLASS, string);
        }

        return new ConditionData.ConditionPart(ConditionData.ConditionType.NOTHING, string);
    }
}
