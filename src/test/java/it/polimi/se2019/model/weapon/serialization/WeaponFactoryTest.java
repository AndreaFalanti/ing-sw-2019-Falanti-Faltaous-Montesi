package it.polimi.se2019.model.weapon.serialization;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.model.weapon.behaviour.*;
import it.polimi.se2019.util.JsonString;
import it.polimi.se2019.util.Jsons;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WeaponFactoryTest {
    public Weapon mHeatseeker;

    @Before
    public void instantiate() {
        // TODO: consider using builder instead of setters
        mHeatseeker = new Weapon("heatseeker", new AmmoValue(2, 1, 0), new AmmoValue(1, 1, 0));
        mHeatseeker.setPrimaryEffect(
                new InflictDamage(
                        new DamageLiteral(
                                new Damage(3, 0)
                        ),
                        new SelectTargets(
                                new IntLiteral(1),
                                new IntLiteral(1),
                                new NegateSelection(
                                        new CanSee()
                                )
                        )
                )
        );
    }

    // TODO NEXT: implement
    @Test
    public void testToJsonHeatseeker() {
        String actual = WeaponFactory.toJson(mHeatseeker);

        String expected = Jsons.get("weapons/heatseeker");

        assertEquals(
                new JsonString(expected),
                new JsonString(actual)
        );
    }

    // TODO NEXT: implement
    @Test
    public void testFromJsonSimpleBehaviour() {
        Expression actual = ExpressionFactory.fromJson(Jsons.get("weapons/tests/simple_behaviour"));

        // Expression expected = mSimpleBehaviour;

        // assertEquals(expected, actual);
    }
}
