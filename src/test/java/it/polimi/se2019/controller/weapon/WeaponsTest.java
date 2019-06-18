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
import it.polimi.se2019.view.request.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.stubbing.Stubber;

import java.util.*;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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
    private void assertPlayerPosition(Player player, Position newPosition) {
        assertEquals(player.getPos(), newPosition);
    }
    private void assertPlayerStatus(Player affectedPlayer,
                                    List<PlayerColor> damageInflicted,
                                    List<Pair<PlayerColor, Integer>> inflictedMarks,
                                    Position newPosition) {
        assertPlayerDamage(affectedPlayer, damageInflicted, inflictedMarks);
        assertPlayerPosition(affectedPlayer, newPosition);
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
    private void mockViewLogging(View viewMock) {
        doAnswer(invocationOnMock -> {
            mLogger.log(Level.SEVERE, "ERR FROM VIEW: {0}", (String) invocationOnMock.getArgument(0));
            return null;
        }).
                when(viewMock).reportError(anyString());

        doAnswer(invocationOnMock -> {
            mLogger.log(Level.INFO, "MSG FROM VIEW: {0}", (String) invocationOnMock.getArgument(0));
            return null;
        }).
                when(viewMock).showMessage(anyString());
    }
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

        stubber.when(viewMock).showTargetsSelectionView(anyInt(), anyInt(), anySet());
    }
    private void mockEffectSelections(View viewMock, Controller controller, List<List<String>> effectSelections) {
        if (effectSelections.isEmpty())
            throw new IllegalArgumentException();

        BiFunction<View, List<String>, Object> requestProvider = (mock, effectSelection) -> {
            controller.getShootInteraction().putRequest(new EffectsSelectedRequest(effectSelection, mock));
            return null;
        };

        Iterator<List<String>> itr = effectSelections.listIterator();
        List<String> firstEle = itr.next();
        Stubber stubber = doAnswer(invocationOnMock -> requestProvider.apply(
                (View) invocationOnMock.getMock(),
                firstEle
        ));
        while (itr.hasNext()) {
            List<String> ele = itr.next();
            stubber = stubber.doAnswer(invocationOnMock -> requestProvider.apply(
                    (View) invocationOnMock.getMock(),
                    ele
            ));
        }

        stubber.when(viewMock).showEffectsSelectionView(any(), anySet());
    }
    private void mockPositionSelections(View viewMock, Controller controller, List<Position> positionSelections) {
        if (positionSelections.isEmpty())
            throw new IllegalArgumentException();

        BiFunction<View, Position, Object> requestProvider = (mock, positionSelection) -> {
            controller.getShootInteraction().putRequest(new PositionSelectedRequest(positionSelection, mock));
            return null;
        };

        Iterator<Position> itr = positionSelections.listIterator();
        Position firstEle = itr.next();
        Stubber stubber = doAnswer(invocationOnMock -> requestProvider.apply(
                (View) invocationOnMock.getMock(),
                firstEle
        ));
        while (itr.hasNext()) {
            Position ele = itr.next();
            stubber = stubber.doAnswer(invocationOnMock -> requestProvider.apply(
                    (View) invocationOnMock.getMock(),
                    ele
            ));
        }

        stubber.when(viewMock).showPositionSelectionView(anySet());
    }
    private void mockModeSelections(View viewMock, Controller controller, List<String> modeSelections) {
        if (modeSelections.isEmpty())
            throw new IllegalArgumentException();

        BiFunction<View, String, Object> requestProvider = (mock, modeSelection) -> {
            controller.getShootInteraction().putRequest(new WeaponModeSelectedRequest(modeSelection, viewMock));
            return null;
        };

        Iterator<String> itr = modeSelections.listIterator();
        String firstEle = itr.next();
        Stubber stubber = doAnswer(invocationOnMock -> requestProvider.apply(
                (View) invocationOnMock.getMock(),
                firstEle
        ));
        while (itr.hasNext()) {
            String ele = itr.next();
            stubber = stubber.doAnswer(invocationOnMock -> requestProvider.apply(
                    (View) invocationOnMock.getMock(),
                    ele
            ));
        }

        stubber.when(viewMock).showWeaponModeSelectionView(any(Effect.class), any(Effect.class));
    }
    private void mockSelections(Controller controller, Request... requestsInOrder) {
        controller.getShootInteraction().getRequestQueue().addAll(Arrays.asList(requestsInOrder));
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
    public void instantiateTestGames() {
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
                        new Player("Smurfette", PlayerColor.BLUE, new Position(2, 2)),
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
        inOrder.verify(viewMock).showTargetsSelectionView(1, 1, Collections.singleton(PlayerColor.GREEN));

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

        // shoot to Luigi with basic effect and to Smurfette with using the second lock
        mockTargetSelections(viewMock, testController, Arrays.asList(
                Collections.singleton(PlayerColor.GREEN),
                Collections.singleton(PlayerColor.BLUE)
        ));

        // use second lock to shoot Smurfette
        mockEffectSelections(viewMock, testController, Collections.singletonList(
                Collections.singletonList("with_second_lock")
        ));

        // shoot through controller
        testController.startShootInteraction(viewMock, PlayerColor.YELLOW, lockrifle.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // verify order of mock method calls
        InOrder inOrder = inOrder(viewMock);
        inOrder.verify(viewMock).showTargetsSelectionView(1, 1, allTargets(PlayerColor.YELLOW));
        inOrder.verify(viewMock).showEffectsSelectionView(any(), anySet());
        inOrder.verify(viewMock).showTargetsSelectionView(1, 1, allTargetsExcept(
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
        Weapon testedWeapon = Weapons.get("electroscythe");

        // create mock view
        View viewMock = mock(View.class);

        // mock effect selection
        mockModeSelections(viewMock, testController,  Collections.singletonList(
                "in_reaper_mode"
        ));

        // shoot through controller
        testController.startShootInteraction(viewMock, PlayerColor.PURPLE, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // verify order of mock method calls
        InOrder inOrder = inOrder(viewMock);
        inOrder.verify(viewMock).showWeaponModeSelectionView(any(Effect.class), any(Effect.class));

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

    @Test
    public void testVortexCannonBasicModeOnlyLuigiSucksMarioIntoVortex() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty);

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("vortex_cannon");

        // create mock view
        View viewMock = mock(View.class);

        // mock position selections
        mockPositionSelections(viewMock, testController, Collections.singletonList(
                new Position(2, 2) // vortex position
        ));

        // mock target selections
        mockTargetSelections(viewMock, testController, Collections.singletonList(
                Collections.singleton(PlayerColor.PURPLE)
        ));

        // mock effect selection
        mockEffectSelections(viewMock, testController,  Collections.singletonList(
                Collections.emptyList() // do not pick with black hole
        ));

        // shoot through controller
        testController.startShootInteraction(viewMock, PlayerColor.GREEN, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // verify order of mock method calls
        InOrder inOrder = inOrder(viewMock);
        inOrder.verify(viewMock).showPositionSelectionView(anySet());
        inOrder.verify(viewMock).showTargetsSelectionView(anyInt(), anyInt(), anySet());
        inOrder.verify(viewMock).showEffectsSelectionView(any(), anySet());

        // assert that Mario is hurt and has moved to new position
        assertPlayerStatus(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.PURPLE),
                Arrays.asList(
                        PlayerColor.GREEN,
                        PlayerColor.GREEN
                ),
                Collections.emptyList(),
                new Position(2, 2)
        );
    }

    @Test
    public void testWhisperSmurfetteSnipesLuigi() {
         // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty);

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("whisper");

        // create mock view
        View viewMock = mock(View.class);

        // select Luigi
        mockTargetSelections(viewMock, testController, Collections.singletonList(
                Collections.singleton(PlayerColor.GREEN)
        ));

        // shoot through controller
        testController.startShootInteraction(viewMock, PlayerColor.BLUE, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // verify order of mock method calls
        InOrder inOrder = inOrder(viewMock);
        inOrder.verify(viewMock).showTargetsSelectionView(anyInt(), anyInt(), anySet());

        // assert that Mario is hurt and has moved to new position
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREEN),
                Arrays.asList(
                        PlayerColor.BLUE,
                        PlayerColor.BLUE,
                        PlayerColor.BLUE
                ),
                Collections.singletonList(
                        new Pair<>(PlayerColor.BLUE, 1)
                )
        );
    }

    @Test
    public void testHellionStonesHurtsMarioAndThenNanoTracesMarioAndLuigi() {
          // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty);

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("hellion");

        // create mock view
        View viewMock = mock(View.class);

        // choose nano-tracer mode
        mockModeSelections(viewMock, testController, Collections.singletonList(
                "in_nano-tracer_mode"
        ));

        // select Mario for the hurting (Luigi will be hurt automatically)
        mockTargetSelections(viewMock, testController, Collections.singletonList(
                Collections.singleton(PlayerColor.PURPLE)
        ));

        // shoot through controller
        testController.startShootInteraction(viewMock, PlayerColor.YELLOW, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // verify order of mock method calls
        InOrder inOrder = inOrder(viewMock);
        inOrder.verify(viewMock).showWeaponModeSelectionView(any(Effect.class), any(Effect.class));
        inOrder.verify(viewMock).showTargetsSelectionView(anyInt(), anyInt(), anySet());

        // assert that Mario is hurt and that Dorian has been inflicted marks
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.PURPLE),
                Collections.singletonList(
                        PlayerColor.YELLOW
                ),
                Collections.singletonList(
                        new Pair<>(PlayerColor.YELLOW, 2)
                )
        );
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREY),
                Collections.emptyList(),
                Collections.singletonList(
                        new Pair<>(PlayerColor.YELLOW, 2)
                )
        );
    }

    @Test
    public void testRocketLauncher() {
           // instantiate controller
        Controller testController = new Controller(mAllInOriginGame);

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("rocket_launcher");

        // create mock view
        View viewMock = mock(View.class);
        mockViewLogging(viewMock);

        // mock selection
        mockSelections(testController,
                // move downwards to the other room with rocket jump
                new EffectsSelectedRequest(Collections.singletonList("with_rocket_jump"), viewMock),
                new PositionSelectedRequest(new Position(0, 1), viewMock),

                // Shoot Mario as basic effect target
                new EffectsSelectedRequest(Collections.singletonList("basic_effect"), viewMock),
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.PURPLE), viewMock),

                // Use fragmenting warhead for extra mayhem
                new EffectsSelectedRequest(Collections.singletonList("with_fragmenting_warhead"), viewMock),

                // Move Mario on your square
                new EffectsSelectedRequest(Collections.singletonList("basic_effect_move"), viewMock),
                new PositionSelectedRequest(new Position(0, 1), viewMock)
        );

        // shoot through controller
        testController.startShootInteraction(viewMock, PlayerColor.GREEN, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // verify order of mock method calls
        // InOrder inOrder = inOrder(viewMock);
        // inOrder.verify(viewMock).showWeaponModeSelectionView(any(Effect.class), any(Effect.class));
        // inOrder.verify(viewMock).showTargetsSelectionView(anyInt(), anyInt(), anySet());

        // assert Luigi's rocket jump
        assertPlayerPosition(
                mAllInOriginGame.getPlayerFromColor(PlayerColor.GREEN),
                new Position(0, 1)
        );
        // assert that Mario is hurt more than the others and is on your square
        assertPlayerStatus(
                mAllInOriginGame.getPlayerFromColor(PlayerColor.PURPLE),
                Arrays.asList(
                        PlayerColor.GREEN,
                        PlayerColor.GREEN,
                        PlayerColor.GREEN
                ),
                Collections.emptyList(),
                new Position(0, 1)
        );
        // assert that everybody else was properly damaged by fragmenting warhead
        for (PlayerColor target : allTargetsExcept(PlayerColor.GREEN, PlayerColor.PURPLE))
            assertPlayerDamage(
                    mAllInOriginGame.getPlayerFromColor(target),
                    Collections.singletonList(
                            PlayerColor.GREEN
                    ),
                    Collections.emptyList()
            );
    }
}
