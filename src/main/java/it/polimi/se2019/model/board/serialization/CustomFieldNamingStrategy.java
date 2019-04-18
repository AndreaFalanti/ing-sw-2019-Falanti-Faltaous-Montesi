package it.polimi.se2019.model.board.serialization;

import com.google.gson.FieldNamingStrategy;

import java.lang.reflect.Field;

public class CustomFieldNamingStrategy implements FieldNamingStrategy {

    @Override
    public String translateName(Field field) {
        String fieldName = field.getName();

        // transform private fields to make their names prettier
        // e.g. "mField" -> "field"
        // if a field is one letter long, ignore the conversion
        if (fieldName.length() >= 2 &&
                fieldName.charAt(0) == 'm' &&
                Character.isUpperCase(fieldName.charAt(1))) {
            return Character.toLowerCase(fieldName.charAt(1)) + fieldName.substring(2);
        }

        return field.getName();
    }
}
