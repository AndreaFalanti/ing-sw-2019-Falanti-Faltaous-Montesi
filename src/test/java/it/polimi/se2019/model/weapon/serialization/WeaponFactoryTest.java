package it.polimi.se2019.model.weapon.serialization;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.controller.weapon.behaviour.*;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Damage;
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
        mHeatseeker = new Weapon("Heatseeker", new AmmoValue(2, 1, 0), new AmmoValue(1, 0, 0));
        mHeatseeker.setBehaviour(
                new InflictDamage(
                        new DamageLiteral(
                                new Damage(3, 0)
                        ),
                        new SelectOneTarget(
                                new NegateSelection(
                                        new CanSee()
                                )
                        )
                )
        );
    }

    // TODO: add doc
    @Test
    public void testFromJsonHeatseeker() {
        Weapon actual = WeaponFactory.fromJson(Jsons.get("weapons/real/heatseeker"));

        Weapon expected = mHeatseeker;

        assertEquals(expected, actual);
    }
}
