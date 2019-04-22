package it.polimi.se2019.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonsTest {
    /**
     * Load all jsons using a Jsons class and check if test resources are available
     */
    // TODO: find out if it's better to do this using some sort of "mock resources directory"
    @Test
    public void testInstanceInstanciateOnceResourceContentsAreFine() {
        // TODO: remove the need to specify "resources" at the start of the path
        final String testJsonString = Jsons.get("tests/test");

        // DB: uncomment this to display all loaded resources by name
        // System.out.println(Jsons.listResourceNames());

        final String expectedString = "{\"value\" : \"hello test!\"}";

        assertEquals(new JsonString(testJsonString),
                     new JsonString(expectedString));
    }
}

