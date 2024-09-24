package me.rejomy.actions.util.condition.expression;

import me.rejomy.actions.util.interfaces.Expression;

public class ExpressionContains implements Expression<String, String> {

    @Override
    public boolean test(String string, String string2) {
        return string.toLowerCase().contains(string2.toLowerCase());
    }
}
