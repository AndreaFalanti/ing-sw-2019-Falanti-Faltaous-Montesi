package it.polimi.se2019.network.client;

import it.polimi.se2019.controller.response.*;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.network.server.Connection;
import it.polimi.se2019.network.server.serialization.ServerMessageFactory;
import it.polimi.se2019.network.server.serialization.ServerMessageType;
import it.polimi.se2019.view.ResponseHandler;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.Request;
import it.polimi.se2019.view.request.serialization.RequestFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkHandler implements ClientNetworkHandler, ResponseHandler {
    private static final Logger logger = Logger.getLogger(NetworkHandler.class.getName());

    private View mView;
    private Connection mConnection;

    public NetworkHandler(View view, Connection connection) {
        mView = view;
        mConnection = connection;
    }

    @Override
    public void update(Request request) {
        String rawRequest = RequestFactory.toJson(request);

        logger.log(Level.INFO, "Sending request: {0}", rawRequest);
        mConnection.sendMessage(rawRequest);
    }

    public void startRecievingServerMessages() {
        new Thread(() -> {
            while (true) {
                // wait for message to be sent
                logger.info("Waiting for server messsage");
                String rawMessage = mConnection.waitForMessage();

                // receive it and unwrap it
                logger.log(Level.INFO, "Received server message: {0}", rawMessage);
                if (ServerMessageFactory.getMessageType(rawMessage).equals(ServerMessageType.Response)) {
                    // handle response
                    Response response = ServerMessageFactory.getAsResponse(rawMessage);
                    logger.info("Handling response...");
                    response.handleMe(this);
                }
                else if (ServerMessageFactory.getMessageType(rawMessage).equals(ServerMessageType.Update)) {
                    // handle update
                    Update update = ServerMessageFactory.getAsUpdate(rawMessage);
                    logger.info("Handling update...");
                    update.handleMe(mView.getUpdateHandler());
                }
            }
        }).start();
    }

    @Override
    public void handle(MessageResponse response) {
        mView.showMessage(response.getMessage());
    }

    @Override
    public void handle(PickWeaponResponse response) {
        mView.showWeaponSelectionView(response.getSpawnColor());
    }

    @Override
    public void handle(ValidMoveResponse response) {
        //TODO: implement later if we have time
    }

    @Override
    public void handle(DiscardPowerUpResponse response) {
        mView.showPowerUpsDiscardView();
    }

    @Override
    public void handle(PickDirectionResponse response) {
        mView.showDirectionSelectionView();
    }

    @Override
    public void handle(PickPositionResponse response) {
        mView.showPositionSelectionView(response.getPositions());
    }

    @Override
    public void handle(PickPowerUpsResponse response) {
        mView.showPowerUpSelectionView(response.getIndexes());
    }

    @Override
    public void handle(PickTargetsResponse response) {
        mView.showTargetsSelectionView(response.getMinTargets(), response.getMaxTargets(), response.getTargets());
    }

    @Override
    public void handle(PickEffectsResponse response) {
        mView.showEffectsSelectionView(response.getPriorityMap(), response.getPossibleEffects());
    }

    @Override
    public void handle(PickWeaponModeResponse response) {
        mView.showWeaponModeSelectionView(response.getEffect1(), response.getEffect2());
    }

    @Override
    public void handle(PickRoomColorResponse response) {
        mView.showRoomColorSelectionView(response.getTileColors());
    }

    @Override
    public void handle(PickRespawnPowerUpResponse response) {
        mView.showRespawnPowerUpDiscardView();
    }

    @Override
    public void handle(InitializationInfoResponse response) {
        mView.reinitialize(response.getInitializationInfo());
    }

    @Override
    public boolean sendUsername(String username) {
        mConnection.sendMessage(username);

        String message = mConnection.waitForMessage();
        if (message == null || !message.equals("ok")) {
            logger.info(message);
            return false;
        }

        return true;
    }
}
