package it.polimi.se2019.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonUtilsTest {

    @Test
    public void testCombineObjects() {
        JsonObject lhs = new JsonObject();
        lhs.add("hello", new JsonPrimitive(1));
        lhs.add("there", new JsonPrimitive(2));

        JsonObject rhs = new JsonObject();
        rhs.add("howdy", new JsonPrimitive(3));
        rhs.add("partner", new JsonPrimitive(4));

        JsonObject actual = JsonUtils.combineObjects(lhs, rhs);

        JsonObject expected = new JsonObject();
        expected.add("hello", new JsonPrimitive(1));
        expected.add("there", new JsonPrimitive(2));
        expected.add("howdy", new JsonPrimitive(3));
        expected.add("partner", new JsonPrimitive(4));

        assertEquals(expected, actual);
    }
}