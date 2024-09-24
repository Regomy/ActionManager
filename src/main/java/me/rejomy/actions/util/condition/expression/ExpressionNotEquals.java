package me.rejomy.actions.util.condition.expression;

import me.rejomy.actions.util.ExpressionUtil;
import me.rejomy.actions.util.interfaces.Expression;

public class ExpressionNotEquals implements Expression<Object, Object> {

    @Override
    public boolean test(Object object, Object object2) {
        return ExpressionUtil.tryToCompare(object, object2, (objectA, objectB) -> {
            System.out.println("a != " + objectA + " " + objectB + " result1111=" + (!objectA.equals(objectB)));
            return !objectA.equals(objectB);
        });
    }
}
