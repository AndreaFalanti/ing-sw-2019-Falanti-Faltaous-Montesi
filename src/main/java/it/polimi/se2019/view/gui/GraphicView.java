package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.view.View;

import java.util.Set;
import java.util.SortedMap;

public class GraphicView extends View {
    private MainScreen mMainFrameController;


    public GraphicView(MainScreen mainFrameController) {
        super(new GraphicResponseHandler(mainFrameController), new GraphicUpdateHandler(mainFrameController));
        mMainFrameController = mainFrameController;
    }

    @Override
    public void showMessage(String message){
        mMainFrameController.logToChat(message);
    }

    @Override
    public void reportError(String error){

    }

    @Override
    public int parseWeaponInformation(TileColor tileColor) {
        return 0;
    }

    @Override
    public int parseWeaponInformation() {
        return 0;
    }

    @Override
    public void showPowerUpsDiscardView() {
        mMainFrameController.activateDirectionTab();
    }

    @Override
    public void showDirectionSelectionView() {

    }

    @Override
    public Direction pickDirection() {
        return null;
    }

    @Override
    public Position selectPosition(Set<Position> possiblePositions) {
        return null;
    }

    @Override
    public Set<PlayerColor> selectTargets(int possibleTargets, int minToSelect, Set<PlayerColor> maxToSelect) {
        return null;
    }

    @Override
    public Set<String> selectEffects(SortedMap<Integer, Set<Effect>> priorityMap, int currentPriority) {
        return null;
    }

}