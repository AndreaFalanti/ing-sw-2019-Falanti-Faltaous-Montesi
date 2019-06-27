package it.polimi.se2019.view;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.UpdateHandler;
import it.polimi.se2019.view.request.TargetsSelectedRequest;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

public class TestView extends View {
    PlayerColor mOwnerColor;

    public TestView(PlayerColor ownerColor) {
        super(
                new UpdateHandler() {
                    @Override
                    public void fallbackHandle(Update update) {

                    }
                }
        );

        mOwnerColor = ownerColor;
    }

    @Override
    public void showMessage(String message) {
        System.out.println("MSG: " + message);
    }

    @Override
    public void reportError(String error) {
        System.out.println("ERROR: " + error);
    }

    @Override
    public void showPowerUpsDiscardView() {
        throw new UnsupportedOperationException("WIP");
    }

    @Override
    public void showWeaponSelectionView(TileColor spawnColor) {
        throw new UnsupportedOperationException("WIP");
    }

    @Override
    public void showValidPositions(List<Position> positions) {
        throw new UnsupportedOperationException("WIP");
    }

    @Override
    public void showPowerUpSelectionView(List<Integer> indexes) {
        throw new UnsupportedOperationException("WIP");
    }

    @Override
    public void showRoomColorSelectionView(Set<TileColor> possibleColors) {
        throw new UnsupportedOperationException("WIP");
    }

    @Override
    public void showDirectionSelectionView() {
        throw new UnsupportedOperationException("WIP");
    }

    @Override
    public void showPositionSelectionView(Set<Position> possiblePositions) {
        throw new UnsupportedOperationException("WIP");
    }

    @Override
    public void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets) {
        System.out.println(mOwnerColor + " selecting targets among " + possibleTargets);
        System.out.println("Responding with Luigi of course");
        notify(new TargetsSelectedRequest(Collections.singleton(PlayerColor.GREEN), mOwnerColor));
    }

    @Override
    public void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {
        throw new UnsupportedOperationException("WIP");
    }

    @Override
    public void showWeaponModeSelectionView(Effect effect1, Effect effect2) {
        throw new UnsupportedOperationException("WIP");
    }

    @Override
    public void reinitialize(InitializationInfo initiInfo) {

    }
}
