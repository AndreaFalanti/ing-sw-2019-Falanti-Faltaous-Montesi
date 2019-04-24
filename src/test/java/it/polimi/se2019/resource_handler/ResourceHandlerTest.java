package it.polimi.se2019.resource_handler;

import it.polimi.se2019.util.JsonString;
import org.junit.Test;

import java.util.function.BiPredicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResourceHandlerTest {
    @Test
    public void testRegisterResourceLoadSimpleJsonFile() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setBasePath("resources/");

        resourceHandler.registerResource(
                JsonResource::loadFromPath,
                "resources/json/tests/test.json", "testJsonString");

        String expectedJsonString = "" +
                "{\n" +
                "   \"value\" : \"hello test!\"\n" +
                "}";

        BiPredicate<String, String> strEqualsIgnoreWhitespace = (String lhs, String rhs) ->
            lhs.replaceAll("\\s+","").equals(rhs.replaceAll("\\s+",""));

        assertEquals(new JsonString(expectedJsonString),
                     new JsonString((String) resourceHandler.get("testJsonString")));
    }

    @Test(expected = NonExistentResourceException.class)
    public void testGetRequestNonexistentResource() {
        ResourceHandler resourceHandler = new ResourceHandler();

        resourceHandler.get("nonexistent");
    }
}
