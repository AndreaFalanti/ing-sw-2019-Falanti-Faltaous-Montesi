package it.polimi.se2019.resource_handler;

import it.polimi.se2019.util.JsonString;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResourceHandlerTest {
    @Test
    public void testRegisterResourceLoadSimpleJsonFile() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setBasePath("resources/");

        resourceHandler.registerResource(
                JsonResource::loadFromPath,
                "src/main/resources/json/tests/test.json", "testJsonString");

        String expectedJsonString = "" +
                "{\n" +
                "   \"value\" : \"hello test!\"\n" +
                "}";

        assertEquals(new JsonString(expectedJsonString),
                     new JsonString((String) resourceHandler.get("testJsonString")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRequestNonexistentResource() {
        ResourceHandler resourceHandler = new ResourceHandler();

        resourceHandler.get("nonexistent");
    }
}
