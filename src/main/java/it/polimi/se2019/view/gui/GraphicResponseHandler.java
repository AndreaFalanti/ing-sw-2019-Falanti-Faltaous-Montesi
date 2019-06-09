package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.response.DiscardPowerUpResponse;
import it.polimi.se2019.controller.response.MessageResponse;
import it.polimi.se2019.controller.response.PickWeaponResponse;
import it.polimi.se2019.controller.response.ValidMoveResponse;
import it.polimi.se2019.view.ResponseHandler;

public class GraphicResponseHandler implements ResponseHandler {
    private MainScreen mMainController;

    public GraphicResponseHandler(MainScreen mainController) {
        mMainController = mainController;
    }

    @Override
    public void handle(MessageResponse response) {
        mMainController.logToChat(response.getMessage());
    }

    @Override
    public void handle(PickWeaponResponse response) {
        if (response.getSpawnColor() == null) {
            mMainController.enableWeaponBoxForSendingIndex();
        }
        else {
            mMainController.getBoardController().enableSpawnWeaponBoxForSendingIndex(response.getSpawnColor());
        }
    }

    @Override
    public void handle(ValidMoveResponse response) {

    }

    @Override
    public void handle(DiscardPowerUpResponse response) {
        mMainController.enablePowerUpBoxForSendingDiscardedIndex();
    }
}
