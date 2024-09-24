package me.rejomy.actions.util.condition.expression;

import me.rejomy.actions.util.interfaces.Expression;

public class ExpressionContains implements Expression<Object, Object> {

    @Override
    public boolean test(Object string, Object string2) {
        return String.valueOf(string).toLowerCase().contains(String.valueOf(string2).toLowerCase());
    }
}
