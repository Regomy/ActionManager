package me.rejomy.actions.util.condition.expression;

import me.rejomy.actions.util.interfaces.Expression;

public class ExpressionLarge implements Expression<Object, Object> {

    @Override
    public boolean test(Object number, Object number2) {
        // Convert all values to double because all numbers can convert to here.
        double value = Double.parseDouble(String.valueOf(number));
        double value2 = Double.parseDouble(String.valueOf(number));

        return value > value2;
    }
}
