package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.*;
import it.polimi.se2019.controller.weapon.Expression;

import java.lang.reflect.Type;

public class CustomExpressionAdapter implements JsonSerializer<Expression>, JsonDeserializer<Expression> {
    @Override
    public Expression deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        return ExpressionFactory.fromJson(jsonElement);
    }

    @Override
    public JsonElement serialize(Expression expression, Type type, JsonSerializationContext jsonSerializationContext) {
        return ExpressionFactory.toJsonTree(expression);
    }
}