package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.controller.weapon.expression.*;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.board.TileColor;
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
import java.util.stream.Stream;

public class ShootInteraction {
    // logger
    private Logger mLogger = Logger.getLogger(getClass().getName());

    // constants
    private static final int REQUEST_ACCEPTANCE_TIMEOUT = 1; // amount of time spent waiting for a request in seconds

    // fields
    private boolean mOccupied = false;
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
     * @param view the view used to communicate with the user
     * @param shooter the shooter
     * @param weaponBehaviour the weapon behaviour used to shoot
     */
    public void exec(Game game, View view, PlayerColor shooter, Expression weaponBehaviour) {
        // announce that thread is occupied
        mOccupied = true;

        mLogger.info("Starting shoot interaction thread...");
        new Thread(() -> {
            // evaluate shoot expression
            ShootContext context = new ShootContext(game, view, shooter, this);
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
        Board board = game.getBoard();
        Player inflicterPlayer = game.getPlayerFromColor(inflicter);
        View inflicterView = mPlayerViews.get(inflicter);

        mLogger.log(Level.INFO,
                "{0} inflicting {1} damage to {2}",
                new Object[]{inflicter, amount, inflicted}
        );

        // handle targeting scope activation
        if (amount.getDamage() > 0) {
            Set<Integer> possibleIndices = inflicterPlayer.getPowerUpIndices(PowerUpType.TARGETING_SCOPE);

            possibleIndices.forEach(index -> {
                // the activation of each scope is requested individually from the user
                inflicterView.showMessage(
                        "You can use your targeting scope on one of the selected targets! Select it in the powerup " +
                                "menu to use it."
                );

                if (requestPowerupActivation(
                        inflicter, PowerUpType.TARGETING_SCOPE.toString(), index)
                ) {
                    useTargetingScope(inflicter, inflicted, index);
                }
            });
        }

        inflicted.stream()
                // actually do damage to inflicted player
                .peek(singularInflicted ->
                        game.handleDamageInteraction(inflicter, singularInflicted, amount))

                // handle tagback activation
                .forEach(singularInflicted -> {
                    Player inflictedPlayer = game.getPlayerFromColor(singularInflicted);
                    View inflictedView = mPlayerViews.get(singularInflicted);

                    if (board.canSee(inflictedPlayer.getPos(), inflicterPlayer.getPos())) {
                        Set<Integer> possibleIndices = inflictedPlayer.getPowerUpIndices(PowerUpType.TAGBACK_GRENADE);

                        possibleIndices.forEach(index -> {
                            inflictedView.showMessage(
                                    "You can use your tagback grenade on " + inflicterPlayer.getName() + "!" +
                                            "Select it from the powerup menu to use it."
                            );

                            if (requestPowerupActivation(
                                    singularInflicted,PowerUpType.TAGBACK_GRENADE.toString(), index)
                            ) {
                                useTagbackGrenade(singularInflicted, inflicter, index);
                            }
                        });
                    }
                });
    }

    // move player around
    public static void move(Game game, Set<PlayerColor> who, Position where) {
        who.forEach(pl -> game.getPlayerFromColor(pl).move(where));
    }

    // wait for a particular request
    private Request waitForRequest(String requestName) {
        mLogger.log(Level.INFO, "Shoot interaction waiting for {0} request", requestName);

        // wait for request to be presented on the request queue by another thread
        Request request;
        try {
            request = getRequestQueue().poll(REQUEST_ACCEPTANCE_TIMEOUT, TimeUnit.SECONDS);

            // interrupt if timeout is too long
            if (request == null)
                throw new InterruptedException();
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
    private <T> Request waitForSelectionRequest(View selectingView, Collection<T> possibleToSelect,
                                                Function<Request, Stream<T>> selectionGetter, String selectionDescriptor) {
        Request request;
        List<T> selection;
        while (true) {
            request = waitForRequest(selectionDescriptor + " selection");

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
            selectingView.reportError(String.format(
                    "Illegal %s selection made: %s\n" +
                            "Possible selection set: %s",
                    selectionDescriptor, selection, possibleToSelect
            ));
        }

        mLogger.log(Level.INFO, "Shoot interaction received {0} selection: [{1}]",
                new Object[]{selectionDescriptor, selection});

        return request;
    }

    // select targets
    public Set<PlayerColor> selectTargets(View view, int min, int max, Set<PlayerColor> possibleTargets) {
        // undo if selection cannot be performed...
        if (possibleTargets.size() < min) {
            view.reportError(Controller.NO_ACTIONS_REMAINING_ERROR_MSG);
            throw new UndoShootInteractionException();
        }

        // attempt to skip selection request
        if (possibleTargets.size() == min) {
            mLogger.log(Level.INFO, "Skipping selection of {0} targets", min);
            return possibleTargets;
        }

        view.showTargetsSelectionView(min, max, possibleTargets);
        TargetsSelectedRequest request = (TargetsSelectedRequest) waitForSelectionRequest(
                view, possibleTargets,
                req -> ((TargetsSelectedRequest) req).getSelectedTargets().stream(),
                "target"
        );

        return request.getSelectedTargets();
    }

    // select position
    public Position selectPosition(View view, Set<Position> range) {
        view.showPositionSelectionView(range);

        PositionSelectedRequest request = (PositionSelectedRequest) waitForSelectionRequest(
                view, range,
                req -> Stream.of(((PositionSelectedRequest) req).getPosition()),
                "position"
        );

        return request.getPosition();
    }

    // select effects
    public List<String> selectEffects(View view,
                                      SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {
        // select effects through view
        view.showEffectsSelectionView(priorityMap, possibleEffects);

        Set<String> possibleEffectIDs = possibleEffects.stream()
                .map(Effect::getId)
                .collect(Collectors.toSet());
        EffectsSelectedRequest request = (EffectsSelectedRequest) this.<String>waitForSelectionRequest(
                view,
                possibleEffectIDs,
                req -> ((EffectsSelectedRequest) req).getSelectedEffects().stream(),
                "effect"
        );

        return request.getSelectedEffects();
    }

    // select modes
    public String selectWeaponMode(View view, Effect mode1, Effect mode2) {
        // select effects through view
        view.showWeaponModeSelectionView(mode1, mode2);

        WeaponModeSelectedRequest request = (WeaponModeSelectedRequest) waitForSelectionRequest(
                view, Stream.of(mode1, mode2).map(Effect::getId).collect(Collectors.toSet()),
                req -> Stream.of(((WeaponModeSelectedRequest) req).getId()),
                "mode"
        );

        return request.getId();
    }

    // pick direction
    public Direction pickDirection(View view) {
        view.pickDirection();

        DirectionSelectedRequest request =
                (DirectionSelectedRequest) waitForRequest("direction selection");

        return request.getDirection();
    }

    // pick room color
    public TileColor pickRoomColor(View view, Set<TileColor> possibleColors) {
        view.pickDirection();

        RoomSelectedRequest request =
                (RoomSelectedRequest) waitForSelectionRequest(
                        view,
                        possibleColors,
                        req -> Stream.of(((RoomSelectedRequest) req).getColor()),
                        "room color"
                );

        return request.getColor();
    }


    /************************/
    /* Powerup interactions */
    /************************/

    // request a powerup activation
    private boolean requestPowerupActivation(PlayerColor activator, String powerupName, int index) {
        View view = mPlayerViews.get(activator);
        view.showPowerUpSelectionView(Collections.singletonList(index));

        while (true) {
            Request request = waitForRequest(powerupName + " activation");

            Optional<Integer> maybeSelectedIndex = ((PowerUpSelectedRequest) request).getIndex();

            if (!maybeSelectedIndex.isPresent())
                return false;

            int selectedIndex = maybeSelectedIndex.get();

            if (selectedIndex != index) {
                view.reportError("You can't use a " +
                        mGame.getPlayerFromColor(activator).getPowerUpCard(selectedIndex).getType() +
                        " you can only use a " + powerupName
                );
                continue;
            }


            return true;
        }
    }

    // activate tagback grenade
    private void useTagbackGrenade(PlayerColor inflicter, PlayerColor inflicted, int index) {
        mGame.getPlayerFromColor(inflicted).discard(index);

        mGame.handleDamageInteraction(inflicter, inflicted, new Damage(0, 1));
    }
    private void useTargetingScope(PlayerColor inflicter, Set<PlayerColor> possibleTargets, int index) {
        mGame.getPlayerFromColor(inflicter).discard(index);

        PlayerColor selectedTarget = selectTargets(mPlayerViews.get(inflicter), 1, 1, possibleTargets)
                .iterator().next();
        mGame.handleDamageInteraction(inflicter, selectedTarget, new Damage(0, 1));
    }
}

