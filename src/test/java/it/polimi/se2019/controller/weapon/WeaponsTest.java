package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.model.weapon.serialization.ExpressionFactory;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.util.Pair;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class WeaponsTest {
    private Logger mLogger = Logger.getLogger(getClass().getName());

    private Game mAllInOriginGame;
    private Game mLuigiHidesFromYellowParty;

    private PowerUpCard mBlueTagback;

    private final Map<PlayerColor, View> mPlayerViewMocks = new EnumMap<>(PlayerColor.class);

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
        assertEquals(newPosition, player.getPos());
    }
    private void assertPlayerAmmo(Player player, AmmoValue newAmmo) {
        assertEquals(player.getAmmo(), newAmmo);
    }
    private void assertPlayerStatus(Player affectedPlayer,
                                    List<PlayerColor> damageInflicted,
                                    List<Pair<PlayerColor, Integer>> inflictedMarks,
                                    Position newPosition,
                                    AmmoValue ammoValue) {
        assertPlayerStatus(affectedPlayer, damageInflicted, inflictedMarks, newPosition);
        assertPlayerAmmo(affectedPlayer, ammoValue);
    }
    private void assertPlayerStatus(Player affectedPlayer,
                                    List<PlayerColor> damageInflicted,
                                    List<Pair<PlayerColor, Integer>> inflictedMarks,
                                    Position newPosition) {
        assertPlayerDamage(affectedPlayer, damageInflicted, inflictedMarks);
        assertPlayerPosition(affectedPlayer, newPosition);
    }
    private void assertPlayerPowerups(Player player, List<PowerUpCard> powerUpCards) {
        assertArrayEquals(powerUpCards.toArray(new PowerUpCard[4]), player.getPowerUps());
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
    private void mockViewLogging(View viewMock, PlayerColor color) {
        doAnswer(invocationOnMock -> {
            mLogger.log(Level.SEVERE, "FROM {0} VIEW: {1}",
                    new Object[]{ color, invocationOnMock.getArgument(0) });
            return null;
        }).
                when(viewMock).reportError(anyString());

        doAnswer(invocationOnMock -> {
            mLogger.log(Level.INFO, "FROM {0} VIEW: {1}",
                    new Object[]{ color, invocationOnMock.getArgument(0) });
            return null;
        }).
                when(viewMock).showMessage(anyString());
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
        AmmoValue initialAmmo = new AmmoValue(3, 3, 3);
        mAllInOriginGame = new Game(
                Board.fromJson(Jsons.get("boards/game/board1")),
                new ArrayList<>(Arrays.asList(
                        new Player("Mario", PlayerColor.PURPLE, new Position(0, 0), initialAmmo),
                        new Player("Luigi", PlayerColor.GREEN, new Position(0, 0), initialAmmo),
                        new Player("Dorian", PlayerColor.GREY, new Position(0, 0), initialAmmo),
                        new Player("Smurfette", PlayerColor.BLUE, new Position(0, 0), initialAmmo),
                        new Player("Stones", PlayerColor.YELLOW, new Position(0, 0), initialAmmo)
                )),
                1
        );

        mLuigiHidesFromYellowParty = new Game(
                Board.fromJson(Jsons.get("boards/game/board1")),
                new ArrayList<>(Arrays.asList(
                        new Player("Mario", PlayerColor.PURPLE, new Position(3, 2), initialAmmo),
                        new Player("Luigi", PlayerColor.GREEN, new Position(2, 0), initialAmmo),
                        new Player("Dorian", PlayerColor.GREY, new Position(3, 2), initialAmmo),
                        new Player("Smurfette", PlayerColor.BLUE, new Position(2, 2), initialAmmo),
                        new Player("Stones", PlayerColor.YELLOW, new Position(2, 1), initialAmmo)
                )),
                1
        );
    }

    @Before
    public void instantiateTestViewMap() {
        Function<PlayerColor, View> makePlayerViewMock = (playerColor) -> {
            View viewMock = mock(View.class);

            mockViewLogging(viewMock, playerColor);

            return viewMock;
        };

        Arrays.stream(PlayerColor.values()).forEach(color ->
                mPlayerViewMocks.put(color, makePlayerViewMock.apply(color))
        );
    }

    @Before
    public void intantiateTestPowerups() {
        mBlueTagback = new PowerUpCard(PowerUpType.TAGBACK_GRENADE, new AmmoValue(0, 0, 1));
    }

    @Test
    public void testWeaponsGetAll() {
        System.out.println(Weapons.getAll());
    }

    @Test
    public void testWeaponsLoading() {
        // try to get a weapon and in doing so expect no exceptions
        System.out.println(Jsons.get("weapons/real/cyberblade"));
    }

    @Test
    public void testHeatseekerMarioShootsHiddenLuigi() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        PlayerColor shooterColor = PlayerColor.PURPLE;

        // NB. no selection mocking is necessary, since Luigi is the only target that cannot be seen by Mario

        // initiate shoot interaction and wait for it to end
        Weapon heatseeker = Weapons.get("heatseeker");
        testController.startShootInteraction(shooterColor, heatseeker.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

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
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        // instantiate weapon
        Weapon lockrifle = Weapons.get("lock_rifle");

        PlayerColor shooterColor = PlayerColor.YELLOW;

        // shoot to Luigi with basic effect and to Smurfette with using the second lock
        mockSelections(testController,
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.GREEN), shooterColor),
                new EffectsSelectedRequest(Collections.singletonList("with_second_lock"), shooterColor),
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.BLUE), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, lockrifle.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

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
        Controller testController = new Controller(mAllInOriginGame, mPlayerViewMocks);

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("electroscythe");

        PlayerColor shooterColor = PlayerColor.PURPLE;

        // mock effect selection
        mockSelections(testController,
                new WeaponModeSelectedRequest("in_reaper_mode", shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

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
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("vortex_cannon");

        PlayerColor shooterColor = PlayerColor.GREEN;

        // mock selections
        mockSelections(testController,
                // select vortex position
                new PositionSelectedRequest(new Position(2, 2), shooterColor),
                // select target to suck into vortex
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.PURPLE), shooterColor),
                // do not pick black hole
                new EffectsSelectedRequest(Collections.emptyList(), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // assert that Mario is hurt and has moved to a new position
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
    public void testWhisperSmurfetteCannotSeeLuigiAndTheShootInteractionIsAutomaticallyUndone() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("whisper");

        PlayerColor shooterColor = PlayerColor.BLUE;

        // no selections can be done

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // assert that everyone is without damage and in their original position
        for (Player pl : mLuigiHidesFromYellowParty.getPlayers())
            assertPlayerDamage(pl,
                    Collections.emptyList(),
                    Collections.emptyList()
            );
    }

    @Test
    public void testHellionStonesHurtsMarioAndThenNanoTracesMarioAndLuigi() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("hellion");

        PlayerColor shooterColor = PlayerColor.YELLOW;

        // mock selection
        mockSelections(testController,
                // choose nano-tracer mode
                new WeaponModeSelectedRequest("in_nano-tracer_mode", shooterColor),

                // select Mario for the hurting (Luigi will be hurt automatically)
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.PURPLE), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

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
    public void testMachineGunNotEnoughAmmoToPayFragmentingWarheadSoPlayersUndoesShootInteraction() {
        // instantiate controller
        Controller testController = new Controller(mAllInOriginGame, mPlayerViewMocks);
        mAllInOriginGame.getPlayerFromColor(PlayerColor.GREEN).setAmmo(new AmmoValue(0, 0, 1));

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("rocket_launcher");

        PlayerColor shooterColor = PlayerColor.GREEN;

        // mock selection
        mockSelections(testController,
                // move downwards to the other room with rocket jump
                new EffectsSelectedRequest(Collections.singletonList("with_rocket_jump"), shooterColor),
                new PositionSelectedRequest(new Position(0, 1), shooterColor),

                // Shoot Mario as basic effect target
                new EffectsSelectedRequest(Collections.singletonList("basic_effect"), shooterColor),
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.PURPLE), shooterColor),

                // Use fragmenting warhead for extra mayhem
                new EffectsSelectedRequest(Collections.singletonList("with_fragmenting_warhead"), shooterColor),

                // realize you don't have to ammo and undo the shoot interaction...
                new UndoWeaponInteractionRequest(shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // assert that nobody was affected by Luigi's outburst (because of undo)
        for (PlayerColor target : PlayerColor.values()) {
            assertPlayerStatus(
                    mAllInOriginGame.getPlayerFromColor(PlayerColor.PURPLE),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    new Position(0, 0),
                    new AmmoValue(3, 3, 3)
            );
        }
    }

    @Test
    public void testRocketLauncherLuigiSidestepsShootsMarioAndDecimatesOrigin() {
        // instantiate controller
        Controller testController = new Controller(mAllInOriginGame, mPlayerViewMocks);

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("rocket_launcher");

        PlayerColor shooterColor = PlayerColor.GREEN;

        // mock selection
        mockSelections(testController,
                // move downwards to the other room with rocket jump
                new EffectsSelectedRequest(Collections.singletonList("with_rocket_jump"), shooterColor),
                new PositionSelectedRequest(new Position(0, 1), shooterColor),

                // Shoot Mario as basic effect target
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.PURPLE), shooterColor),

                // Use fragmenting warhead for extra mayhem
                new EffectsSelectedRequest(Collections.singletonList("with_fragmenting_warhead"), shooterColor),

                // Move Mario on your square
                new EffectsSelectedRequest(Collections.singletonList("basic_effect_move"), shooterColor),
                new PositionSelectedRequest(new Position(0, 1), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

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

    @Test
    public void testTractorBeamMarioDragsLuigiOutOfHiding() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREEN).move(new Position(1, 0));

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("tractor_beam");

        PlayerColor shooterColor = PlayerColor.PURPLE;

        // mock selection
        mockSelections(testController,
                // drag Luigi into yellow room with basic effect
                new WeaponModeSelectedRequest("basic_mode", shooterColor),
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.GREEN), shooterColor),
                new PositionSelectedRequest(new Position(2, 1), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // assert that Luigi has been hazardously dragged into the yellow room
        assertPlayerStatus(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREEN),
                Collections.singletonList(
                        PlayerColor.PURPLE
                ),
                Collections.emptyList(),
                new Position(2, 1)
        );
    }

    @Test
    public void testFlamethrowerBasicModeSmurfetteRoastsStonesAndLuigi() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("flamethrower");

        PlayerColor shooterColor = PlayerColor.BLUE;

        // mock selection
        mockSelections(testController,
                // roast Luigi and Stones with basic mode
                new WeaponModeSelectedRequest("basic_mode", shooterColor),
                new DirectionSelectedRequest(Direction.NORTH, shooterColor),
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.YELLOW), shooterColor),
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.GREEN), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // assert that Luigi and Stones have been roasted thoroughly
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREEN),
                Collections.singletonList(
                        PlayerColor.BLUE
                ),
                Collections.emptyList()
        );
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.YELLOW),
                Collections.singletonList(
                        PlayerColor.BLUE
                ),
                Collections.emptyList()
        );
    }

    @Test
    public void testFlamethrowerBasicModeSmurfetteRoastsStonesOnly() {
        // create cutstom game
        AmmoValue initialAmmo = new AmmoValue(3, 3, 3);
        Game game = new Game(
                Board.fromJson(Jsons.get("boards/game/board4")),
                new ArrayList<>(Arrays.asList(
                        new Player("Smurfette", PlayerColor.BLUE, new Position(3, 2), initialAmmo),
                        new Player("Stones", PlayerColor.YELLOW, new Position(3, 1), initialAmmo),
                        new Player("Luigi", PlayerColor.GREEN, new Position(3, 1), initialAmmo)
                )),
                8
        );

        // instantiate controller
        Controller testController = new Controller(game, mPlayerViewMocks);

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("flamethrower");

        PlayerColor shooterColor = PlayerColor.BLUE;

        // mock selection
        mockSelections(testController,
                // roast Luigi
                new WeaponModeSelectedRequest("basic_mode", shooterColor),
                new DirectionSelectedRequest(Direction.NORTH, shooterColor),
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.YELLOW), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // assert that Stones has been roasted thoroughly
        assertPlayerDamage(
                game.getPlayerFromColor(PlayerColor.YELLOW),
                Collections.singletonList(
                        PlayerColor.BLUE
                ),
                Collections.emptyList()
        );
    }

    @Test
    public void testFlamethrowerBarbecueModeSmurfetteRoastsSomeMore() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.BLUE)
                .setAmmo(new AmmoValue(0, 1, 0))
                .addPowerUp(new PowerUpCard(PowerUpType.TAGBACK_GRENADE, new AmmoValue(0, 1, 0)));
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREY).move(new Position(2, 1));

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("flamethrower");

        PlayerColor shooterColor = PlayerColor.BLUE;

        // mock selection
        mockSelections(testController,
                // roast Stones, Dorian and then Luigi
                new WeaponModeSelectedRequest("in_barbecue_mode", shooterColor),
                new PowerUpDiscardedRequest(new boolean[]{ false, true, false, false }, shooterColor),
                new PowerUpDiscardedRequest(new boolean[]{ true, false, false, false }, shooterColor),
                new DirectionSelectedRequest(Direction.NORTH, shooterColor),
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.GREEN), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // assert roasting
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREY),
                Arrays.asList(
                        PlayerColor.BLUE,
                        PlayerColor.BLUE
                ),
                Collections.emptyList()
        );
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.YELLOW),
                Arrays.asList(
                        PlayerColor.BLUE,
                        PlayerColor.BLUE
                ),
                Collections.emptyList()
        );
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREEN),
                Collections.singletonList(
                        PlayerColor.BLUE
                ),
                Collections.emptyList()
        );
    }

    @Test
    public void testFlamethrowerPrematureUndoWhenPickingDirection() {
        // instantiate controller
        Controller testController = new Controller(mAllInOriginGame, mPlayerViewMocks);

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("flamethrower");

        PlayerColor shooterColor = PlayerColor.BLUE;

        // mock selection
        mockSelections(testController,
                // roast Stones, Dorian and then Luigi
                new WeaponModeSelectedRequest("in_barbecue_mode", shooterColor),
                new UndoWeaponInteractionRequest(shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // verify that paying the cost of barbecue mode has been undone
        assertPlayerAmmo(
                mAllInOriginGame.getPlayerFromColor(PlayerColor.BLUE),
                new AmmoValue(3, 3, 3)
        );
    }

    @Test
    public void testCyberbladeSmurfetteShadowStepsAndCutsMarioAndGreyUsingATargetingScopeOnEach() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        PlayerColor shooterColor = PlayerColor.BLUE;

        // give out powerups
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.BLUE)
                .addPowerUp(new PowerUpCard(PowerUpType.TARGETING_SCOPE, new AmmoValue(0, 0, 1)))
                .addPowerUp(new PowerUpCard(PowerUpType.TARGETING_SCOPE, new AmmoValue(1, 0, 0)));
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.YELLOW)
                .addPowerUp(new PowerUpCard(PowerUpType.TAGBACK_GRENADE, new AmmoValue(0, 1, 0)));

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("cyberblade");

        // mock selection
        mockSelections(testController,
                // move onto the square where Mario and Dorian are with shadowstep
                new EffectsSelectedRequest(Collections.singletonList("with_shadowstep"), shooterColor),
                new PositionSelectedRequest(new Position(3, 2), shooterColor),

                // use the basic effect to whack Mario
                new EffectsSelectedRequest(Collections.singletonList("basic_effect"), shooterColor),
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.PURPLE), shooterColor),

                // do even more damage to Mario with the targeting scope
                //  NB. target selection is skipped since only Mario was damaged
                new PowerUpsSelectedRequest(Collections.singletonList(0), shooterColor),
                new AmmoColorSelectedRequest(TileColor.BLUE, shooterColor),
                new PowerUpsSelectedRequest(Collections.emptyList(), shooterColor),

                // whack stones with slice and dice
                // NB. selection is skipped
                new EffectsSelectedRequest(Collections.singletonList("with_slice_and_dice"), shooterColor),

                // use last targeting scope on Dorian
                new PowerUpsSelectedRequest(Collections.singletonList(1), shooterColor),
                new AmmoColorSelectedRequest(TileColor.RED, shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // verify shadowstep movement
        assertPlayerPosition(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.BLUE),
                new Position(3, 2)
        );

        // verify whacking damage and targeting scope damage (1 additional to each)
        for (PlayerColor target : Arrays.asList(PlayerColor.PURPLE, PlayerColor.GREY))
            assertPlayerDamage(
                    mLuigiHidesFromYellowParty.getPlayerFromColor(target),
                    Arrays.asList(
                            PlayerColor.BLUE,
                            PlayerColor.BLUE,
                            PlayerColor.BLUE
                    ),
                    Collections.emptyList()
            );
    }

    @Test
    public void testThorMarioUsesChainToZapHiddenLuigiThroughStonesAlsoStonesUsesATagbackAndLuigiCannotBecauseHeCantSeeMario() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREEN).move(new Position(1, 0));


        PlayerColor shooterColor = PlayerColor.PURPLE;

        // give out powerups
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.PURPLE)
                .setAmmo(new AmmoValue(0, 0, 0))
                .addPowerUp(mBlueTagback)
                .addPowerUp(mBlueTagback)
                .addPowerUp(mBlueTagback);
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.YELLOW)
                .addPowerUp(mBlueTagback);
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREEN)
                .addPowerUp(mBlueTagback);

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("thor");

        // mock selection
        mockSelections(testController,
                // damage Stones with basic effect
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.YELLOW), shooterColor),

                // Stones counters with tagback activation
                new PowerUpsSelectedRequest(Collections.singletonList(0), PlayerColor.YELLOW),

                // damage Luigi with chain reaction
                //  NB. A powerup needs to be discarded to pay for the effect
                //  NB. Luigi cannot respond with his tagback since he can't see Mario
                new EffectsSelectedRequest(Collections.singletonList("with_chain_reaction"), shooterColor),
                new PowerUpDiscardedRequest(new boolean[] {true, false, false, false}, shooterColor),
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.GREEN), shooterColor),

                // do not select high voltage
                new EffectsSelectedRequest(Collections.emptyList(), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // verify damage caused but the Thor's effects
        {
            final List<PlayerColor> targets = Arrays.asList(PlayerColor.GREEN, PlayerColor.YELLOW);
            IntStream.range(0, 2)
                    .forEach(i ->
                            assertPlayerDamage(
                                    mLuigiHidesFromYellowParty.getPlayerFromColor(targets.get(i)),
                                    Stream.generate(() -> PlayerColor.PURPLE).limit(i + 1).collect(Collectors.toList()),
                                    Collections.emptyList()
                            )
                    );
        }

        // verify tagback damage to Mario
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.PURPLE),
                Collections.emptyList(),
                Collections.singletonList(
                        new Pair<>(PlayerColor.YELLOW, 1)
                )
        );

        // verify discarded powerup
        assertPlayerPowerups(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.PURPLE),
                Arrays.asList(
                        null,
                        mBlueTagback,
                        mBlueTagback
                )
        );
    }

    @Test
    public void testPlasmaGunSmurfettePicksWrongPositionWithPhaseGlide() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        PlayerColor shooterColor = PlayerColor.BLUE;

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("plasma_gun");

        // mock selection
        mockSelections(testController,
                // try to go too far using phase glide
                new EffectsSelectedRequest(Collections.singletonList("with_phase_glide"), shooterColor),
                new PositionSelectedRequest(new Position(1, 0), shooterColor),

                // immediately undo to keep this test simple
                new UndoWeaponInteractionRequest(shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // verify that an error has been issued to the user
        // TODO: find another way to check this (maybe using spy?)
        // verify(mPlayerViewMocks.get(shooterColor), times(1)).showMessage(anyString());

        // verify that Smurfette hasn't moved
        assertPlayerPosition(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.BLUE),
                new Position(2, 2)
        );
    }

    @Test
    public void testFurnaceLuigiRoastsEveryoneInYellowRoom() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        PlayerColor shooterColor = PlayerColor.GREEN;

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("furnace");

        // mock selection
        mockSelections(testController,
                // roast everyone in yellow room
                new WeaponModeSelectedRequest("basic_effect", shooterColor),
                new RoomSelectedRequest(TileColor.YELLOW, shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // very that everyone in yellow rooms has been damaged
        for (PlayerColor target : allTargets(PlayerColor.GREEN))
            assertPlayerDamage(
                    mLuigiHidesFromYellowParty.getPlayerFromColor(target),
                    Collections.singletonList(
                            PlayerColor.GREEN
                    ),
                    Collections.emptyList()
            );
    }

    @Test
    public void testZX2SmurfetteUsesInScannerModeOnMarioAndDorian() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        PlayerColor shooterColor = PlayerColor.BLUE;

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("zx-2");

        // mock selection
        mockSelections(testController,
                // shoot to Mario and Dorian (refrain from selecting 3 people)
                new WeaponModeSelectedRequest("in_scanner_mode", shooterColor),
                new TargetsSelectedRequest(
                        new HashSet<>(Arrays.asList(PlayerColor.PURPLE, PlayerColor.GREY)),
                        shooterColor
                )
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // very that everyone in yellow rooms has been damaged
        for (PlayerColor target : Arrays.asList(PlayerColor.PURPLE, PlayerColor.GREY))
            assertPlayerDamage(
                    mLuigiHidesFromYellowParty.getPlayerFromColor(target),
                    Collections.emptyList(),
                    Collections.singletonList(
                            new Pair<>(PlayerColor.BLUE, 1)
                    )
            );
    }

    @Test
    public void testGrenadeLauncherSmurfetteUsesAdditionalGrenadeOnMarioAndDorianAndThenShootsStones() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        PlayerColor shooterColor = PlayerColor.BLUE;

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("grenade_launcher");

        // mock selection
        mockSelections(testController,
                // shoot Dorian w/ basic effect
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.YELLOW), shooterColor),

                // shoot to Mario and Dorian (refrain from selecting 3 people)
                new EffectsSelectedRequest(Collections.singletonList("with_extra_grenade"), shooterColor),
                new PositionSelectedRequest(new Position(3, 2), shooterColor),

                // move Dorian
                new EffectsSelectedRequest(Collections.singletonList("basic_effect_move"), shooterColor),
                new PositionSelectedRequest(new Position(3, 1 ), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // very that everyone in yellow rooms has been damaged
        for (PlayerColor target : Arrays.asList(PlayerColor.PURPLE, PlayerColor.YELLOW, PlayerColor.GREY))
            assertPlayerDamage(
                    mLuigiHidesFromYellowParty.getPlayerFromColor(target),
                    Collections.singletonList(
                            PlayerColor.BLUE
                    ),
                    Collections.emptyList()
            );
    }

    @Test
    public void testGrenadeLauncherSkipEffect() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.PURPLE).move(new Position(0, 0));
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREY).move(new Position(0, 0));

        PlayerColor shooterColor = PlayerColor.BLUE;

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("grenade_launcher");

        // mock selection
        mockSelections(testController,
                // shoot Stones w/ basic effect

                // shoot Stones again
                new EffectsSelectedRequest(Collections.singletonList("with_extra_grenade"), shooterColor),
                new PositionSelectedRequest(new Position(2, 1), shooterColor),

                // move Stones
                new EffectsSelectedRequest(Collections.singletonList("basic_effect_move"), shooterColor),
                new PositionSelectedRequest(new Position(3, 1 ), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // very that everyone in yellow rooms has been damaged
        assertPlayerStatus(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.YELLOW),
                Arrays.asList(
                        PlayerColor.BLUE,
                        PlayerColor.BLUE
                ),
                Collections.emptyList(),
                new Position(3, 1)
        );
    }

    @Test
    public void testShotgunMarioShootsAndMovesDorian() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        PlayerColor shooterColor = PlayerColor.PURPLE;

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("shotgun");

        // mock selection
        mockSelections(testController,
                // shoot Dorian w/ basic effect (Luigi is picked immediately)
                new WeaponModeSelectedRequest("basic_mode", shooterColor),

                // move Dorian
                new EffectsSelectedRequest(Collections.singletonList("basic_mode_move"), shooterColor),
                new PositionSelectedRequest(new Position(2, 2), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // very that everyone in yellow rooms has been damaged
        assertPlayerStatus(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREY),
                Arrays.asList(
                        PlayerColor.PURPLE,
                        PlayerColor.PURPLE,
                        PlayerColor.PURPLE
                ),
                Collections.emptyList(),
                new Position(2, 2)
        );
    }

    @Test
    public void testShotgunMarioShootsDorian() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        PlayerColor shooterColor = PlayerColor.PURPLE;

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("shotgun");

        // mock selection
        mockSelections(testController,
                // shoot Dorian w/ basic effect (Luigi is picked immediately)
                new WeaponModeSelectedRequest("basic_mode", shooterColor),

                // do not move
                new EffectsSelectedRequest(Collections.emptyList(), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // very that everyone in yellow rooms has been damaged
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREY),
                Arrays.asList(
                        PlayerColor.PURPLE,
                        PlayerColor.PURPLE,
                        PlayerColor.PURPLE
                ),
                Collections.emptyList()
        );
    }

    @Test
    public void testPowerGloveRocketFistModeLuigiPunchesSmurfetteOnly() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        PlayerColor shooterColor = PlayerColor.GREEN;

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("power_glove");

        // mock selection
        mockSelections(testController,
                new WeaponModeSelectedRequest("in_rocket_fist_mode", shooterColor),

                // refrain from punching Stones, but still jump to his position
                new DirectionSelectedRequest(Direction.SOUTH, shooterColor),
                new EffectsSelectedRequest(Collections.emptyList(), shooterColor),

                // go on rocket-fisting; this time punch Smurfette
                new EffectsSelectedRequest(Collections.singletonList("second_rocket_jump"), shooterColor),
                new EffectsSelectedRequest(Collections.singletonList("second_punch"), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // very Smurfette's final position after two jumps
        assertPlayerPosition(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREEN),
                new Position(2, 2)
        );

        // assert damage of punch to Smurfette
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.BLUE),
                Arrays.asList(
                        PlayerColor.GREEN,
                        PlayerColor.GREEN
                ),
                Collections.emptyList()
        );
    }

    @Test
    public void testRailgunLuigiShootsStonesAndDorianWhichIsBehindAWall() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREEN).move(new Position(0, 1));
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.YELLOW).move(new Position(1, 1));
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREY).move(new Position(2, 1));

        PlayerColor shooterColor = PlayerColor.GREEN;

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("railgun");

        // mock selection
        mockSelections(testController,
                // Shoot Dorian and Luigi
                new WeaponModeSelectedRequest("in_piercing_mode", shooterColor),
                new DirectionSelectedRequest(Direction.EAST, shooterColor),
                new TargetsSelectedRequest(new HashSet<>(Arrays.asList(
                        PlayerColor.YELLOW, PlayerColor.GREY
                )), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // assert damage to Dorian and Luigi
        for (PlayerColor target : Arrays.asList(PlayerColor.YELLOW, PlayerColor.GREY))
            assertPlayerDamage(
                    mLuigiHidesFromYellowParty.getPlayerFromColor(target),
                    Arrays.asList(
                            PlayerColor.GREEN,
                            PlayerColor.GREEN
                    ),
                    Collections.emptyList()
            );
    }

    @Test
    public void testShockWaveBasicModeSmurfetteHurtsMarioAndStones() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        PlayerColor shooterColor = PlayerColor.BLUE;

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("shockwave");

        // mock selection
        mockSelections(testController,
                new WeaponModeSelectedRequest("basic_mode", shooterColor),

                // hurt Mario (first target selection is forced)
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.PURPLE), shooterColor),

                // decide to also hurt Stones
                new EffectsSelectedRequest(Collections.singletonList("second_selection"), shooterColor),

                // stop hurting people
                new EffectsSelectedRequest(Collections.emptyList(), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // assert damage to Dorian and Luigi
        for (PlayerColor target : Arrays.asList(PlayerColor.YELLOW, PlayerColor.PURPLE))
            assertPlayerDamage(
                    mLuigiHidesFromYellowParty.getPlayerFromColor(target),
                    Collections.singletonList(
                            PlayerColor.BLUE
                    ),
                    Collections.emptyList()
            );
    }

    @Test
    public void testSledgehammerSmurfettePushesLuigiAlongFullLengthOfCorridor() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.BLUE).move(new Position(0, 0));
        mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREEN).move(new Position(0, 0));

        PlayerColor shooterColor = PlayerColor.BLUE;

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("sledgehammer");

        // mock selection
        mockSelections(testController,
                // hurt and push Luigi
                new WeaponModeSelectedRequest("in_pulverize_mode", shooterColor),
                new DirectionSelectedRequest(Direction.EAST, shooterColor),
                new PositionSelectedRequest(new Position(3, 0), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // assert and position of Luigi
        assertPlayerStatus(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREEN),
                Arrays.asList(
                        PlayerColor.BLUE,
                        PlayerColor.BLUE,
                        PlayerColor.BLUE
                ),
                Collections.emptyList(),
                new Position(3, 0)
        );
    }

    @Test
    public void testMachineGunSmurfetteUsesBasicEffectOnMarioAndDorianThenFocusesOnMarioThenUsesTurretTripodOnStones() {
        // instantiate controller
        Controller testController = new Controller(mLuigiHidesFromYellowParty, mPlayerViewMocks);

        PlayerColor shooterColor = PlayerColor.BLUE;

        // instantiate weapon
        Weapon testedWeapon = Weapons.get("machine_gun");

        // mock selection
        mockSelections(testController,
                // use basic effect on Mario and Dorian
                new TargetsSelectedRequest(new HashSet<>(Arrays.asList(
                        PlayerColor.PURPLE, PlayerColor.GREY
                )), shooterColor),

                // use focus shot on Mario
                new EffectsSelectedRequest(Collections.singletonList("with_focus_shot"), shooterColor),
                new TargetsSelectedRequest(Collections.singleton(PlayerColor.PURPLE), shooterColor),

                // use turret tripod 1 on Mario
                new EffectsSelectedRequest(Collections.singletonList("with_turret_tripod"), shooterColor),
                new EffectsSelectedRequest(Collections.singletonList("with_turret_tripod_1"), shooterColor),

                // use turret tripod 2 on Stones
                //  NB. target selection should be automatic
                new EffectsSelectedRequest(Collections.singletonList("with_turret_tripod_2"), shooterColor)
        );

        // shoot through controller
        testController.startShootInteraction(shooterColor, testedWeapon.getBehaviour());
        waitForShootInteractionToEnd(testController.getShootInteraction());

        // assert damage
        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.PURPLE),
                Arrays.asList(
                        PlayerColor.BLUE,
                        PlayerColor.BLUE
                ),
                Collections.emptyList()
        );

        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.GREY),
                Arrays.asList(
                        PlayerColor.BLUE,
                        PlayerColor.BLUE
                ),
                Collections.emptyList()
        );

        assertPlayerDamage(
                mLuigiHidesFromYellowParty.getPlayerFromColor(PlayerColor.YELLOW),
                Collections.singletonList(
                        PlayerColor.BLUE
                ),
                Collections.emptyList()
        );
    }
}

