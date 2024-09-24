package me.rejomy.actions.util;

import lombok.experimental.UtilityClass;

import java.util.Collection;

@UtilityClass
public class ClassUtil {

    public boolean isCollection(Object object) {
        return object instanceof Collection;
    }

    public boolean isArray(Object object) {
        return object.getClass().isArray();
    }
}
