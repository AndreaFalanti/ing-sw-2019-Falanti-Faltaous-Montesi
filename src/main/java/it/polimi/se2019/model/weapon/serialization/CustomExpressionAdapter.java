package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.*;
import it.polimi.se2019.model.weapon.behaviour.*;

import java.lang.reflect.Type;

public class CustomExpressionAdapter implements JsonSerializer<AtomicExpression>, JsonDeserializer<AtomicExpression> {
    @Override
    public AtomicExpression deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        return ExpressionFactory.fromJson(jsonElement);
    }

    @Override
    public JsonElement serialize(AtomicExpression expression, Type type, JsonSerializationContext jsonSerializationContext) {
        return ExpressionFactory.toJsonTree(expression);
    }
}