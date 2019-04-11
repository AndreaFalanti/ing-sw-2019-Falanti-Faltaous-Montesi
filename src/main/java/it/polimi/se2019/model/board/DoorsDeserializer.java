package it.polimi.se2019.model.board;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

class DoorsDeserializer implements JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new Gson().fromJson(jsonElement, List.class).stream()
                .map(String.class::cast)
                .mapToInt(strDoor -> {
                    if (strDoor.equals("north")) {
                        return 0b1000;
                    }
                    else if (strDoor.equals("east")) {
                        return 0b0100;
                    }
                    else if (strDoor.equals("south")) {
                        return 0b0010;
                    }
                    else if (strDoor.equals("west")) {
                        return 0b0001;
                    }
                    else return 0b0000;
                })
                .sum();
    }
}
