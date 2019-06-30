package it.polimi.se2019.view;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.UpdateHandler;
import it.polimi.se2019.view.request.Request;
import it.polimi.se2019.view.request.TargetsSelectedRequest;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

public class TestView extends View {
    public TestView(PlayerColor ownerColor) {
        super(ownerColor, new UpdateHandler() {
            @Override
            public void fallbackHandle(Update update) {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Override
    public void showMessage(String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reportError(String error) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void showPowerUpsDiscardView() {
        throw new UnsupportedOperationException();

    }

    @Override
    public void showWeaponSelectionView(TileColor spawnColor) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void showValidPositions(List<Position> positions) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void showPowerUpSelectionView(List<Integer> indexes) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void showRoomColorSelectionView(Set<TileColor> possibleColors) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void showDirectionSelectionView() {

        throw new UnsupportedOperationException();
    }

    @Override
    public void showPositionSelectionView(Set<Position> possiblePositions) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets) {
        System.out.println("Test view selecting targets...");
        notify(new TargetsSelectedRequest(Collections.singleton(PlayerColor.GREEN), mOwnerColor));
    }

    @Override
    public void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void showWeaponModeSelectionView(Effect effect1, Effect effect2) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void showRespawnPowerUpDiscardView() {
        throw new UnsupportedOperationException();

    }

    @Override
    public void reinitialize(InitializationInfo initInfo) {
        throw new UnsupportedOperationException();

    }
}
