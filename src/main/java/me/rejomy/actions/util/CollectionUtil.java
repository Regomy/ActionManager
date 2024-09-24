package me.rejomy.actions.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@UtilityClass
public class CollectionUtil {

    public List<Object> toList(Object... objects) {
        List<Object> list = new ArrayList<>();

        for (Object object : objects) {

            if (ClassUtil.isCollection(object)) {
                list.addAll((Collection<?>) object);
            } else if (ClassUtil.isArray(object)) {
                int length = Array.getLength(object);
                for (int i = 0; i < length; i++) {
                    Object objectFromArray = Array.get(object, i);
                    list.add(objectFromArray);
                }
            } else {
                list.add(object);
            }
        }

        return list;
    }
}
