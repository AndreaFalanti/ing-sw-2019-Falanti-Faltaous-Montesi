package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.view.View;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

public class GraphicView extends View {
    private MainScreen mMainFrameController;


    public GraphicView(PlayerColor color, MainScreen mainFrameController) {
        super(color, new GraphicUpdateHandler(mainFrameController));
        mMainFrameController = mainFrameController;
    }

    @Override
    public void showMessage(String message){
        mMainFrameController.logToChat(message);
    }

    @Override
    public void reportError(String error){
        mMainFrameController.logToChat(error);
    }

    @Override
    public void showPowerUpsDiscardView() {
        mMainFrameController.enablePowerUpBoxForSendingDiscardedIndex();
    }

    @Override
    public void showWeaponSelectionView(TileColor spawnColor) {
        if (spawnColor == null) {
            mMainFrameController.enableWeaponBoxForSendingIndex();
        }
        else {
            mMainFrameController.getBoardController().enableSpawnWeaponBoxForSendingIndex(spawnColor);
        }
    }

    @Override
    public void showValidPositions(List<Position> positions) {

    }

    @Override
    public void showPowerUpSelectionView(List<Integer> indexes) {

    }

    @Override
    public void showRoomColorSelectionView(Set<TileColor> possibleColors) {
        mMainFrameController.activateRoomTab(possibleColors);
    }

    @Override
    public void showDirectionSelectionView() {
        mMainFrameController.activateDirectionTab();
    }

    @Override
    public void showPositionSelectionView(Set<Position> possiblePositions) {

    }

    @Override
    public void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets) {
        mMainFrameController.activateTargetsTab(possibleTargets, minToSelect, maxToSelect);
    }



    @Override
    public void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {

    }

    @Override
    public void showWeaponModeSelectionView(Effect effect1, Effect effect2) {
        mMainFrameController.activateEffectsTabForWeaponMode(effect1, effect2);
    }

}