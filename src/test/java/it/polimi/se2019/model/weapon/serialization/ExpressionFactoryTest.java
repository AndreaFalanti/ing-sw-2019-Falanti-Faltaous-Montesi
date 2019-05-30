package it.polimi.se2019.model.weapon.serialization;

import com.sun.applet2.preloader.event.PreloaderEvent;
import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.weapon.behaviour.DamageLiteral;
import it.polimi.se2019.model.weapon.behaviour.Expression;
import it.polimi.se2019.model.weapon.behaviour.InflictDamage;
import it.polimi.se2019.model.weapon.behaviour.TargetsLiteral;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.util.PrettyJsonElement;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class ExpressionFactoryTest {
    public Expression mSimpleBehaviour;

    @Before
    public void instantiate() {
        mSimpleBehaviour = new InflictDamage(
                new DamageLiteral(new Damage(1, 0)),
                new TargetsLiteral(Collections.singleton(PlayerColor.GREEN))
        );
    }

    @Test
    public void testToJsonSimpleBehaviour() {
        String actual = ExpressionFactory.toJson(mSimpleBehaviour);

        String expected = Jsons.get("weapons/tests/simple_behaviour");

        assertEquals(
                new PrettyJsonElement(expected),
                new PrettyJsonElement(actual)
        );
    }

    @Test
    public void testFromJsonSimpleBehaviour() {
        Expression actual = ExpressionFactory.fromJson(Jsons.get("weapons/tests/simple_behaviour"));

        Expression expected = mSimpleBehaviour;

        assertEquals(expected, actual);
    }
}
