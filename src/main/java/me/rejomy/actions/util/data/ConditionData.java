package me.rejomy.actions.util.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.rejomy.actions.util.interfaces.Expression;

@AllArgsConstructor
@Getter
public class ConditionData {

    final ConditionPart firstPart;
    final ConditionPart secondPart;
    final Expression[] expressions;

    @Getter
    @AllArgsConstructor
    public static class ConditionPart {
        ConditionType conditionType;
        // Should we try use this condition for target or for event.
        boolean forTarget;

        Object object;
    }

    public enum ConditionType {
        PLACEHOLDER,
        CLASS,
        INVOKE,
        CONTAINER,
        NOTHING;
    }
}
