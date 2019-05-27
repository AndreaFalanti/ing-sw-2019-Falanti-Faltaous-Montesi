package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.DamageAction;
import it.polimi.se2019.model.action.WeaponAction;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.weapon.behaviour.*;
import it.polimi.se2019.util.Jsons;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class WeaponsTest {
    private ShootContext mMarioBrosContext;

    // utility function for checking optionals in tests
    public static <T> T failOptionalCheck(Class<T> toReturn) {
        assertTrue(false);
        return null;
    }

    @Before
    public void instantiate() {
        mMarioBrosContext = new ShootContext(
                Board.fromJson(Jsons.get("boards/game/board1")),
                new HashSet(Arrays.asList(
                        new Player("Mario", PlayerColor.PURPLE, new Position(0, 0)),
                        new Player("Luigi", PlayerColor.GREEN, new Position(0, 0))
                )),
                PlayerColor.PURPLE
        );
    }

    @Test
    public void testHeatseekerMarioShootsLuigi() {
        // instantiate weapon
        Weapon heatseeker = Weapons.get("heatseeker");

        // provide needed information to shoot
        mMarioBrosContext.provideInfo(new TargetsLiteral(Selection.fromSingle(PlayerColor.GREEN)));
        
        // produce result with complete context
        ShootResult result = heatseeker.shoot(mMarioBrosContext);

        // assert that result is an action (since context was complete)
        assertTrue(result.isComplete());

        // test that action is correct
        Action actual = result.asAction();
        Action expected = new WeaponAction(
                new DamageAction(
                        PlayerColor.PURPLE,
                        Collections.singleton(PlayerColor.GREEN),
                        new Damage(3, 0)
                )
        );
        assertEquals(expected, actual);
    }

    /*************************************************************/
    /* @Test                                                     */
    /* public void testHeatseekerMarioShootsLuigiMissingInfo() { */
    /*      // instantiate weapon                                */
    /*     Weapon heatseeker = Weapons.get("heatseeker");        */
    /*                                                           */
    /*     // shoot                                              */
    /*     Expression expected = new InflictDamage(              */
    /*             new DamageLiteral(new Damage(3, 0)),          */
    /*             new WaitForInfo()                             */
    /*             );                                            */
    /*     // assertEquals(expected, actual);                    */
    /* }                                                         */
    /*************************************************************/
}
