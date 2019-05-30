package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.AmmoValueTest;
import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.weapon.behaviour.*;
import it.polimi.se2019.util.Jsons;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpressionParserTest {
    @Test
    public void parseDamageLiteralNoMarksTest() {
        JsonElement expected = ExpressionFactory.toJsonTree(
                new DamageLiteral(new Damage(1, 0))
        );

        JsonElement actual = ExpressionParser.parse(new JsonPrimitive("1d"));

        assertEquals(expected, actual);
    }

    @Test
    public void parseComplexBehaviour() {
        JsonElement expected = new Gson().fromJson(
                Jsons.get("weapons/tests/raw_behaviour"),
                JsonElement.class
        );

        JsonElement actual = ExpressionParser.parse(
                new Gson().fromJson(Jsons.get("weapons/tests/pretty_behaviour"), JsonElement.class)
        );

        assertEquals(expected, actual);
    }
}
