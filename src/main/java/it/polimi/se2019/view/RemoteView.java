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

    void showWeaponSelectionView(TileColor spawnColor);

    void showValidPositions(List<Position> positions);

    void showPowerUpSelectionView(List<Integer> indexes);

    void showRoomColorSelectionView(Set<TileColor> possibleColors);

    void showDirectionSelectionView();

    void showPositionSelectionView(Set<Position> possiblePositions);

    void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets);

    void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap,
                                  Set<Effect> possibleEffects);

    void showWeaponModeSelectionView(Effect effect1, Effect effect2);

    void reinitialize(InitializationInfo initializationInfo);

    @Override
    void update(Update update);
}
