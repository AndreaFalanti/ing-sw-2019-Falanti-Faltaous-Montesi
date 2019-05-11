package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.se2019.model.weapon.PayedEffect;

import java.lang.reflect.Type;

public class CustomPrimaryEffectAdapter implements JsonSerializer<PayedEffect> {
    @Override
    public JsonElement serialize(PayedEffect payedEffect, Type type, JsonSerializationContext context) {
        // TODO: find better way of doing this
        // TODO: see if context.serialize(") is needed or not...
        JsonElement result = ExpressionFactory.GSON.toJsonTree(payedEffect.getBehaviour());

        // URGENT TODO: find a betters way to do this...
        result.getAsJsonObject().add("expr", new JsonPrimitive(payedEffect.getBehaviour().getClass().getSimpleName()));

        return result;
    }
}
