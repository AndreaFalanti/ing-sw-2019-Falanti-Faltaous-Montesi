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

    // TODO: add doc
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

    // TODO: add doc
    @Test
    public void testToJsonHeatseeker() {
        String actual = WeaponFactory.toJson(mHeatseeker);

        String expected = Jsons.get("weapons/tests/raw_heatseeker");

        assertEquals(
                new JsonString(expected),
                new JsonString(actual)
        );
    }

    // TODO: add doc
    @Test
    public void testFromJsonHeatseeker() {
        Weapon actual = WeaponFactory.fromJson(Jsons.get("weapons/tests/raw_heatseeker"));

        Weapon expected = mHeatseeker;

        assertEquals(expected, actual);
    }
}
