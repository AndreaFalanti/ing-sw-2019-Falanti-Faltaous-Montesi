package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.DamageAction;
import it.polimi.se2019.model.action.WeaponAction;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.weapon.behaviour.ShootContext;
import it.polimi.se2019.model.weapon.behaviour.TargetsLiteral;
import it.polimi.se2019.util.Jsons;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class WeaponsTest {
    private ShootContext mMarioBrosContext;

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
        mMarioBrosContext.pushCollectedInfo(new TargetsLiteral(Selection.fromSingle(PlayerColor.GREEN)));
        
        // produce action with complete context
        Optional<Action> maybeActual = heatseeker.shoot(mMarioBrosContext);

        // assert presence (since info is complete)
        assertTrue(maybeActual.isPresent());

        // test
        Action actual = maybeActual.get();
        Action expected = new WeaponAction(
                new DamageAction(
                        PlayerColor.PURPLE,
                        Collections.singleton(PlayerColor.BLUE),
                        new Damage(1, 0)
                )
        );
        assertEquals(expected, actual);
    }
}
