package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.DamageAction;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.weapon.behaviour.ShootContext;
import it.polimi.se2019.model.weapon.behaviour.TargetsLiteral;
import it.polimi.se2019.util.Jsons;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

public class WeaponsTest {
    private ShootContext mMarioBrosContext;

    @Before
    public void instantiate() {
        mMarioBrosContext = new ShootContext(
                Board.fromJson(Jsons.get("boards/game/board1")),
                new HashSet(Arrays.asList(
                        new Player("Mario", PlayerColor.PURPLE),
                        new Player("Luigi", PlayerColor.GREEN)
                )),
                PlayerColor.PURPLE
        );
    }

    @Test
    public void testHeatseekerMarioShootsLuigi() {
        // instantiate weapon
        Weapon heatseeker = Weapons.get("heatseeker");

        // provide needed information to shoot
        mMarioBrosContext.pushInfo(new TargetsLiteral(Selection.fromSingle(PlayerColor.GREEN)));
        
        // produce action with complete context
        WeaponAction actual = heatseeker.shoot(mMarioBrosContext);

        // test
        WeaponAction expected = new WeaponAction(
                new DamageAction(PlayerColor.PURPLE, PlayerColor.GREEN, new Damage(1, 0))
        );
        assertEquals(expected, actual);
    }
}
