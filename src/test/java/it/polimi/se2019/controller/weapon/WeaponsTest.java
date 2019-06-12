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
import org.mockito.InOrder;

import java.util.*;
import java.util.stream.Collectors;

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
    private void assertPlayerDamage(Player damagedPlayer,
                                    List<PlayerColor> damage,
                                    List<Pair<PlayerColor, Integer>> inflictedMarks) {
        Map<PlayerColor, Integer> inflictedMarksMap =  inflictedMarks.stream()
                .collect(Collectors.toMap(
                        Pair::getFirst,
                        Pair::getSecond
                ));
        Map<PlayerColor, Integer> marks = Arrays.stream(PlayerColor.values())
                .collect(Collectors.toMap(
                        clr -> clr,
                        clr -> inflictedMarksMap.getOrDefault(clr, 0)
                ));

        assertArrayEquals(
                damage.toArray(new PlayerColor[12]),
                damagedPlayer.getDamageTaken()
        );
        assertEquals(
                marks,
                damagedPlayer.getMarks()
        );
    }

    /**
     * Utility function to quickly construct target sets
     */
    private Set<PlayerColor> targets(PlayerColor shooter, PlayerColor... colors) {
        Set<PlayerColor> targets = Arrays.stream(colors)
                .collect(Collectors.toSet());

        if (targets.contains(shooter))
            throw new IllegalArgumentException("Cannot consider the shooter a target!");

        return targets;
    }
    private Set<PlayerColor> allTargets(PlayerColor shooter) {
        return Arrays.stream(PlayerColor.values())
                .filter(clr -> !clr.equals(shooter))
                .collect(Collectors.toSet());
    }
    private Set<PlayerColor> allTargetsExcept(PlayerColor shooter, PlayerColor... colors) {
        Set<PlayerColor> excludedTargets = targets(shooter, colors);

        return Arrays.stream(PlayerColor.values())
                .filter(clr -> !excludedTargets.contains(clr) && !shooter.equals(clr))
                .collect(Collectors.toSet());
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
                        new Player("Stones", PlayerColor.YELLOW, new Position(0, 0))
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
                        new Player("Stones", PlayerColor.YELLOW, new Position(2, 1))
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
                Collections.emptyList()
        );
    }

    @Test
    public void testLockRifleStonesShootsLuigiAndThenSmurfette() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty);

        // instantiate weapon
        Weapon lockrifle = Weapons.get("lock_rifle");

        /***************************************/
        /* instantiate and customize mock view */
        /***************************************/
        View viewMock = mock(View.class);

        // choose effects
        given(viewMock.selectEffects(any(), anyInt()))
                .willReturn(Collections.singleton("basic_effect"))
                .willReturn(Collections.singleton("with_second_lock"));

        // choose targets (first Luigi, then Smurfette)
        given(viewMock.selectTargets(anyInt(), anyInt(), any()))
                .willReturn(Collections.singleton(PlayerColor.GREEN))
                .willReturn(Collections.singleton(PlayerColor.BLUE));


        // shoot through controller
        testController.shoot(viewMock, PlayerColor.YELLOW, lockrifle.getBehaviour());

        // verify order of method calls
        InOrder inOrder = inOrder(viewMock);
        inOrder.verify(viewMock).selectEffects(any(), eq(0));
        inOrder.verify(viewMock).selectTargets(1, 1, allTargets(PlayerColor.YELLOW));
        inOrder.verify(viewMock).selectEffects(any(), eq(1));
        inOrder.verify(viewMock).selectTargets(1, 1, allTargetsExcept(
                PlayerColor.YELLOW, PlayerColor.GREEN
        ));

        // assert that Luigi is hurt
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREEN),
                Arrays.asList(
                        PlayerColor.YELLOW,
                        PlayerColor.YELLOW
                ),
                Collections.singletonList(
                        new Pair<>(PlayerColor.YELLOW, 1)
                )
        );
        // assert that Smurfette is hurt
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.BLUE),
                Collections.emptyList(),
                Collections.singletonList(
                        new Pair<>(PlayerColor.YELLOW, 1)
                )
        );
    }
}
