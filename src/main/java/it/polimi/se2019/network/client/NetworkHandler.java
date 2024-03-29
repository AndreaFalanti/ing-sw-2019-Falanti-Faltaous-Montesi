package it.polimi.se2019.network.client;

import it.polimi.se2019.controller.response.*;
import it.polimi.se2019.controller.response.serialization.ResponseFactory;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.serialization.UpdateFactory;
import it.polimi.se2019.network.connection.Connection;
import it.polimi.se2019.network.connection.NetworkMessage;
import it.polimi.se2019.network.connection.serialization.NetworkMessageFactory;
import it.polimi.se2019.view.ResponseHandler;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.Request;

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
        String rawMessage = NetworkMessageFactory.toJson(request);

        logger.log(Level.INFO, "Sending request: {0}", rawMessage);
        mConnection.sendMessage(rawMessage);
    }

    public void startReceivingMessages() {
        new Thread(() -> {
            while (true) {
                // wait for message to be sent
                // logger.info("Waiting for server message");
                String rawMessage = mConnection.waitForMessage();

                // receive it and unwrap it
                // logger.log(Level.INFO, "Received server message: {0}", rawMessage);
                NetworkMessage message = NetworkMessageFactory.fromJson(rawMessage);
                switch (message.getType()) {
                    case RESPONSE:
                        // responses are handled by calling the appropriate view method
                        logger.log(Level.INFO, "Handling response from server: {0}", message.getRawContents());
                        Response response = ResponseFactory.fromJson(message.getRawContents());
                        response.handleMe(this);
                        break;
                    case UPDATE:
                        // updates are reflected on the view
                        logger.log(Level.INFO, "Handling update from server: {0}", message.getRawContents());
                        Update update = UpdateFactory.fromJson(message.getRawContents());
                        update.handleMe(mView.getUpdateHandler());
                        break;
                    case PING:
                        // pings are answered with pongs
                        // logger.info("Received PING from server. Answering with PONG...");
                        mConnection.sendMessage(NetworkMessageFactory.makeRawPong());
                        break;
                    default:
                        logger.severe("Received server message of unknown type!");
                        break;
                }
            }
        }).start();
    }

    @Override
    public void handle(MessageResponse response) {
        if (response.isError()) {
            mView.reportError(response.getMessage());
        }
        else {
            mView.showMessage(response.getMessage());
        }
    }

    @Override
    public void handle(PickWeaponResponse response) {
        String message = (response.getSpawnColor() != null) ? "Select weapon from " + response.getSpawnColor() + " tile" :
                "Select weapon from your hand";
        mView.showMessage(message);
        mView.showWeaponSelectionView(response.getSpawnColor());
    }

    @Override
    public void handle(ValidMoveResponse response) {
        //TODO: implement later if we have time
    }

    @Override
    public void handle(DiscardPowerUpResponse response) {
        mView.showMessage("Discard powerUps to cover the cost");
        mView.showPowerUpsDiscardView();
    }

    @Override
    public void handle(PickDirectionResponse response) {
        mView.showMessage("Pick a direction");
        mView.showDirectionSelectionView();
    }

    @Override
    public void handle(PickPositionResponse response) {
        mView.showMessage("Pick a position");
        mView.showPositionSelectionView(response.getPositions());
    }

    @Override
    public void handle(PickPowerUpsResponse response) {
        mView.showPowerUpSelectionView(response.getIndexes());
    }

    @Override
    public void handle(PickTargetsResponse response) {
        mView.showMessage("Select targets");
        mView.showTargetsSelectionView(response.getMinTargets(), response.getMaxTargets(), response.getTargets());
    }

    @Override
    public void handle(PickEffectsResponse response) {
        mView.showMessage("Pick weapon effect to activate");
        mView.showEffectsSelectionView(response.getPriorityMap(), response.getPossibleEffects());
    }

    @Override
    public void handle(PickWeaponModeResponse response) {
        mView.showMessage("Pick weapon mode");
        mView.showWeaponModeSelectionView(response.getEffect1(), response.getEffect2());
    }

    @Override
    public void handle(PickRoomColorResponse response) {
        mView.showMessage("Select room");
        mView.showRoomColorSelectionView(response.getTileColors());
    }

    @Override
    public void handle(PickRespawnPowerUpResponse response) {
        mView.showMessage("Discard a powerUp to respawn");
        mView.showRespawnPowerUpDiscardView();
    }

    @Override
    public void handle(InitializationInfoResponse response) {
        mView.reinitialize(response.getInitializationInfo());
    }

    @Override
    public void handle(PickAmmoColorResponse response) {
        mView.showMessage("Select ammo to discard for paying targeting scope card");
        mView.showAmmoColorSelectionView(response.getAmmoColors());
    }

    @Override
    public void handle(InteractionEndResponse response) {
        mView.confirmEndOfInteraction();
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

    @Override
    public void registerObservablesFromView() {
        mView.registerAll(this);
    }
}
