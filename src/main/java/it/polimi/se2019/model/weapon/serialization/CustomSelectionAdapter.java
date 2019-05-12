package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.*;
import it.polimi.se2019.model.weapon.Selection;

import java.lang.reflect.Type;
import java.util.Set;

public class CustomSelectionAdapter<T> implements JsonDeserializer<Selection<T>>, JsonSerializer<Selection<T>> {
    @Override
    public Selection<T> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        Set<T> contentsAsSet = context.deserialize(jsonElement, Set.class);

        return Selection.fromSet(contentsAsSet);
    }

    @Override
    public JsonElement serialize(Selection<T> toSerialize, Type type, JsonSerializationContext context) {
        return context.serialize(toSerialize.asSet(), Set.class);
    }
}
