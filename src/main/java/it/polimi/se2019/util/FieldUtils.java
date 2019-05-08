package it.polimi.se2019.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

public class FieldUtils {
    private FieldUtils() {}

    public static Field[] getFieldsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotationClass))
                .toArray(Field[]::new);
    }
}
