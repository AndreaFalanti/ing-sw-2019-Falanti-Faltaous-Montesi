package it.polimi.se2019.view;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.util.Observer;

import java.rmi.Remote;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

public interface RemoteView extends Observer<Update>, Remote {

     void showMessage(String message);

     void reportError(String error);

     void showPowerUpsDiscardView();

    /**
     * Activate weapon selection in spawn or player hand
     * @param spawnColor Spawn color in which choose the weapon, null if choosing in player hand
     */
     void showWeaponSelectionView(TileColor spawnColor);

     void showValidPositions(List<Position> positions);

     void showPowerUpSelectionView(List<Integer> indexes);

    // WEAPONS related

    /**
     * Ask a player to pick a room color
     * @param possibleColors
     */
     void showRoomColorSelectionView(Set<TileColor> possibleColors);

    /**
     * Ask player a cardinal direction
     */
     void showDirectionSelectionView();

    /**
     * Ask player to select a position from the board
     * @param possiblePositions possible selectable positions (any position selected from outside this range should be
     *                          considered an input error by the controller)
     */
    void showPositionSelectionView(Set<Position> possiblePositions);

    /**
     * Ask player to select a group of targets (represented through PlayerColor) from the board
     * @param minToSelect minimum number of targets requested
     * @param maxToSelect maximum number of targets allowed
     * @param possibleTargets possible selectable targets (any target selected from outside this group should be
     *                        considered an input error by the controller)
     */
    void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets);

    /**
     * Ask player to select a list of effects
     * @param priorityMap The possible effects that the player can choose from. The effects are associated to their
     *                    respective priorities. If an effect is picked that is not present among this map, the the
     *                    controller should consider the call an input error.
     * @param possibleEffects TODO complete this doc
     */
    void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap,
                                                  Set<Effect> possibleEffects);

    /**
     * Ask player to select which weapon mode to use
     * @param effect1 First effect
     * @param effect2 Second effect
     */

    void showWeaponModeSelectionView(Effect effect1, Effect effect2);

    void reinitialize(InitializationInfo initializationInfo);

    @Override
    void update(Update update);
}
