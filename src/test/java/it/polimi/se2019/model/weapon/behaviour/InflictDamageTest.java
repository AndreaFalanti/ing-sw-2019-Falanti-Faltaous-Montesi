package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;

import java.util.Arrays;
import java.util.HashSet;

public class InflictDamageTest {
    ShootContext

    public void testEval() {
        InflictDamage tested = new InflictDamage(
                new DamageLiteral(new Damage(1, 0)),
                new TargetsLiteral(new HashSet(Arrays.asList(
                        new Player("Giorgio", PlayerColor.BLUE),
                        new Player("Mario", PlayerColor.GREEN)
                )))
        );

        Expression actual = tested.eval()
    }
}
