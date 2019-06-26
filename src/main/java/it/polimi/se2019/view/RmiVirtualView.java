package it.polimi.se2019.view;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.UpdateHandler;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

public class RmiVirtualView extends View {
    public RmiVirtualView() {
        super(
                new UpdateHandler() {
                    @Override
                    public void fallbackHandle(Update update) {
                        // TODO: serialize and send
                    }
                }
        );
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void reportError(String error) {

    }

    @Override
    public void showPowerUpsDiscardView() {

    }

    @Override
    public void showWeaponSelectionView(TileColor spawnColor) {

    }

    @Override
    public void showValidPositions(List<Position> positions) {

    }

    @Override
    public void showPowerUpSelectionView(List<Integer> indexes) {

    }

    @Override
    public void showRoomColorSelectionView() {

    }

    @Override
    public void showDirectionSelectionView() {

    }

    @Override
    public void showPositionSelectionView(Set<Position> possiblePositions) {

    }

    @Override
    public void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets) {

    }

    @Override
    public void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {

    }

    @Override
    public void showWeaponModeSelectionView(Effect effect1, Effect effect2) {

    }

}
