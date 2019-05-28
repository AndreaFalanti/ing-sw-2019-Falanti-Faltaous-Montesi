package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.*;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.weapon.PayedEffect;
import it.polimi.se2019.model.weapon.behaviour.Expression;

import java.lang.reflect.Type;

// TODO: see if context.serialize(") is needed or not...
public class CustomPrimaryEffectAdapter implements JsonSerializer<PayedEffect>, JsonDeserializer<PayedEffect> {
    @Override
    public JsonElement serialize(PayedEffect payedEffect, Type type, JsonSerializationContext context) {
        // if null return empty object
        Expression behaviour = payedEffect.getBehaviour();
        if (behaviour == null)
            return new JsonObject();

        // TODO: find better way of doing this
        JsonElement result = ExpressionFactory.toJsonTree(payedEffect.getBehaviour());

        // URGENT TODO: find a betters way to do this...
        result.getAsJsonObject().add("expr", new JsonPrimitive(payedEffect.getBehaviour().getClass().getSimpleName()));

        return result;
    }

    @Override
    public PayedEffect deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        Expression expression = ExpressionFactory.GSON.fromJson(jsonElement, Expression.class);

        return new PayedEffect(
                new AmmoValue(0, 0, 0),
                expression
        );
    }
}
