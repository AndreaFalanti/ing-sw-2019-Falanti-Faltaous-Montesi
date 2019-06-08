package it.polimi.se2019.model.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.se2019.model.AmmoValue;

import java.lang.reflect.Type;

public class AmmoValueDeserializer implements JsonDeserializer<AmmoValue> {
    @Override
    public AmmoValue deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return AmmoValue.from(jsonElement.getAsString())
                .orElseThrow(() ->
                        new JsonParseException(
                                "Could not parse malformed AmmoValue [value: " + jsonElement.getAsString() + "]"
                        ));
    }
}
