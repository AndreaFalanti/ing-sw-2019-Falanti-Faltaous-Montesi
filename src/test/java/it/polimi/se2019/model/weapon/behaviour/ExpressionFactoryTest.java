package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.util.JsonString;
import it.polimi.se2019.util.Jsons;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class ExpressionFactoryTest {
    public Expression mSimpleBehaviour;

    @Before
    public void instantiate() {
        mSimpleBehaviour = new InflictDamage(
                new DamageLiteral(new Damage(1, 0)),
                new TargetsLiteral(new HashSet(Collections.singleton(PlayerColor.GREEN))
        ));
    }

    @Test
    public void testToJsonSimpleBehaviour() {
        String actual = ExpressionFactory.toJson(mSimpleBehaviour);

        String expected = Jsons.get("weapons/tests/simple_behaviour");

        assertEquals(
                new JsonString(expected),
                new JsonString(actual)
        );
    }

    @Test
    public void testFromJsonSimpleBehaviour() {
        Expression actual = ExpressionFactory.fromJson(Jsons.get("weapons/tests/simple_behaviour"));

        Expression expected = mSimpleBehaviour;

        assertEquals(expected, actual);
    }
}
