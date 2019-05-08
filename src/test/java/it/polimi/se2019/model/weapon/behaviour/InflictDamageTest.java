package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.DamageAction;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.weapon.Selection;
import it.polimi.se2019.util.Jsons;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class InflictDamageTest {
    ShootContext mGiorgioShootsMario;

    @Before
    public void instantiate() {
        mGiorgioShootsMario = new ShootContext(
                Board.fromJson(Jsons.get("boards/tests/simple_board")),
                new HashSet(Arrays.asList(
                        new Player("Giorgio", PlayerColor.BLUE),
                        new Player("Mario", PlayerColor.GREEN)
                )),
                PlayerColor.BLUE
        );
    }

    @Test
    public void testEval() {
        // inflict 1 damage to Mario
        InflictDamage tested = new InflictDamage(
                new DamageLiteral(new Damage(1, 0)),
                new TargetsLiteral(Selection.fromSingle(PlayerColor.GREEN))
        );

        // do it
        Expression actual = tested.eval(mGiorgioShootsMario);

        // check it
        Expression expected = new ActionLiteral(new DamageAction(
                PlayerColor.BLUE,
                Selection.fromSingle(PlayerColor.GREEN),
                new Damage(1, 0)
        ));
        assertEquals(expected, actual);
    }
}
