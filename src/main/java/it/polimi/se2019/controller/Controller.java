package it.polimi.se2019.controller;


import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.controller.weapon.expression.ShootUndoInfo;
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

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Observer<Request>, RequestHandler {
    // messages constants
    public static final String NO_ACTIONS_REMAINING_ERROR_MSG = "No actions remaining! Undo and try again...";

    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    // fields
    private final Game mGame;
    private final PlayerActionController mPlayerActionController;
    private final Map<PlayerColor, View> mPlayerViews;
    private final ShootInteraction mShootInteraction;

    private WeaponIndexStrategy mWeaponIndexStrategy;

    private int mPlayerNotSpawnedCounter;
    private boolean mActivePlayerSpawnedThisTurn = false;

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

    // trivial getters
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

    // Setters
    public void setWeaponIndexStrategy(WeaponIndexStrategy weaponIndexStrategy) {
        mWeaponIndexStrategy = weaponIndexStrategy;
    }

    public void setPlayerNotSpawnedCounter(int playerNotSpawnedCounter) {
        mPlayerNotSpawnedCounter = playerNotSpawnedCounter;
    }

    /*******************/
    /* control methods */
    /*******************/
    public void startShootInteraction(PlayerColor shooter, Expression weaponBehaviour) {
        mShootInteraction.exec(mGame, shooter, weaponBehaviour, new ShootUndoInfo(mGame));
    }

    public void startShootInteraction(PlayerColor shooter, Expression weaponBehaviour, ShootUndoInfo undoInfo) {
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
        Player respawningPlayer = mGame.getPlayerFromColor(request.getViewColor());
        TileColor spawnColor = respawningPlayer.getPowerUpCard(request.getIndex()).getColor();
        SpawnTile respawnTile = mGame.getBoard().getSpawnMap().get(spawnColor);
        Position respawnPosition = respawnTile.getPosition();

        respawningPlayer.respawn(respawnPosition);
        respawningPlayer.discard(request.getIndex());

        if (areAllPlayersAlive()) {
            handleNextTurn();
        }
    }

    @Override
    public void handle(UsePowerUpRequest request) {
        PowerUpCard powerUpCard = mGame.getActivePlayer().getPowerUpCard(request.getPowerUpIndex());
        if (powerUpCard == null) {
            mPlayerViews.get(request.getViewColor()).reportError("Invalid power up index selected");
            return;
        }

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

    public void handleNextTurn () {
        // prevent another turn start if called after an initial spawn
        if (mActivePlayerSpawnedThisTurn) {
            return;
        }

        if (mPlayerNotSpawnedCounter > 0) {
            mGame.startNextTurn();
            Player player = mGame.getActivePlayer();

            // handle initial spawn
            player.addPowerUp(mGame.getPowerUpDeck().drawCard(), true);
            player.addPowerUp(mGame.getPowerUpDeck().drawCard(), true);
            mPlayerViews.get(player.getColor()).showRespawnPowerUpDiscardView();

            mActivePlayerSpawnedThisTurn = true;
            mPlayerNotSpawnedCounter--;
        }
        else {
            mGame.startNextTurn();
        }
    }

    private boolean areAllPlayersAlive () {
        for (Player player : mGame.getPlayers()) {
            if (player.isDead()) {
                return false;
            }
        }

        return true;
    }

    private void sendRespawnNotificationToDeadPlayers () {
        for (Player player : mGame.getPlayers()) {
            if (player.isDead()) {
                player.addPowerUp(mGame.getPowerUpDeck().drawCard(), true);
                mPlayerViews.get(player.getColor()).showRespawnPowerUpDiscardView();
            }
        }
    }

    /*****************************************/
    /* update method from observer interface */
    /*****************************************/

    @Override
    public void update(Request message) {
        message.handleMe(this);
    }
}
