package it.polimi.se2019.model.board.serialization;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.IntStream;

public class DoorsDeserializer implements JsonDeserializer<Integer>, JsonSerializer<Integer> {
    @Override
    public Integer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
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

    @Override
    public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
        String[] directions = {"north", "east", "south", "west"};

        JsonArray result = new JsonArray();

        IntStream.range(0, 4)
                .mapToObj(i -> ((src & i) != src) ? directions[i] : "none")
                .filter(strDir -> !strDir.equals("none"))
                .forEach(strDir -> result.add(strDir));

        return result;
    }
}
