package me.rejomy.actions.util.condition.expression;

import me.rejomy.actions.util.interfaces.Expression;

public class ExpressionInstanceof implements Expression<Object, Object> {

    @Override
    public boolean test(Object object, Object object2) {
        if (object2 instanceof Class<?>) {
            return ((Class<?>) object2).isInstance(object);
        }

        return false;
    }
}
