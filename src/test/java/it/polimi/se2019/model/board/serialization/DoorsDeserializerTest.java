package it.polimi.se2019.model.board.serialization;

import com.google.gson.*;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DoorsDeserializerTest {
    @Test
    public void testDeserialize() {
        assertEquals(
                new Integer(5),
                new DoorsDeserializer().deserialize(
                        new Gson().fromJson(
                                "[ \"north\", \"south\" ]",
                                JsonElement.class
                        ),
                        List.class,
                        new JsonDeserializationContext() {
                            @Override
                            public <T> T deserialize(JsonElement jsonElement, Type type) throws JsonParseException {
                                throw new UnsupportedOperationException();
                            }
                        }
                )
        );
    }

    @Test
    public void testSerialize() {
        assertEquals(
                new Gson().toJsonTree(
                        Arrays.asList("north", "east")
                ),
                new DoorsDeserializer().serialize(
                        10,
                        List.class,
                        new JsonSerializationContext() {
                            @Override
                            public JsonElement serialize(Object o) {
                                throw new UnsupportedOperationException();
                            }

                            @Override
                            public JsonElement serialize(Object o, Type type) {
                                throw new UnsupportedOperationException();
                            }
                        }
                )
        );
    }
}
