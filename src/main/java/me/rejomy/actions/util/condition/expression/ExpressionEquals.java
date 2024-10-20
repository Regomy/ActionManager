package me.rejomy.actions.util.condition.expression;

import me.rejomy.actions.util.ExpressionUtil;
import me.rejomy.actions.util.interfaces.Expression;

public class ExpressionEquals implements Expression<Object, Object> {

    @Override
    public boolean test(Object object, Object object2) {
        return ExpressionUtil.tryToCompare(object, object2, (objectA, objectB) -> {
            return objectA.equals(objectB);
        });
    }
}
