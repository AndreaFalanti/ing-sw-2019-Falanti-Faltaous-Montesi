package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.*;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.AmmoValueTest;
import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.weapon.behaviour.*;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.util.PrettyJsonElement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpressionParserTest {
    @Test
    public void testParseDamageLiteralNoMarksTest() {
        JsonElement expected = ExpressionFactory.toJsonTree(
                new DamageLiteral(new Damage(1, 0))
        );

        JsonElement actual = ExpressionParser.parse(new JsonPrimitive("1d"));

        assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexBehaviour() {
        JsonElement expected = new Gson().fromJson(
                Jsons.get("weapons/tests/raw_behaviour"),
                JsonElement.class
        );

        JsonElement actual = ExpressionParser.parse(
                new Gson().fromJson(Jsons.get("weapons/tests/pretty_behaviour"), JsonElement.class)
        );

        assertEquals(
                new PrettyJsonElement(expected),
                new PrettyJsonElement(actual)
        );
    }

    @Test
    public void testParseLockrifleBehaviour() {
        JsonElement expected = new Gson().fromJson(
                Jsons.get("weapons/tests/raw_lock_rifle"),
                JsonElement.class
        );

        JsonElement actual = ExpressionParser.parse(
                new Gson().fromJson(Jsons.get("weapons/tests/pretty_lock_rifle"), JsonElement.class)
        );

        assertEquals(
                new PrettyJsonElement(expected),
                new PrettyJsonElement(actual)
        );
    }

    @Test
    public void testParseSimpleStore() {
         JsonElement expected = new Gson().fromJson(
                Jsons.get("weapons/tests/raw_simple_store"),
                JsonElement.class
        );

        JsonElement actual = ExpressionParser.parse(
                new Gson().fromJson(Jsons.get("weapons/tests/pretty_simple_store"), JsonElement.class)
        );

        assertEquals(
                new PrettyJsonElement(expected),
                new PrettyJsonElement(actual)
        );
    }
}
