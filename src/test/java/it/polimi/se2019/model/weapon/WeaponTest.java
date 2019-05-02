package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.DamageAction;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.weapon.behaviour.Context;
import it.polimi.se2019.model.weapon.behaviour.TargetsLiteral;
import it.polimi.se2019.util.Jsons;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class WeaponTest {
    Game mSimpleGame;

    @Before
    public void instantiate() {
        mSimpleGame = new Game(
                Board.fromJson(Jsons.get("boards/tests/simple_board")),
                Arrays.asList(
                        new Player("Mario", PlayerColor.BLUE),
                        new Player("Giorgio", PlayerColor.GREEN)
                ),
                0 // TODO: ask what this is
        );
    }

    @Test
    public void testGenerateShootActionWhisper() {
        Weapon whisper = Weapons.makeWhisper();
        PlayerColor shooterColor = PlayerColor.BLUE;
        PlayerColor defenderColor = PlayerColor.GREEN;

        Context context = new Context(mSimpleGame, shooterColor);
        context.pushInfo(new TargetsLiteral(Collections.singleton(defenderColor)));

        Action actual = whisper.generateShootAction(context);

        Action expected = new DamageAction(
                shooterColor,
                Collections.singleton(defenderColor),
                new Damage(3, 1)
        );

        assertEquals(expected, actual);
    }
}
