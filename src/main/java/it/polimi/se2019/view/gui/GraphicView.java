package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.view.View;

import java.util.Collections;
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
    public boolean[] discardPowerUps() {
        return new boolean[0];
    }

    @Override
    public Direction pickDirection() {
        return Direction.NORTH;
    }

    @Override
    public Position selectPosition(Set<Position> possiblePositions) {
        return null;
    }

    @Override
    synchronized public Set<PlayerColor> selectTargets(int possibleTargets, int minToSelect, Set<PlayerColor> maxToSelect) {
        // tell view to notify
        mMainFrameController.getUndoButton().setOnMouseClicked(event -> {
            synchronized (this) {
                notifyAll();
            }
        });

        // wait for notification
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // return GREEN at random when notified
        return Collections.singleton(PlayerColor.GREEN);
    }

    @Override
    public Set<String> selectEffects(SortedMap<Integer, Set<Effect>> priorityMap, int currentPriority) {
        return null;
    }

}