package me.rejomy.actions.util;

import lombok.experimental.UtilityClass;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ReflectionUtil {

    /**
     * Find classes in jar.
     * @param clazz - Clazz that we should need.
     * @param packageName - Package where we should find classes. If null, we will find in jar.
     * @return Set of classes that extends from T
     * @param <T> class.
     */
    public <T> Set<Class<? extends T>> findClasses(Class<T> clazz, String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> classes = reflections.getSubTypesOf(clazz);

        if (packageName != null) {
            classes = classes.stream()
                    .filter(findClazz -> findClazz.getPackage().getName().startsWith(packageName))
                    .collect(Collectors.toSet());
        }

        return classes;
    }

    /**
     * Return class of method arguments. multiple(int a, double b)
     * @param argumentIndex - get argument type with index. 0 - int, 1 - b;
     * @return type of element with index.
     */
    public Class<?> getMethodArgumentClass(int argumentIndex, Class<?> clazz, String methodName) {
        try {
            // Get the "test" method from the class
            Method testMethod = clazz.getMethod(methodName, Object.class, Object.class);

            // Get the parameter types of the "test" method
            Class<?>[] parameterTypes = testMethod.getParameterTypes();

            if (argumentIndex >= parameterTypes.length) {
                throw new IllegalArgumentException("Argument index " + argumentIndex +
                        " large than method " + methodName + " argument in class " + clazz.getName() +
                        " with args size " + parameterTypes.length);
            }

            return parameterTypes[argumentIndex];
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Class<?> getClassByPath(String path) {
        try {
            return Class.forName(path);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return object from class with selected path.
     * @param path Path to object, entity#name find entity in current class, and after
     *             move to entity class and select name field.
     * @param object Class where we should start.
     * @param sendError Should we send error in console if this class not found.
     * @return null or object.
     */
    public Object getObject(String path, Object object, boolean sendError) {
        String[] parts = path.split("#");
        String find = parts[0];
        Class<?> clazz = object.getClass();

        while (clazz != null) {
            try {
                Object foundObject;

                if (find.contains("()")) {
                    // Dont save this to find, because otherwise if this getMethodObject throw error,
                    // we get problem that we dont have () and we will look method as field.
                    String replacedFind = find.replace("()", "");
                    foundObject = getMethodObject(replacedFind, clazz, object);
                    find = replacedFind;
                } else {
                    foundObject = getFieldObject(find, clazz, object);
                }

                if (parts.length > 1) {
                    return getObject(path.substring(path.indexOf("#") + 1), foundObject, sendError);
                } else {
                    return foundObject;
                }

            } catch (NoSuchFieldException | IllegalAccessException | RuntimeException | NoSuchMethodException | InvocationTargetException e) {
                clazz = clazz.getSuperclass();
            }
        }

        if (sendError) {
            Logger.error("Path " + find + " not found in " + object.getClass().getName() + " object or his parent values.");
        }

        return null;
    }

    /**
     * Return field object from selected object.
     */
    public Object getFieldObject(String fieldName, Class<?> clazz, Object object)
            throws NoSuchFieldException, IllegalAccessException {

        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true); // Открываем доступ к приватному полю

        return field.get(object);
    }

    /**
     * Return method return value from selected object.
     */
    public Object getMethodObject(String methodName,  Class<?> clazz, Object object)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = clazz.getDeclaredMethod(methodName);
        method.setAccessible(true);

        return method.invoke(object);
    }

    public Object getOrderValues(Object object, boolean sendError, String... fieldNames) {
        Object value = null;

        for (String fieldName : fieldNames) {
            value = getObject(fieldName, object, false);

            if (value != null) {
                break;
            }
        }

        if (value == null && sendError) {
            Logger.error("Path " + String.join(", ", fieldNames) + " not found in " + object.getClass().getName() + " object or his parent fields.");
        }

        return value;
    }
}
