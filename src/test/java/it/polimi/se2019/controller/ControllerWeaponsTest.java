package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.controller.weapon.Weapons;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ControllerWeaponsTest {
    private Game mAllInOriginGame;

    private void assertPlayerDamage(Player damagedPlayer, List<PlayerColor> damage, List<Pair<PlayerColor, Integer>> marks) {
        assertArrayEquals(
                damage.toArray(),
                damagedPlayer.getDamageTaken()
        );
        assertEquals(
                marks.stream()
                        .collect(Collectors.toMap(
                                Pair::getFirst,
                                Pair::getSecond
                        )),
                damagedPlayer.getMarks()
        );
    }

    @Before
    public void instantiate() {
        // mario bros
        mAllInOriginGame = new Game(
                Board.fromJson(Jsons.get("boards/game/board1")),
                new ArrayList<>(Arrays.asList(
                        new Player("Mario", PlayerColor.PURPLE, new Position(0, 0)),
                        new Player("Luigi", PlayerColor.GREEN, new Position(0, 0)),
                        new Player("Dorian", PlayerColor.GREY, new Position(0, 0)),
                        new Player("Smurfette", PlayerColor.BLUE, new Position(0, 0)),
                        new Player("Banano", PlayerColor.YELLOW, new Position(0, 0))
                )),
                0
        );
    }

    @Test
    public void testHeatseekerMarioShootsLuigi() {
        // instantiate controller with
        // TODO: wire to mock view
        Controller testController = new Controller(mAllInOriginGame);

        // instantiate weapon
        Weapon heatseeker = Weapons.get("heatseeker");

        // produce result with complete context
        testController.shoot(PlayerColor.PURPLE, heatseeker.getBehaviour());

        // assert that Luigi was hurt
        assertPlayerDamage(
                mAllInOriginGame.getPlayerFromColor(PlayerColor.GREEN),
                Arrays.asList(
                        PlayerColor.PURPLE,
                        PlayerColor.PURPLE,
                        PlayerColor.PURPLE
                ),
                Arrays.asList()
        );

    }

    @Test
    public void testLockRifleMarioShootsLuigiAndThenDorian() {
        // instantiate controller
        Controller testController = new Controller(mAllInOriginGame);

        // instantiate weapon
        Weapon lockrifle = Weapons.get("lock_rifle");

        // provide needed information to shoot
        // TODO: use mock view for these
        // mAllInOriginGame.provideInfo(Arrays.asList(
                // new TargetsLiteral(Collections.singleton(PlayerColor.GREEN)),
                // new TargetsLiteral(Collections.singleton(PlayerColor.GREY))
        // ));

        // produce result with complete context
        testController.shoot(PlayerColor.PURPLE, lockrifle.getBehaviour());

        // assert that luigi is hurt
        assertPlayerDamage(
                mAllInOriginGame.getPlayerFromColor(PlayerColor.GREEN),
                Arrays.asList(
                        PlayerColor.PURPLE,
                        PlayerColor.PURPLE
                ),
                Arrays.asList(
                        new Pair(PlayerColor.PURPLE, 1)
                )
        );

        // assert that Dorian is hurt
        assertPlayerDamage(
                mAllInOriginGame.getPlayerFromColor(PlayerColor.GREY),
                Arrays.asList(),
                Arrays.asList(
                        new Pair(PlayerColor.PURPLE, 1)
                )
        );
    }
}
