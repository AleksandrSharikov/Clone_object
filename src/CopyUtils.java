import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public class CopyUtils {
    private static final Unsafe unsafe;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deepCopy(T toClone) {
        try {
            Class<?> clazz = toClone.getClass();
            Object copy = unsafe.allocateInstance(clazz);

            Field[] fields = clazz.getDeclaredFields();
            List<Object> fieldValues = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                Object fieldValue = field.get(toClone);
                if (fieldValue != null && !field.getType().isPrimitive() && !field.getType().getName().startsWith("java")) {
                    fieldValue = deepCopy(fieldValue); // Recursive call for nested class
                } else if (fieldValue instanceof Collection<?>) {
                    fieldValue = deepCopyCollection((Collection<?>) fieldValue); // Recursive call for collections
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

}
