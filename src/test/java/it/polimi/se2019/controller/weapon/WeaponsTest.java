package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.controller.weapon.behaviour.ShootResult;
import it.polimi.se2019.controller.weapon.behaviour.TargetsLiteral;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.DamageAction;
import it.polimi.se2019.model.action.WeaponAction;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static junit.framework.TestCase.assertTrue;

public class WeaponsTest {
    private ShootContext mAllInOriginContext;

    @Before
    public void instantiate() {
        // mario bros
        mAllInOriginContext = new ShootContext(
                Board.fromJson(Jsons.get("boards/game/board1")),
                new HashSet(Arrays.asList(
                        new Player("Mario", PlayerColor.PURPLE, new Position(0, 0)),
                        new Player("Luigi", PlayerColor.GREEN, new Position(0, 0)),
                        new Player("Dorian", PlayerColor.GREY, new Position(0, 0)),
                        new Player("Smurfette", PlayerColor.BLUE, new Position(0, 0)),
                        new Player("Banano", PlayerColor.YELLOW, new Position(0, 0))
                )),
                PlayerColor.PURPLE
        );
    }

    @Test
    public void testHeatseekerMarioShootsLuigi() {
        // instantiate weapon
        Weapon heatseeker = Weapons.get("heatseeker");

        // provide needed information to shoot
        mAllInOriginContext.provideInfo(new TargetsLiteral(Collections.singleton(PlayerColor.GREEN)));

        // produce result with complete context
        ShootResult result = heatseeker.shoot(mAllInOriginContext);

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

    @Test
    public void testHeatseekerMarioShootsLuigiMissingTarget() {
         // instantiate weapon
        Weapon heatseeker = Weapons.get("heatseeker");

        // shoot
        Response actual = heatseeker.shoot(mAllInOriginContext).asResponse();

        // AtomicExpression expected = new InflictDamage(
                // new DamageLiteral(new Damage(3, 0)),
                // new WaitForInfo()
        // );
        Response expected = new TargetSelectionResponse(
                1,
                1,
                Collections.emptySet()
        );
        assertEquals(expected, actual);
    }

    @Test
    public void testLockRifleMarioShootsLuigiAndThenDorian() {
        // instantiate weapon
        Weapon lockrifle = Weapons.get("lock_rifle");

        // provide needed information to shoot
        mAllInOriginContext.provideInfo(Arrays.asList(
                new TargetsLiteral(Collections.singleton(PlayerColor.GREEN)),
                new TargetsLiteral(Collections.singleton(PlayerColor.GREY))
        ));

        // produce result with complete context
        ShootResult result = lockrifle.shoot(mAllInOriginContext);

        // assert that result is an action (since context was complete)
        assertTrue(result.isComplete());

        // test that action is correct
        Action actual = result.asAction();
        Action expected = new WeaponAction(
                new DamageAction(
                        PlayerColor.PURPLE,
                        Collections.singleton(PlayerColor.GREEN),
                        new Damage(2, 1)
                ),
                new DamageAction(
                        PlayerColor.PURPLE,
                        Collections.singleton(PlayerColor.GREY),
                        new Damage(0, 1)
                )
        );
        assertEquals(expected, actual);
    }
}
