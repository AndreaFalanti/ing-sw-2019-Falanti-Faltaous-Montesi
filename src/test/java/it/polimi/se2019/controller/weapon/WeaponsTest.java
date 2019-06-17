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
import it.polimi.se2019.view.request.EffectsSelectedRequest;
import it.polimi.se2019.view.request.TargetsSelectedRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.stubbing.Stubber;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class WeaponsTest {
    private Logger mLogger = Logger.getLogger(getClass().getName());

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

    /**
     * Utility functions for providing fake requests to a shoot interaction (used in mocking)
     */
    private void mockTargetSelections(View viewMock, Controller controller, List<Set<PlayerColor>> targetSelections) {
        if (targetSelections.isEmpty())
            throw new IllegalArgumentException();

        BiFunction<View, Set<PlayerColor>, Object> requestProvider = (mock, targetSelection) -> {
            controller.getShootInteraction().putRequest(new TargetsSelectedRequest(targetSelection, mock));
            return null;
        };

        Iterator<Set<PlayerColor>> itr = targetSelections.listIterator();
        Set<PlayerColor> firstEle = itr.next();
        Stubber stubber = doAnswer(invocationOnMock -> requestProvider.apply(
                (View) invocationOnMock.getMock(),
                firstEle
        ));
        while (itr.hasNext()) {
            Set<PlayerColor> ele = itr.next();
            stubber = stubber.doAnswer(invocationOnMock -> requestProvider.apply(
                    (View) invocationOnMock.getMock(),
                    ele
            ));
        }

        stubber.when(viewMock).selectTargets(anyInt(), anyInt(), anySet());
    }
    private void mockEffectSelections(View mockView, Controller controller, List<Set<String>> selectedEffects) {
        if (selectedEffects.isEmpty())
            throw new IllegalArgumentException();

        Function<Set<String>, Object> requestProvider = effectSelection -> {
            controller.getShootInteraction().putRequest(new EffectsSelectedRequest(effectSelection, mockView));
            return null;
        };

        Iterator<Set<String>> itr = selectedEffects.iterator();
        Stubber stubber = doAnswer(mock -> requestProvider.apply(itr.next()));
        while (itr.hasNext()) {
            Set<String> ele = itr.next();
            stubber = stubber.doAnswer(mock -> requestProvider.apply(ele));
        }

        stubber.when(mockView.selectEffects(any(), anyInt()));
    }

    private void waitForShootInteractionToEnd(ShootInteraction interaction) {
        synchronized (interaction.getLock()) {
            try {
                interaction.getLock().wait();
            } catch (InterruptedException e) {
                mLogger.warning("Test thread interrupted while waiting for shoot interaction to finish...");
            }
        }
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

        // create mock view
        View viewMock = mock(View.class);

        // mock target selection (pick poor hidden luigi)
        mockTargetSelections(viewMock, testController, Collections.singletonList(
                Collections.singleton(PlayerColor.GREEN)
        ));

        // initiate shoot interaction and wait for it to end
        Weapon heatseeker = Weapons.get("heatseeker");
        testController.startShootInteraction(viewMock, PlayerColor.PURPLE, heatseeker.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // verify order of mock view method calls
        InOrder inOrder = inOrder(viewMock);
        inOrder.verify(viewMock).selectTargets(1, 1, Collections.singleton(PlayerColor.GREEN));

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

        // create mock view
        View viewMock = mock(View.class);

        // mock effect selection
        // doAnswer(mock -> ,
                // Collections.singleton("basic_effect"),
                // Collections.singleton("with_second_lock")
        // )));

        // mock target selection (first Luigi, then Smurfette)
        given(viewMock.selectTargets(anyInt(), anyInt(), any()))
                .willReturn(Collections.singleton(PlayerColor.GREEN))
                .willReturn(Collections.singleton(PlayerColor.BLUE));

        // shoot through controller
        testController.startShootInteraction(viewMock, PlayerColor.YELLOW, lockrifle.getBehaviour());

        // verify order of mock method calls
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

    @Test
    public void testElectroscytheMarioScythesEveryone() {
        // instantiate controller
        Controller testController = new Controller(mAllInOriginGame);

        // instantiate weapon
        Weapon lockrifle = Weapons.get("electroscythe");

        // create mock view
        View viewMock = mock(View.class);

        // mock effect selection
        mockEffectSelections(viewMock, testController,  Collections.singletonList(
                Collections.singleton("in_reaper_mode")
        ));

        // shoot through controller
        testController.startShootInteraction(viewMock, PlayerColor.PURPLE, lockrifle.getBehaviour());

        // verify order of mock method calls
        InOrder inOrder = inOrder(viewMock);
        inOrder.verify(viewMock).selectEffects(any(), eq(0));

        // assert that everyone except Mario is hurt
        for (PlayerColor target : allTargets(PlayerColor.PURPLE)) {
            assertPlayerDamage(
                    mAllInOriginGame.getPlayerFromColor(target),
                    Arrays.asList(
                            PlayerColor.PURPLE,
                            PlayerColor.PURPLE
                    ),
                    Collections.emptyList()
            );
        }
    }
}
