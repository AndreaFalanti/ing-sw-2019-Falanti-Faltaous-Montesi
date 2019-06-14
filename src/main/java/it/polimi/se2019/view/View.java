package it.polimi.se2019.view;

import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.UpdateHandler;
import it.polimi.se2019.util.Either;
import it.polimi.se2019.util.Observable;
import it.polimi.se2019.util.Observer;
import it.polimi.se2019.view.request.Request;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

public abstract class View extends Observable<Request> implements Observer<Either<Response, Update>> {
    // fields
    protected PlayerColor ownerColor;
    protected UpdateHandler mUpdateHandler;
    protected ResponseHandler mResponseHandler;

    // constructor
    public View(ResponseHandler responseHandler, UpdateHandler updateHandler) {
        mResponseHandler = responseHandler;
        mUpdateHandler = updateHandler;
    }

    // trivial getters
    public ResponseHandler getResponseHandler() {
        return mResponseHandler;
    }

    public UpdateHandler getUpdateHandler() {
        return mUpdateHandler;
    }

    public PlayerColor getOwnerColor() {
        return ownerColor;
    }


    // Actually handled in ResponseHandler, will be called directly from controller on view

    // ACTIONS related
    public abstract void showMessage(String message);

    public abstract void reportError(String error);

    public abstract void showPowerUpsDiscardView();

    /**
     * Activate weapon selection in spawn or player hand
     * @param spawnColor Spawn color in which choose the weapon, null if choosing in player hand
     */
    public abstract void showWeaponSelectionView(TileColor spawnColor);

    public abstract void showValidPositions(List<Position> positions);

    // WEAPONS related
    /**
     * Ask player a cardinal direction
     */
    public abstract void showDirectionSelectionView();

    /**
     * Ask player to select a position from the board
     * @param possiblePositions possible selectable positions (any position selected from outside this range should be
     *                          considered an input error by the controller)
     */
    public abstract void showPositionSelectionView(Set<Position> possiblePositions);

    /**
     * Ask player to select a group of targets (represented through PlayerColor) from the board
     * @param minToSelect minimum number of targets requested
     * @param maxToSelect maximum number of targets allowed
     * @param possibleTargets possible selectable targets (any target selected from outside this group should be
     *                        considered an input error by the controller)
     */
    public abstract void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets);

    /**
     * Ask player to select a list of effects
     * @param priorityMap The possible effects that the player can choose from. The effects are associated to their
     *                    respective priorities. If an effect is picked that is not present among this map, the the
     *                    controller should consider the call an input error.
     * @param currentPriority The required priority of the picked effect. If an effect is returned that does not have
     *                        this priority, then the controller should consider the call an input error.
     */
    public abstract void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap, int currentPriority);

    //region old responses
    /**
     * Ask player a cardinal direction
     * @return the selected direction
     */
    public abstract Direction pickDirection();

    /**
     * Ask player to select a position from the board
     * @param possiblePositions possible selectable positions (any position selected from outside this range should be
     *                          considered an input error by the controller)
     * @return the selected position
     */
    public abstract Position selectPosition(Set<Position> possiblePositions);

    /**
     * Ask player to select a group of targets (represented through PlayerColor) from the board
     * @param minToSelect minimum number of targets requested
     * @param maxToSelect maximum number of targets allowed
     * @param possibleTargets possible selectable targets (any target selected from outside this group should be
     *                        considered an input error by the controller)
     * @return the selected targets
     */
    public abstract Set<PlayerColor> selectTargets(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets);

    /**
     * Ask player to select a list of effects
     * @param priorityMap The possible effects that the player can choose from. The effects are associated to their
     *                    respective priorities. If an effect is picked that is not present among this map, the the
     *                    controller should consider the call an input error.
     * @param currentPriority The required priority of the picked effect. If an effect is returned that does not have
     *                        this priority, then the controller should consider the call an input error.
     * @return The id of the selected effect
     */
    public abstract Set<String> selectEffects(SortedMap<Integer, Set<Effect>> priorityMap, int currentPriority);
//endregion

    @Override
    public final void update(Either<Response, Update> message) {
        message.apply(
                response -> response.handleMe(mResponseHandler),
                update -> update.handleMe(mUpdateHandler)
        );
    }

}
