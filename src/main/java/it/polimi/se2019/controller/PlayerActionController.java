package it.polimi.se2019.controller;

import it.polimi.se2019.controller.weapon.expression.UndoInfo;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.*;
import it.polimi.se2019.model.action.response.DiscardRequiredActionResponse;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;
import it.polimi.se2019.model.action.response.SelectWeaponRequiredActionResponse;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.view.View;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Sub-controller that handle all action performs and relative errors
 *
 * @author Andrea Falanti
 */
public class PlayerActionController implements InvalidActionResponseHandler {
    private Controller mMainController;
    private View mRequestingView;

    private Action mCachedAction;
    private GrabAction mCompletableGrabAction;
    private CostlyAction mCompletableCostlyAction;
    private TeleportAction mCompletableTeleportAction;
    private NewtonAction mCompletableNewtonAction;


    public PlayerActionController(Controller mainController) {
        mMainController = mainController;
    }

    public Action getCachedAction() {
        return mCachedAction;
    }

    public GrabAction getCompletableGrabAction() {
        return mCompletableGrabAction;
    }

    public CostlyAction getCompletableCostlyAction() {
        return mCompletableCostlyAction;
    }

    public TeleportAction getCompletableTeleportAction() {
        return mCompletableTeleportAction;
    }

    public NewtonAction getCompletableNewtonAction() {
        return mCompletableNewtonAction;
    }

    public void setCachedAction(Action cachedAction) {
        mCachedAction = cachedAction;
    }

    public void setCompletableGrabAction(GrabAction completableGrabAction) {
        mCompletableGrabAction = completableGrabAction;
    }

    public void setCompletableTeleportAction(TeleportAction completableTeleportAction) {
        mCompletableTeleportAction = completableTeleportAction;
    }

    public void setCompletableNewtonAction(NewtonAction completableNewtonAction) {
        mCompletableNewtonAction = completableNewtonAction;
    }

    private void resetCache () {
        mCachedAction = null;
        mCompletableCostlyAction = null;
        mCompletableGrabAction = null;
        mCompletableTeleportAction = null;
        mCompletableNewtonAction = null;
    }

    public void executeAction (Action action, View requestingView){
        mRequestingView = requestingView;
        resetCache();

        Optional<InvalidActionResponse> response = action.getErrorResponse(mMainController.getGame());
        if(!response.isPresent()) {
            mMainController.getTurnTimer().cancel();
            mMainController.getTurnTimer().purge();

            if (action.leadToAShootInteraction()) {
                UndoInfo shootUndoInfo = new UndoInfo(
                        mMainController.getGame().getActivePlayer().getColor(),
                        mMainController.getGame()
                );

                action.perform(mMainController.getGame());
                mMainController.startShootInteraction(requestingView.getOwnerColor(),
                        ((ShootLeadingAction) action).getShotBehaviour(mMainController.getGame()),
                        shootUndoInfo);
            }
            else {
                action.perform(mMainController.getGame());
                requestingView.confirmEndOfInteraction();

                mMainController.setTimerTask();
            }

            if (action.consumeAction()) {
                mMainController.getGame().decreaseActionCounter();
            }
        }
        else {
            mCachedAction = action;
            response.get().handle(this);
        }
    }

    @Override
    public void handle(MessageActionResponse actionResponse) {
        if (actionResponse.isError()) {
            mRequestingView.reportError(actionResponse.getMessage());
        }
        else {
            mRequestingView.showMessage(actionResponse.getMessage());
        }
    }

    @Override
    public void handle(DiscardRequiredActionResponse actionResponse) {
        mCompletableCostlyAction = actionResponse.getCostlyAction();

        mRequestingView.showMessage(actionResponse.getMessage());
        mRequestingView.showPowerUpsDiscardView();
    }

    @Override
    public void handle(SelectWeaponRequiredActionResponse actionResponse) {
        mCompletableGrabAction = actionResponse.getGrabAction();
        mMainController.setWeaponIndexStrategy(actionResponse.getStrategy());

        mRequestingView.showMessage(actionResponse.getMessage());
        mRequestingView.showWeaponSelectionView(actionResponse.getColor());
    }

    public Set<Position> getPositionsForTeleport () {
        Set<Position> positions = new HashSet<>();
        Board board = mMainController.getGame().getBoard();

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Position position = new Position(x, y);
                if(board.getTileAt(position) != null) {
                    positions.add(position);
                }
            }
        }
        // remove active player position, can't teleport to his current position
        positions.remove(mMainController.getGame().getActivePlayer().getPos());

        return positions;
    }

    public Set<PlayerColor> getAllTargetsExceptActivePlayer () {
        Set<PlayerColor> playerColors = new HashSet<>();

        for(Player player : mMainController.getGame().getPlayers()) {
            if (player.getPos() != null) {
                playerColors.add(player.getColor());
            }
        }
        playerColors.remove(mMainController.getGame().getActivePlayer().getColor());

        return playerColors;
    }

    public Set<Position> getPositionsForNewton (PlayerColor targetColor) {
        Player target = mMainController.getGame().getPlayerFromColor(targetColor);
        return mMainController.getGame().getBoard().getReachablePositions(target.getPos(), 1, 2);
    }
}

