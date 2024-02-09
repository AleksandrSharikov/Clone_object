import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CopyUtils {

    public static <T> T deepCopy(T toClone) {
        try {
            Class<?> clazz = toClone.getClass();
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            Constructor<?> selectedConstructor = null;
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() == 0) {
                    selectedConstructor = constructor;
                    break;
                }
            }
            if (selectedConstructor == null) {
                // If no constructor with 0 parameters is found, use the first constructor found
                selectedConstructor = constructors[0];
            }
            selectedConstructor.setAccessible(true); // Ensure we can access the constructor

            Object copy;
            if (selectedConstructor.getParameterCount() == 0) {
                copy = selectedConstructor.newInstance(); // Use the no-args constructor if available
            } else {
                // Prepare constructor arguments for constructors with parameters
                Object[] constructorArgs = new Object[selectedConstructor.getParameterCount()];
                for (int i = 0; i < constructorArgs.length; i++) {
                    constructorArgs[i] = getDefaultParameter(selectedConstructor.getParameterTypes()[i]);
                }
                copy = selectedConstructor.newInstance(constructorArgs); // Use the constructor with arguments
            }

            Field[] fields = clazz.getDeclaredFields();
            List<Object> fieldValues = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                Object fieldValue = field.get(toClone);
                if (fieldValue != null && !field.getType().isPrimitive() && !field.getType().getName().startsWith("java")) {
                    fieldValue = deepCopy(fieldValue); // Recursive call for nested class
                } else if (fieldValue instanceof Collection<?>) {
                    fieldValue = deepCopyCollection((Collection<?>) fieldValue); // Recursive call for collections
                }else if (fieldValue instanceof Map<?, ?>) {
                    fieldValue = deepCopyMap((Map<?, ?>) fieldValue); // Recursive call for maps
                }
                fieldValues.add(fieldValue);
            }

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                field.set(copy, fieldValues.get(i));
            }

            @SuppressWarnings("unchecked")
            T typedCopy = (T) copy;
            return typedCopy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <E, C extends Collection<E>> C deepCopyCollection(C collection) {
        try {
            C newCollection = (C) collection.getClass().getDeclaredConstructor().newInstance(); // Create a new instance of the same collection type

            for (E element : collection) {
                if (element != null && !element.getClass().isPrimitive() && !element.getClass().getName().startsWith("java")) {
                    newCollection.add((E) deepCopy(element)); // Recursive call for elements of the collection
                } else {
                    newCollection.add(element); // Copy primitive types directly
                }
            }

            return newCollection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    private static <K, V, M extends Map<K, V>> M deepCopyMap(M map) {
        try {
            M newMap = (M) map.getClass().getDeclaredConstructor().newInstance(); // Create a new instance of the same map type

            for (Map.Entry<K, V> entry : map.entrySet()) {
                K key = entry.getKey();
                V value = entry.getValue();
                if (key != null && !key.getClass().isPrimitive() && !key.getClass().getName().startsWith("java")) {
                    key = (K) deepCopy(key); // Recursive call for map keys
                }
                if (value != null && !value.getClass().isPrimitive() && !value.getClass().getName().startsWith("java")) {
                    value = (V) deepCopy(value); // Recursive call for map values
                }
                newMap.put(key, value);
            }

            return newMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to get default values for constructor arguments
    private static Object getDefaultParameter(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class) return false;
            if (type == char.class) return '\u0000';
            if (type == byte.class || type == short.class || type == int.class) return 0;
            if (type == long.class) return 0L;
            if (type == float.class) return 0.0f;
            if (type == double.class) return 0.0;
        }
        return null; // For non-primitive types, use null as default
    }
}
