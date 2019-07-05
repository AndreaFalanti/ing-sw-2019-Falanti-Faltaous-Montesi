package it.polimi.se2019.controller;


import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.controller.weapon.expression.UndoInfo;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.action.CostlyAction;
import it.polimi.se2019.model.action.MoveGrabAction;
import it.polimi.se2019.model.action.NewtonAction;
import it.polimi.se2019.model.action.TeleportAction;
import it.polimi.se2019.model.board.SpawnTile;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.util.Observer;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Observer<Request>, RequestHandler {
    // messages constants
    public static final String NO_ACTIONS_REMAINING_ERROR_MSG = "Can't proceed further with shoot! Undoing action...";
    private static final boolean DONT_USE_TIMER = false;

    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    // fields
    private final Game mGame;
    private final PlayerActionController mPlayerActionController;
    private final Map<PlayerColor, View> mPlayerViews;
    private final ShootInteraction mShootInteraction;

    private WeaponIndexStrategy mWeaponIndexStrategy;

    private int mPlayerNotSpawnedCounter;
    private boolean mActivePlayerSpawnedThisTurn = false;
    private PlayerColor mExpectedPlayingPlayer;
    private Timer mTurnTimer = new Timer();

    // constructors
    public Controller(Game game, Map<PlayerColor, View> playerViews) {
        mGame = game;
        mPlayerViews = playerViews;
        mPlayerActionController = new PlayerActionController(this);
        mShootInteraction = new ShootInteraction(mGame, mPlayerViews);

        mPlayerNotSpawnedCounter = playerViews.size();

        // observe view (Request)
        playerViews.values().stream()
                // observe views (Request)
                .peek(view -> view.registerAll(this))
                // make views observe game (Update)
                .forEach(mGame::registerAll);
    }

    // getters
    public Game getGame() {
        return mGame;
    }

    public PlayerActionController getPlayerActionController() {
        return mPlayerActionController;
    }

    public Map<PlayerColor, View> getPlayerViews() {
        return mPlayerViews;
    }

    public ShootInteraction getShootInteraction() {
        return mShootInteraction;
    }

    public Timer getTurnTimer() {
        return mTurnTimer;
    }

    public Object getShootInteractionLock() {
        if (mShootInteraction.isOccupied())
            return mShootInteraction.getLock();
        else
            throw new UnsupportedOperationException("Trying to get lock of a non occupied shoot interaction!");
    }

    public boolean isHandlingShootInteraction() {
        return mShootInteraction.isOccupied();
    }

    public int getPlayerNotSpawnedCounter() {
        return mPlayerNotSpawnedCounter;
    }

    public boolean isActivePlayerSpawnedThisTurn() {
        return mActivePlayerSpawnedThisTurn;
    }

    // setters
    public void setWeaponIndexStrategy(WeaponIndexStrategy weaponIndexStrategy) {
        mWeaponIndexStrategy = weaponIndexStrategy;
    }

    public void setPlayerNotSpawnedCounter(int playerNotSpawnedCounter) {
        mPlayerNotSpawnedCounter = playerNotSpawnedCounter;
    }

    public void setExpectedPlayingPlayer(PlayerColor expectedPlayingPlayer) {
        mExpectedPlayingPlayer = expectedPlayingPlayer;
    }

    /*******************/
    /* control methods */
    /*******************/
    public void startShootInteraction(PlayerColor shooter, Expression weaponBehaviour) {
        mShootInteraction.exec(mGame, shooter, weaponBehaviour, new UndoInfo(shooter, mGame));
    }

    public void startShootInteraction(PlayerColor shooter, Expression weaponBehaviour, UndoInfo undoInfo) {
        mShootInteraction.exec(mGame, shooter, weaponBehaviour, undoInfo);
    }

    private void continueShootInteraction(Request request) {
        // shoot info is useless without the shooting
        if (!isHandlingShootInteraction()) {
            mPlayerViews.get(request.getViewColor()).reportError("You can't provide shoot info with no shooting going on...");
            return;
        }

        // feed shooting information to current shoot interaction
        mShootInteraction.putRequest(request);
    }


    /******************************/
    /* handle requests from view  */
    /******************************/

    @Override
    public void handle(ValidPositionRequest request) {
        // deliver moves based on action
    }

    @Override
    public void handle(TargetsSelectedRequest request) {
        if (isHandlingShootInteraction()) {
            continueShootInteraction(request);
        }
        else {
            // only one target is needed, not hacked view should send only one anyway
            PlayerColor targetColor = (PlayerColor)request.getSelectedTargets().toArray()[0];
            mPlayerActionController.getCompletableNewtonAction().setTarget(targetColor);
            mPlayerViews.get(request.getViewColor()).showMessage("Select position in which move target");
            mPlayerViews.get(request.getViewColor()).showPositionSelectionView(
                    mPlayerActionController.getPositionsForNewton(targetColor));
        }
    }

    @Override
    public void handle(ActionRequest actionRequest) {
        mPlayerActionController.executeAction(actionRequest.getAction(), mPlayerViews.get(actionRequest.getViewColor()));
    }

    @Override
    public void handle(PowerUpDiscardedRequest request) {
        logger.log(Level.INFO, "Handling power up discard selection. PowerUps discarded: {0}",
                Arrays.toString(request.getDiscarded()));

        CostlyAction costlyAction = mPlayerActionController.getCompletableCostlyAction();
        if (costlyAction != null) {
            costlyAction.setDiscardedCards(request.getDiscarded());

            mPlayerActionController.executeAction(mPlayerActionController.getCachedAction(),
                    mPlayerViews.get(request.getViewColor()));
        }
    }

    @Override
    public void handle(WeaponSelectedRequest request) {
        logger.log(Level.INFO, "Handling weapon selection. Index selected: {0}", request.getWeaponIndex());

        mPlayerActionController.setCompletableGrabAction(mWeaponIndexStrategy.completeAction(request.getWeaponIndex(),
                mPlayerActionController.getCompletableGrabAction()));
        if (mPlayerActionController.getCachedAction().isComposite()) {
            ((MoveGrabAction) mPlayerActionController.getCachedAction()).setGrabAction(
                    mPlayerActionController.getCompletableGrabAction()
            );
        }
        else {
            mPlayerActionController.setCachedAction(mPlayerActionController.getCompletableGrabAction());
        }

        mWeaponIndexStrategy = null;
        mPlayerActionController.executeAction(mPlayerActionController.getCachedAction(),
                mPlayerViews.get(request.getViewColor()));
    }

    @Override
    public void handle(DirectionSelectedRequest request) {
        continueShootInteraction(request);
    }

    @Override
    public void handle(PositionSelectedRequest request) {
        if (isHandlingShootInteraction()) {
            continueShootInteraction(request);
        }
        else {
            TeleportAction teleportAction = mPlayerActionController.getCompletableTeleportAction();

            if (teleportAction != null) {
                teleportAction.setDestination(request.getPosition());
                mPlayerActionController.executeAction(teleportAction, mPlayerViews.get(request.getViewColor()));
            }
            else {
                NewtonAction newtonAction = mPlayerActionController.getCompletableNewtonAction();
                newtonAction.setDestination(request.getPosition());
                mPlayerActionController.executeAction(newtonAction, mPlayerViews.get(request.getViewColor()));
            }
        }
    }

    @Override
    public void handle(EffectsSelectedRequest request) {
        continueShootInteraction(request);
    }

    @Override
    public void handle(UndoWeaponInteractionRequest request) {
        continueShootInteraction(request);
    }

    @Override
    public void handle(WeaponModeSelectedRequest request) {
        continueShootInteraction(request);
    }

    @Override
    public void handle(PowerUpsSelectedRequest request) {
        continueShootInteraction(request);
    }

    @Override
    public void handle(RoomSelectedRequest request) {
        continueShootInteraction(request);
    }

    @Override
    public void handle(TurnEndRequest request) {
        mActivePlayerSpawnedThisTurn = false;
        mGame.onTurnEnd();

        if (areAllPlayersAlive()) {
            handleNextTurn();
        }
        else {
            sendRespawnNotificationToDeadPlayers();
        }
    }

    @Override
    public void handle(RespawnPowerUpRequest request) {
        if (!checkPowerUpValidity(request.getIndex())) {
            View playerView = mPlayerViews.get(request.getViewColor());
            playerView.reportError("Invalid power up index selected");
            playerView.showRespawnPowerUpDiscardView();
            return;
        }

        respawnPlayer(request.getIndex(), request.getViewColor());

        if (areAllPlayersAlive()) {
            handleNextTurn();
        }
        else {
            sendRespawnNotificationToDeadPlayers();
        }
    }

    private void respawnPlayer (int powerUpIndex, PlayerColor color) {
        Player respawningPlayer = mGame.getPlayerFromColor(color);
        TileColor spawnColor = respawningPlayer.getPowerUpCard(powerUpIndex).getColor();
        SpawnTile respawnTile = mGame.getBoard().getSpawnMap().get(spawnColor);
        Position respawnPosition = respawnTile.getPosition();

        respawningPlayer.respawn(respawnPosition);
        respawningPlayer.discard(powerUpIndex);
    }

    @Override
    public void handle(UsePowerUpRequest request) {
        if (!checkPowerUpValidity(request.getPowerUpIndex())) {
            mPlayerViews.get(request.getViewColor()).reportError("Invalid power up index selected");
            return;
        }

        PowerUpCard powerUpCard = mGame.getActivePlayer().getPowerUpCard(request.getPowerUpIndex());
        PowerUpType powerUpType = powerUpCard.getType();
        View playerView = mPlayerViews.get(request.getViewColor());

        switch (powerUpType) {
            case TELEPORT:
                mPlayerActionController.setCompletableTeleportAction(new TeleportAction(request.getPowerUpIndex()));
                playerView.showMessage("Select a position for teleport");
                playerView.showPositionSelectionView(mPlayerActionController.getPositionsForTeleport());
                break;
            case NEWTON:
                mPlayerActionController.setCompletableNewtonAction(new NewtonAction(request.getPowerUpIndex()));
                Set<PlayerColor> possibleTargets = mPlayerActionController.getAllTargetsExceptActivePlayer();
                if (possibleTargets.isEmpty()) {
                    playerView.reportError("No valid targets for newton!");
                }
                else {
                    playerView.showMessage("Select target for newton");
                    playerView.showTargetsSelectionView(1, 1, possibleTargets);
                }
                break;
            case TAGBACK_GRENADE:
                logger.info("Tagback grenade can't be handled without proper event");
                playerView.reportError("Can't use tagback without taking damage");
                break;
            case TARGETING_SCOPE:
                logger.info("Targeting scope can't be handled without proper event");
                playerView.reportError("Can't use targeting scope outside a shooting interaction");
                break;
            default:
                logger.severe("Unknown powerUp type");
                break;
        }
    }

    @Override
    public void handle(AmmoColorSelectedRequest request) {
        continueShootInteraction(request);
    }

    @Override
    public void handle(ReconnectionRequest request) {
        View reconnectedView = mPlayerViews.get(request.getViewColor());

        mPlayerViews.values().stream()
                .filter(view -> view != reconnectedView)
                .forEach(view -> view.showMessage(
                        String.format(
                                "%s has reconnected...",
                                mGame.getPlayerFromColor(request.getViewColor()).getName()
                        )
                ));

        reconnectedView.reinitialize(
                mGame.extractViewInitializationInfo(request.getViewColor())
        );
        reconnectedView.showMessage("You have been reconnected!");
    }

    @Override
    public void handle(DisconnectionRequest request) {
        System.out.println("HEWLLO!!!");

        mPlayerViews.entrySet().stream()
                .peek(entry -> System.out.println((entry.getKey())))
                .map(Map.Entry::getValue)
                .forEach(view -> view.showMessage(
                String.format(
                        "%s has disconnected...",
                        mGame.getPlayerFromColor(request.getViewColor()).getName()
                )
        ));
    }

    /**
     * Try to start next turn of the game, handling initial spawn if needed
     */
    public void handleNextTurn () {
        // prevent another turn start if called after an initial spawn
        if (mActivePlayerSpawnedThisTurn) {
            return;
        }

        if (mPlayerNotSpawnedCounter > 0) {
            startNextTurn();
            Player player = mGame.getActivePlayer();

            // handle initial spawn
            player.addPowerUp(mGame.getPowerUpDeck().drawCard(), true);
            player.addPowerUp(mGame.getPowerUpDeck().drawCard(), true);
            mPlayerViews.get(player.getColor()).showRespawnPowerUpDiscardView();

            mActivePlayerSpawnedThisTurn = true;
            mPlayerNotSpawnedCounter--;
        }
        else {
            startNextTurn();
        }
    }

    /**
     * Start next turn of the game
     */
    private void startNextTurn () {
        mGame.startNextTurn();
        mExpectedPlayingPlayer = mGame.getActivePlayer().getColor();

        setTimerTask();
    }

    /**
     * Suspend players for inactivity after a minute between an action and another
     */
    void setTimerTask () {
        mTurnTimer = new Timer();
        if (DONT_USE_TIMER) {
            return;
        }

        mTurnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mPlayerViews.get(mExpectedPlayingPlayer).showMessage("SUSPENDED FOR INACTIVITY");

                Player activePlayer = mGame.getActivePlayer();
                if (activePlayer.getPos() == null) {
                    boolean spawned = false;

                    for (int i = 0; i < activePlayer.getPowerUps().length && !spawned; i++) {
                        if (activePlayer.getPowerUpCard(i) != null) {
                            respawnPlayer(i, activePlayer.getColor());
                            spawned = true;
                        }
                    }
                }

                handle(new TurnEndRequest(mExpectedPlayingPlayer));
            }
        },60000);
    }

    /**
     * Check if all players are alive
     * @return true if all players are alive, false otherwise
     */
    private boolean areAllPlayersAlive () {
        for (Player player : mGame.getPlayers()) {
            if (player.isDead()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Send a respawn notification to first dead player found
     */
    private void sendRespawnNotificationToDeadPlayers () {
        for (Player player : mGame.getPlayers()) {
            if (player.isDead()) {
                player.addPowerUp(mGame.getPowerUpDeck().drawCard(), true);
                mPlayerViews.get(player.getColor()).showRespawnPowerUpDiscardView();

                mExpectedPlayingPlayer = player.getColor();
                return;
            }
        }
    }

    /**
     * Check if powerUp index is valid
     * @param index PowerUp index
     * @return true if valid, false otherwise
     */
    private boolean checkPowerUpValidity (int index) {
        if (index < 0 || index > 3) {
            return false;
        }
        PowerUpCard powerUpCard = mGame.getPlayerFromColor(mExpectedPlayingPlayer).getPowerUpCard(index);
        return powerUpCard != null;
    }

    /*****************************************/
    /* update method from observer interface */
    /*****************************************/

    @Override
    public void update(Request message) {
        PlayerColor activePlayer = isHandlingShootInteraction() ?
                getShootInteraction().getActivePlayerColor() :
                mExpectedPlayingPlayer;

        if (!activePlayer.equals(message.getViewColor()))
            mPlayerViews.get(message.getViewColor()).reportError("It's not your turn!");
        else
            message.handleMe(this);
    }
}
