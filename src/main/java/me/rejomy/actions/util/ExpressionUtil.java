package me.rejomy.actions.util;

import lombok.experimental.UtilityClass;
import me.rejomy.actions.util.interfaces.Expression;

@UtilityClass
public class ExpressionUtil {

    // Use only for equals and not equals
    public boolean tryToCompare(Object object, Object object2, Expression<Object, Object> expression) {
        Class<?> firstClazz = object.getClass();
        Class<?> secondClazz = object2.getClass();

        // Check if either value is a wrapper for a primitive type.
        if (firstClazz == Boolean.class || firstClazz == Byte.class ||
                firstClazz == Short.class || firstClazz == Integer.class ||
                firstClazz == Long.class || firstClazz == Float.class ||
                firstClazz == Double.class || secondClazz == Boolean.class ||
                secondClazz == Byte.class || secondClazz == Short.class ||
                secondClazz == Integer.class || secondClazz == Long.class ||
                secondClazz == Float.class || secondClazz == Double.class) {

            // Convert both values to string before comparing.
            return expression.test(String.valueOf(object), String.valueOf(object2));
        }

        // Otherwise, use the original comparison.
        return expression.test(object, object2);
    }
}
