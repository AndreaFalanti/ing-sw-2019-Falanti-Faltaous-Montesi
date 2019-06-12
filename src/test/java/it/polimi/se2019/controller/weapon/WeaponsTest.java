package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.util.Pair;
import it.polimi.se2019.view.View;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class WeaponsTest {
    private Game mAllInOriginGame;
    private Game mLuigiHidesFromYellowParty;

    /**
     * Utility to assert that a player has been damaged correctly
     */
    private void assertPlayerDamage(Player damagedPlayer, List<PlayerColor> damage, Map<PlayerColor, Integer> marks) {
        assertArrayEquals(
                damage.toArray(new PlayerColor[12]),
                damagedPlayer.getDamageTaken()
        );
        assertEquals(
                Arrays.stream(PlayerColor.values())
                        .collect(Collectors.toMap(
                                clr -> clr,
                                clr -> marks.getOrDefault(clr, 0)
                        )),
                damagedPlayer.getMarks()
        );
    }

    @Before
    public void instantiate() {
        mAllInOriginGame = new Game(
                Board.fromJson(Jsons.get("boards/game/board1")),
                new ArrayList<>(Arrays.asList(
                        new Player("Mario", PlayerColor.PURPLE, new Position(0, 0)),
                        new Player("Luigi", PlayerColor.GREEN, new Position(0, 0)),
                        new Player("Dorian", PlayerColor.GREY, new Position(0, 0)),
                        new Player("Smurfette", PlayerColor.BLUE, new Position(0, 0)),
                        new Player("Banano", PlayerColor.YELLOW, new Position(0, 0))
                )),
                1
        );

        mLuigiHidesFromYellowParty = new Game(
                Board.fromJson(Jsons.get("boards/game/board1")),
                new ArrayList<>(Arrays.asList(
                        new Player("Mario", PlayerColor.PURPLE, new Position(3, 2)),
                        new Player("Luigi", PlayerColor.GREEN, new Position(2, 0)),
                        new Player("Dorian", PlayerColor.GREY, new Position(3, 2)),
                        new Player("Smurfette", PlayerColor.BLUE, new Position(3, 1)),
                        new Player("Banano", PlayerColor.YELLOW, new Position(2, 2))
                )),
                1
        );
    }

    @Test
    public void testWeaponsLoading() {
        // try to get a weapon and in doing so expect no exceptions
        Weapons.get(Weapons.listResourceNames().iterator().next());

        System.out.println(Weapons.get("rocket_launcher"));
    }

    @Test
    public void testHeatseekerMarioShootsHiddenLuigi() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty);

        /***************************************/
        /* instantiate and customize mock view */
        /***************************************/
        View viewMock = mock(View.class, withSettings().verboseLogging());
        // choose Luigi
        given(viewMock
                .selectTargets(1, 1, Collections.singleton(PlayerColor.GREEN)))
                .willReturn(Collections.singleton(PlayerColor.GREEN));

        // instantiate weapon
        Weapon heatseeker = Weapons.get("heatseeker");

        // produce result with complete context
        // TODO: wire to mock view
        testController.shoot(viewMock, PlayerColor.PURPLE, heatseeker.getBehaviour());

        // assert that Luigi was hurt
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREEN),
                Arrays.asList(
                        PlayerColor.PURPLE,
                        PlayerColor.PURPLE,
                        PlayerColor.PURPLE
                ),
                Collections.emptyMap()
        );
    }

    @Test
    public void testLockRifleMarioShootsLuigiAndThenDorian() {
        /*****************************************************************************/
        /* // instantiate controller                                                 */
        /* Controller testController = new Controller(mAllInOriginGame);             */
        /*                                                                           */
        /* // mock view                                                              */
        /* View view = mock(View.class);                                             */
        /*                                                                           */
        /* // instantiate weapon                                                     */
        /* Weapon lockrifle = Weapons.get("lock_rifle");                             */
        /*                                                                           */
        /* // provide needed information to shoot                                    */
        /* // TODO: use mock view for these                                          */
        /* // mAllInOriginGame.provideInfo(Arrays.asList(                            */
        /*         // new TargetsLiteral(Collections.singleton(PlayerColor.GREEN)),  */
        /*         // new TargetsLiteral(Collections.singleton(PlayerColor.GREY))    */
        /* // ));                                                                    */
        /*                                                                           */
        /* // produce result with complete context                                   */
        /* testController.shoot(view, PlayerColor.PURPLE, lockrifle.getBehaviour()); */
        /*                                                                           */
        /* // assert that luigi is hurt                                              */
        /* assertPlayerDamage(                                                       */
        /*         mAllInOriginGame.getPlayerFromColor(PlayerColor.GREEN),           */
        /*         Arrays.asList(                                                    */
        /*                 PlayerColor.PURPLE,                                       */
        /*                 PlayerColor.PURPLE                                        */
        /*         ),                                                                */
        /*         Arrays.asList(                                                    */
        /*                 new Pair(PlayerColor.PURPLE, 1)                           */
        /*         )                                                                 */
        /* );                                                                        */
        /*                                                                           */
        /* // assert that Dorian is hurt                                             */
        /* assertPlayerDamage(                                                       */
        /*         mAllInOriginGame.getPlayerFromColor(PlayerColor.GREY),            */
        /*         Arrays.asList(),                                                  */
        /*         Arrays.asList(                                                    */
        /*                 new Pair(PlayerColor.PURPLE, 1)                           */
        /*         )                                                                 */
        /* );                                                                        */
        /*****************************************************************************/
    }
}
