package it.polimi.se2019.model.weapon.serialization;

import it.polimi.se2019.controller.weapon.expression.*;
import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.util.PrettyJsonElement;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpressionFactoryTest {
    public Behaviour mSimpleBehaviour;

    @Before
    public void instantiate() {
        mSimpleBehaviour = new InflictDamage(
                new DamageLiteral(new Damage(1, 0)),
                new TargetLiteral(PlayerColor.GREEN)
        );
    }

    @Test
    public void testToJsonSimpleBehaviour() {
        String actual = ExpressionFactory.toJson(mSimpleBehaviour);

        String expected = Jsons.get("weapons/tests/raw_simple_behaviour");

        assertEquals(
                new PrettyJsonElement(expected),
                new PrettyJsonElement(actual)
        );
    }

    @Test
    public void testFromJsonSimpleDefaults() {
        Expression actual = ExpressionFactory.fromJson(Jsons.get("weapons/tests/simple_defaults"));

        Expression expected = new SelectOneTarget(
                new CanSee(new You())
        );

        assertEquals(expected, actual);
    }
}
