package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.controller.weapon.expression.ShootUndoInfo;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.action.AmmoPayment;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.util.ArrayUtils;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class ShootInteraction {
    // logger
    private Logger mLogger = Logger.getLogger(getClass().getName());

    // constants
    private final int MAX_POWERUP_DISCARDS = 3;
    private final int MAX_POWERUPS_IN_HAND = 3;
    private static final int REQUEST_ACCEPTANCE_TIMEOUT = 1000; // amount of time spent waiting for a request in seconds

    // fields
    private boolean mOccupied = false;
    private PlayerColor mActivePlayerColor;


    // containers
    private final BlockingQueue<Request> mRequests = new LinkedBlockingQueue<>();
    private final Object mLock = new Object();
    private final Map<PlayerColor, View> mPlayerViews;
    private final Game mGame;

    // trivial constructors
    public ShootInteraction(Game game, Map<PlayerColor, View> playerViews) {
        mGame = game;
        mPlayerViews = playerViews;
    }

    // trivial getters
    public boolean isOccupied() {
        return mOccupied;
    }
    public BlockingQueue<Request> getRequestQueue() {
        return mRequests;
    }
    public Object getLock() {
        return mLock;
    }
    public PlayerColor getActivePlayerColor() {
        return mActivePlayerColor;
    }

    // trivial setters
    public void putRequest(Request request) {
        if (!isOccupied())
            throw new UnsupportedOperationException("No one is shooting!");

        mLogger.log(Level.INFO, "Putting request in shoot interaction queue: {0}", request);
        mRequests.add(request);
    }

    /**
     * Executes shoot interaction if not already occupied
     * @param game the game on which the shoot interaction will take place
     * @param shooter the shooter
     * @param weaponBehaviour the weapon behaviour used to shoot
     */
    public void exec(Game game, PlayerColor shooter, Expression weaponBehaviour, ShootUndoInfo undoInfo) {
        // announce that thread is occupied
        mOccupied = true;
        mActivePlayerColor = mGame.getActivePlayer().getColor();

        mLogger.info("Starting shoot interaction thread...");
        new Thread(() -> {
            // evaluate shoot expression
            ShootContext context = new ShootContext(game, mPlayerViews.get(shooter), shooter, undoInfo, this);
            try {
                weaponBehaviour.eval(context);
            } catch (UndoShootInteractionException e) {
                mLogger.log(Level.INFO,
                        "Undoing shoot interaction...");
                context.getUndoInfo().doUndo();
            } catch (Exception e) {
                mLogger.log(Level.WARNING,
                        "Shutting down shoot interaction because of exception in expression evaluation...");
                throw e;
            } finally {
                synchronized (mLock) {
                    mLock.notifyAll();
                }

                context.getView().confirmEndOfInteraction();
            }

            // announce end of shoot interaction
            synchronized (mLock) {
                mLogger.info("Shutting down shoot interaction thread...");

                if (!mRequests.isEmpty())
                    mLogger.log(Level.SEVERE,
                            "Shoot interaction thread is shutting down, but there are still " +
                                    "requests to process in the request queue!\n" +
                                    "Unprocessed requests: {0}", mRequests
                    );
                mRequests.clear();

                mOccupied = false;
                mLock.notifyAll();
            }
        }).start();
    }

    // inflict damage
    public void inflictDamage(Game game, PlayerColor inflicter, Set<PlayerColor> inflicted, Damage amount) {
        mLogger.log(Level.INFO,
                "{0} inflicting {1} damage to {2}",
                new Object[]{inflicter, amount, inflicted}
        );

        // handle targeting scope activation
        handleTargetingScope(inflicter, inflicted, amount);

        inflicted.stream()
                // actually do damage to inflicted player
                .peek(singularInflicted ->
                        game.handleDamageInteraction(inflicter, singularInflicted, amount))

                // handle tagback activation
                .forEach(singularInflicted ->
                    handleTagbackGrenade(inflicter, singularInflicted)
                );
    }

    // move player around
    public static void move(Game game, Set<PlayerColor> who, Position where) {
        who.forEach(pl -> game.getPlayerFromColor(pl).move(where));
    }

    // wait for a particular request
    private Request waitForRequest(String requestName, Runnable viewActivator) {
        mLogger.log(Level.INFO, "Shoot interaction waiting for {0} request", requestName);

        // show the view corresponding with the requested request
        //  NB. the "Request" here is treated more as a response from the view
        viewActivator.run();

        // wait for request to be presented on the request queue by another thread
        Request request;
        try {
            request = getRequestQueue().poll(REQUEST_ACCEPTANCE_TIMEOUT, TimeUnit.SECONDS);

            // interrupt if timeout is too long
            if (request == null) {
                mLogger.log(Level.SEVERE, "Request acceptance timeout exceeded while waiting for " +
                        "request in shoot interaction");
                throw new InterruptedException();
            }
        } catch (InterruptedException e) {
            // handle interruption
            mLogger.log(Level.WARNING,
                    "Shoot interaction interrupted while waiting for {0} request!",
                    requestName
            );

            Thread.currentThread().interrupt();
            throw new EvaluationInterruptedException("request");
        }

        // undo shoot interaction if requested
        // TODO: use alternative approach to instanceof
        if (request instanceof UndoWeaponInteractionRequest) {
            mLogger.log(
                    Level.INFO, "Received undo request while waiting for {0} request!",
                    request
            );
            throw new UndoShootInteractionException();
        }

        return request;
    }

    // wait for a particular selection request
    private <T> Stream<T> waitForSelectionRequestCheckingInput(View selectingView, Runnable viewActivator,
                                                               Collection<T> possibleToSelect,
                                                               Function<Request, Stream<T>> selectionGetter,
                                                               String selectionDescriptor) {
        Request request;
        List<T> selection;
        while (true) {
            request = waitForRequest(
                    selectionDescriptor + " selection (among " + possibleToSelect + ")",
                    viewActivator
            );

            selection = selectionGetter.apply(request)
                    .collect(Collectors.toList());

            // find invalid input that the user was not allowed to select
            Set<T> invalidSelected = selection.stream()
                    .filter(selected -> !possibleToSelect.contains(selected))
                    .collect(Collectors.toSet());

            // go on if all input is correct
            if (invalidSelected.isEmpty())
                break;

            // else report error and request input again
            reportError(selectingView, String.format(
                    "Illegal %s selection made: %s\n" +
                            "Possible selection set: %s",
                    selectionDescriptor, selection, possibleToSelect
            ));
        }

        mLogger.log(Level.INFO, "Shoot interaction received {0} selection: [{1}]",
                new Object[]{selectionDescriptor, selection});

        return selectionGetter.apply(request);
    }

    // wait for a particular selection request
    private <T> Stream<T> waitForSelectionRequestCheckingInputBounds(View selectingView, Runnable viewActivator,
                                                                     Collection<T> possibleToSelect,
                                                                     int min, int max, Function<Request, Stream<T>> selectionGetter,
                                                                     String selectionDescriptor) {
        // undo if selection cannot be performed...
        if (possibleToSelect.size() < min) {
            reportError(selectingView, Controller.NO_ACTIONS_REMAINING_ERROR_MSG);
            throw new UndoShootInteractionException();
        }

        // attempt to skip selection request
        if (min == max && possibleToSelect.size() == min) {
            selectingView.showMessage(String.format(
                    "Skipping %s selection of %d",
                    selectionDescriptor, min
            ));
            return possibleToSelect.stream();
        }

        while (true) {
            List<T> selection = waitForSelectionRequestCheckingInput(
                    selectingView, viewActivator,
                    possibleToSelect,
                    selectionGetter, selectionDescriptor
            )
                    .collect(Collectors.toList());

            if (selection.size() <= max)
                return selection.stream();

            else
                reportError(selectingView, String.format(
                        "Over-sized %s selection received: %d (max: %d)",
                        selectionDescriptor, selection.size(), max
                ));
        }
    }


    // wait for a particular selection request
    private <T> Stream<T> waitForSelectionRequestSkippingObvious(View selectingView, Runnable viewActivator,
                                                                 Collection<T> possibleToSelect,
                                                                 int min, int max, Function<Request, Stream<T>> selectionGetter,
                                                                 String selectionDescriptor) {
        // attempt to skip selection request
        if (min == max && possibleToSelect.size() == min) {
            selectingView.showMessage(String.format(
                    "Skipping %s selection of %d",
                    selectionDescriptor, min
            ));
            return possibleToSelect.stream();
        }

        return waitForSelectionRequestCheckingInputBounds(
                selectingView, viewActivator,
                possibleToSelect, min, max,
                selectionGetter, selectionDescriptor
        );
    }

    // manage payment
    public void manageAmmoPayment(PlayerColor payingPlayerColor, AmmoValue costToPay, String thingsToPay,
                                  Runnable selectThingsToPayAgain) {
        Player payingPlayer = mGame.getPlayerFromColor(payingPlayerColor);
        View payerView = mPlayerViews.get(payingPlayerColor);

        LOGGER.log(Level.INFO,
                "proceeding with payment of {0}\n" +
                        "Total cost to pay: {1}\n" +
                        "Remaining ammo: {2}",
                new Object[]{
                        thingsToPay,
                        costToPay,
                        payingPlayer.getAmmo()
                }
        );

        // if the effects cannot be payed, the selection is invalid. Ask it again
        if (!AmmoPayment.canPayWithPowerUps(payingPlayer, costToPay)) {
            reportError(payerView, "The effects you chose cost too much to activate!" +
                    " Select less effects or undo the shoot interaction"
            );

            selectThingsToPayAgain.run(); return;
        }

        // if the user has enough ammo, make him pay without asking for powerup selection
        else if (AmmoPayment.isValid(payingPlayer, costToPay, ArrayUtils.ofAll(false, 4))) {
            payerView.showMessage("skipping powerup discard...");

            AmmoPayment.payCost(payingPlayer, costToPay, ArrayUtils.ofAll(false, 4));
        }

        // otherwise a powerup discard is requested
        else {
            LOGGER.log(Level.INFO, "starting poewrup discard interaction loop...");

            while (true) {
                boolean[] selectedPowerupsMask =
                        selectPowerupsForPayment(payerView);
                Set<Integer> selectedPowerupsIndices = IntStream.range(0, 4)
                        .filter(i -> selectedPowerupsMask[i])
                        .boxed()
                        .collect(Collectors.toSet());
                Set<PowerUpCard> selectedPowerups = selectedPowerupsIndices.stream()
                        .map(payingPlayer::getPowerUpCard)
                        .collect(Collectors.toSet());

                // selection not valid
                if (!AmmoPayment.isValid(
                        payingPlayer,
                        costToPay,
                        selectedPowerupsMask
                )) {
                    reportError(payerView, String.format(
                            "You cannot pay the selected effects (%s) with these powerups: %s",
                            thingsToPay, selectedPowerups
                    ));
                }

                // selection valid
                else {
                    LOGGER.log(Level.INFO,
                            "Player paying discarding powerups: {0}",
                            selectedPowerups
                    );

                    AmmoPayment.payCost(payingPlayer, costToPay, selectedPowerupsMask);

                    return;
                }
            }
        }
    }

    // select targets
    public Set<PlayerColor> selectTargets(View view, int min, int max, Set<PlayerColor> possibleTargets) {
        return waitForSelectionRequestSkippingObvious(
                view,
                () -> view.showTargetsSelectionView(min, max, possibleTargets),
                possibleTargets, min, max,
                req -> ((TargetsSelectedRequest) req).getSelectedTargets().stream(),
                "target"
        )
                .collect(Collectors.toSet());
    }

    // select position
    public Position selectPosition(View view, Set<Position> range) {
        view.showPositionSelectionView(range);

        return  waitForSelectionRequestSkippingObvious(
                view,
                () -> view.showPositionSelectionView(range),
                range, 1, 1,
                req -> Stream.of(((PositionSelectedRequest) req).getPosition()),
                "position"
        )
                .collect(Collectors.toList())
                .get(0);
    }

    // select effects
    public List<String> selectEffects(View view, SortedMap<Integer, Set<Effect>> priorityMap,
                                      Set<Effect> possibleEffects) {
        Set<String> possibleEffectIDs = possibleEffects.stream()
                .map(Effect::getId)
                .collect(Collectors.toSet());

        return waitForSelectionRequestCheckingInput(
                view,
                () -> view.showEffectsSelectionView(priorityMap, possibleEffects),
                possibleEffectIDs,
                req -> ((EffectsSelectedRequest) req).getSelectedEffects().stream(),
                "effect"
        )
                .collect(Collectors.toList());
    }

    // select modes
    public String selectWeaponMode(View view, Effect mode1, Effect mode2) {
        return waitForSelectionRequestSkippingObvious(
                view,
                () -> view.showWeaponModeSelectionView(mode1, mode2),
                Stream.of(mode1, mode2).map(Effect::getId).collect(Collectors.toSet()), 1, 1,
                req -> Stream.of(((WeaponModeSelectedRequest) req).getId()),
                "mode"
        )
                .collect(Collectors.toList())
                .get(0);
    }

    // select powerups for ammo payment
    public boolean[] selectPowerupsForPayment(View view) {
        PowerUpDiscardedRequest request =
                (PowerUpDiscardedRequest) waitForRequest(
                        "powerup discard request",
                        view::showPowerUpsDiscardView
                );

        return request.getDiscarded();
    }

    // pick direction
    public Direction pickDirection(View view) {

        DirectionSelectedRequest request =
                (DirectionSelectedRequest) waitForRequest(
                        "direction selection",
                        view::showDirectionSelectionView
                );

        return request.getDirection();
    }

    // pick room color
    public TileColor pickRoomColor(View view, Set<TileColor> possibleColors) {
        return waitForSelectionRequestSkippingObvious(
                view,
                () -> view.showRoomColorSelectionView(possibleColors),
                possibleColors, 1, 1,
                req -> Stream.of(((RoomSelectedRequest) req).getColor()),
                "room color"
        )
                .collect(Collectors.toList())
                .get(0);
    }

    // pick ammo color
    private TileColor pickAmmoColor(View view, Set<TileColor> possibleColors) {
        return waitForSelectionRequestSkippingObvious(
                view,
                () -> view.showAmmoColorSelectionView(possibleColors),
                possibleColors, 1, 1,
                req -> Stream.of(((AmmoColorSelectedRequest) req).getAmmoColor()),
                "ammo color"
        )
                .collect(Collectors.toList())
                .get(0);
    }

    // report error
    private void reportError(View view, String message) {
        view.showMessage(message);
    }

    /************************/
    /* Powerup interactions */
    /************************/

    // request a powerup activation
    private boolean requestPowerupActivation(PlayerColor activator, String powerupName, int index) {
        View view = mPlayerViews.get(activator);

        while (true) {
            Set<Integer> selectedIndices = waitForSelectionRequestCheckingInputBounds(
                    mPlayerViews.get(activator),
                    () -> view.showPowerUpSelectionView(Collections.singletonList(index)),
                    Collections.singleton(index),
                    0, 1,
                    req -> ((PowerUpsSelectedRequest) req).getIndexes().stream(),
                    powerupName
            )
                    .collect(Collectors.toSet());

            if (selectedIndices.isEmpty())
                return false;

            int selectedIndex = selectedIndices.iterator().next();

            if (selectedIndex != index) {
                reportError(view, "You can't use a " +
                        mGame.getPlayerFromColor(activator).getPowerUpCard(selectedIndex).getType() +
                        " you can only use a " + powerupName
                );
                continue;
            }

            return true;
        }
    }

    // handle targeting scope activation
    private void handleTargetingScope(PlayerColor inflicter, Set<PlayerColor> inflicted, Damage damageInflicted) {
        Player inflicterPlayer = mGame.getPlayerFromColor(inflicter);
        View inflicterView = mPlayerViews.get(inflicter);

        if (damageInflicted.getDamage() > 0) {
            Set<Integer> possibleIndices = inflicterPlayer.getPowerUpIndices(PowerUpType.TARGETING_SCOPE);

            possibleIndices.forEach(index -> {
                // check if the scope can be payed for
                boolean[] scopeExcludedMask = ArrayUtils.from(IntStream.range(0, MAX_POWERUPS_IN_HAND)
                        .mapToObj(i -> i != index && inflicterPlayer.getPowerUpCard(i) != null)
                        .collect(Collectors.toList())
                );

                boolean canPay = Stream.of(TileColor.RED, TileColor.YELLOW, TileColor.BLUE)
                        .map(AmmoValue::from)
                        .anyMatch(ammo ->
                                AmmoPayment.isValid(
                                        inflicterPlayer,
                                        ammo,
                                        ArrayUtils.ofAll(false, 3)
                                ) ||
                                        AmmoPayment.isValid(
                                                inflicterPlayer,
                                                ammo,
                                                scopeExcludedMask
                                        )
                        );

                if (canPay) {
                    // the activation of each scope is requested individually from the user
                    inflicterView.showMessage(
                            "You can use your targeting scope on one of the selected targets! Select it in the " +
                                    "powerup menu to use it."
                    );

                    if (requestPowerupActivation(
                            inflicter, PowerUpType.TARGETING_SCOPE.toString(), index)
                    ) {
                        useTargetingScope(inflicter, inflicted, index);
                    }
                }
            });
        }
    }

    // handle tagback grenade activation
    private void handleTagbackGrenade(PlayerColor inflicter, PlayerColor inflicted) {
        Player inflictedPlayer = mGame.getPlayerFromColor(inflicted);
        Player inflicterPlayer = mGame.getPlayerFromColor(inflicter);
        View inflictedView = mPlayerViews.get(inflicted);
        View inflicterView = mPlayerViews.get(inflicter);
        Board board = mGame.getBoard();

        if (board.canSee(inflictedPlayer.getPos(), inflicterPlayer.getPos())) {
            Set<Integer> possibleIndices = inflictedPlayer.getPowerUpIndices(PowerUpType.TAGBACK_GRENADE);

            possibleIndices.forEach(index -> {
                inflicterView.showMessage(
                        inflictedPlayer.getName() + " is thinking..."
                );

                // pass control to tagback activator
                mActivePlayerColor = inflicted;

                inflictedView.showMessage(
                        "You can use your tagback grenade on " + inflicterPlayer.getName() + "!" +
                                "Select it from the powerup menu to use it."
                );

                if (requestPowerupActivation(
                        inflicted, PowerUpType.TAGBACK_GRENADE.toString(), index
                )) {
                    useTagbackGrenade(inflicted, inflicter, index);
                }

                // give control back to shooter
                mActivePlayerColor = inflicter;
            });
        }
    }

    // activate tagback grenade
    private void useTagbackGrenade(PlayerColor inflicter, PlayerColor inflicted, int index) {
        mGame.getPlayerFromColor(inflicter).discard(index);

        mGame.handleDamageInteraction(inflicter, inflicted, new Damage(0, 1));
    }

    // use targeting scope
    private void useTargetingScope(PlayerColor inflicterColor, Set<PlayerColor> possibleTargets, int index) {
        Player inflicter = mGame.getPlayerFromColor(inflicterColor);
        View inflicterView = mPlayerViews.get(inflicterColor);

        inflicter.discard(index);

        // TODO: pick ammo color
        manageAmmoPayment(
                inflicterColor,
                AmmoValue.from(pickAmmoColor(
                        inflicterView,
                        inflicter.getAmmo().getContainedColors()
                )),
                "targeting scope",
                () -> mLogger.log(Level.SEVERE,
                        "{0} not able to pay for targeting scope even though previous checks" +
                                "attested it was possible!")
        );

        PlayerColor selectedTarget = selectTargets(inflicterView, 1, 1, possibleTargets)
                .iterator().next();
        mGame.handleDamageInteraction(inflicterColor, selectedTarget, new Damage(1, 0));
    }

}

