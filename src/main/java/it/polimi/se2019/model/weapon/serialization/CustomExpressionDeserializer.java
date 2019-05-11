package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.se2019.model.weapon.behaviour.Expression;

import java.lang.reflect.Type;

public class CustomExpressionDeserializer implements JsonDeserializer<Expression> {
    @Override
    public Expression deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        Expression result = context.deserialize(jsonElement, Expression.class);

        result.updateAllSubExpressions();

        return result;
    }
}
