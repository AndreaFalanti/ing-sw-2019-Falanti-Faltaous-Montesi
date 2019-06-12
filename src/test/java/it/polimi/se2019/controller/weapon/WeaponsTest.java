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

    private PlayerColor mCurrentShooter;

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
    private void setShooter(PlayerColor shooter) {
        mCurrentShooter = shooter;
    }
    private Set<PlayerColor> targets(PlayerColor... colors) {
        Set<PlayerColor> targets = Arrays.stream(colors)
                .collect(Collectors.toSet());

        if (targets.contains(mCurrentShooter))
            throw new IllegalArgumentException("Cannot consider the shooter a target!");

        return targets;
    }
    private Set<PlayerColor> allTargets() {
        return Arrays.stream(PlayerColor.values())
                .filter(clr -> !clr.equals(mCurrentShooter))
                .collect(Collectors.toSet());
    }
    private Set<PlayerColor> thisTarget(PlayerColor target) {
        return targets(target);
    }
    private Set<PlayerColor> allTargetsExcept(PlayerColor... colors) {
        Set<PlayerColor> excludedTargets = targets(colors);

        return Arrays.stream(PlayerColor.values())
                .filter(clr -> !excludedTargets.contains(clr))
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
        Controller testController = new Controller(mAllInOriginGame);

        // instantiate weapon
        Weapon lockrifle = Weapons.get("lock_rifle");

        /***************************************/
        /* instantiate and customize mock view */
        /***************************************/
        View viewMock = mock(View.class);
        // choose to use "Basic effect" effect
        given(viewMock
                .selectEffects(any(), eq(0)))
                .willAnswer(thing -> {
                    return Collections.singleton("basic_effect");
                });
        // choose to use additional "With second lock" effect
        given(viewMock
                .selectEffects(any(), eq(1)))
                .willAnswer(thing -> {
                    return Collections.singleton("with_second_lock");
                });
        // choose Luigi
        given(viewMock
                .selectTargets(1, 1, allTargets()))
                .willReturn(thisTarget(PlayerColor.GREEN));
        // choose Dorian
        //   N.B. Luigi cannot be picked here
        given(viewMock
                .selectTargets(1, 1, allTargetsExcept(PlayerColor.GREEN)))
                .willReturn(thisTarget(PlayerColor.GREY));

        // produce result with complete context
        testController.shoot(viewMock, PlayerColor.YELLOW, lockrifle.getBehaviour());

        // assert that luigi is hurt
        assertPlayerDamage(
                mAllInOriginGame.getPlayerFromColor(PlayerColor.GREEN),
                Arrays.asList(
                        PlayerColor.YELLOW,
                        PlayerColor.YELLOW
                ),
                Arrays.asList(
                        new Pair(PlayerColor.YELLOW, 1)
                )
        );
        // assert that Dorian is hurt
        assertPlayerDamage(
                mAllInOriginGame.getPlayerFromColor(PlayerColor.GREY),
                Collections.emptyList(),
                Arrays.asList(
                        new Pair(PlayerColor.PURPLE, 1)
                )
        );
    }
}
