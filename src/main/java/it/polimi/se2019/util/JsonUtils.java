package it.polimi.se2019.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import sun.plugin.dom.exception.InvalidAccessException;

import java.util.Map;

public class JsonUtils {
    public static JsonObject combineObjects(JsonObject lhs, JsonObject rhs) {
        JsonObject result = lhs.deepCopy();

        for (Map.Entry<String, JsonElement> entry : rhs.entrySet()) {
            if (result.has(entry.getKey()))
                throw new InvalidAccessException("Overlapping keys!");

            result.add(entry.getKey(), entry.getValue().deepCopy());
        }

        return result;
    }
}
