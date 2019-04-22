package it.polimi.se2019.resource_handler;

import org.junit.Test;

import java.util.function.BiPredicate;

import static org.junit.Assert.assertTrue;

public class ResourceHandlerTest {
    @Test
    public void testRegisterResourceLoadSimpleJsonFile() {
        ResourceHandler resourceHandler = new ResourceHandler();

        resourceHandler.registerResource(
                JsonResource::loadFromPath,
                "tests/test.json", "testJsonString");

        String expectedJsonString = "" +
                "{\n" +
                "   \"name\" : \"Mario\"\n" +
                "}";

        BiPredicate<String, String> strEqualsIgnoreWhitespace = (String lhs, String rhs) ->
            lhs.replaceAll("\\s+","").equals(rhs.replaceAll("\\s+",""));

        assertTrue(strEqualsIgnoreWhitespace.test(expectedJsonString,
                                                  (String) resourceHandler.get("testJsonString")));
    }

    @Test(expected = NonExistentResourceException.class)
    public void testGetRequestNonexistentResource() {
        ResourceHandler resourceHandler = new ResourceHandler();

        resourceHandler.get("nonexistent");
    }
}
