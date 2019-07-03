package it.polimi.se2019.view;

import it.polimi.se2019.controller.response.*;
import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.UpdateHandler;
import it.polimi.se2019.network.connection.Connection;
import it.polimi.se2019.network.connection.NetworkMessage;
import it.polimi.se2019.network.connection.serialization.NetworkMessageFactory;
import it.polimi.se2019.util.Observer;
import it.polimi.se2019.view.request.Request;
import it.polimi.se2019.view.request.serialization.RequestFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VirtualView extends View {
    private static final Logger logger = Logger.getLogger(VirtualView.class.getName());

    // NB. all units are in milliseconds
    private static final int PING_PERIOD = 1000;
    private static final int PONG_CHECK_DELAY = 1000;

    private Connection mConnection;
    private AtomicBoolean mHasReceivedPong = new AtomicBoolean(false);
    private AtomicBoolean mIsDisconnected = new AtomicBoolean(false);

    public VirtualView(PlayerColor ownerColor, Connection connection) {
        super(
                ownerColor,
                new UpdateHandler() {
                    @Override
                    public void fallbackHandle(Update update) {
                        logger.log(Level.INFO, "Sending update: {0}", update.getClass().getSimpleName());
                        connection.sendMessage(NetworkMessageFactory.toJson(update));
                    }
                }
        );

        mConnection = connection;
    }

    public Connection getConnection() {
        return mConnection;
    }

    @Override
    public void showMessage(String message) {
        sendResponse(new MessageResponse(message, false));
    }

    @Override
    public void reportError(String error) {
        sendResponse(new MessageResponse(error, true));
    }

    @Override
    public void showPowerUpsDiscardView() {
        sendResponse(new DiscardPowerUpResponse());
    }

    @Override
    public void showWeaponSelectionView(TileColor spawnColor) {
        sendResponse(new PickWeaponResponse(spawnColor));
    }

    @Override
    public void showValidPositions(List<Position> positions) {
        // TODO: need implementation in controller if we have time
    }

    @Override
    public void showPowerUpSelectionView(List<Integer> indexes) {
        sendResponse(new PickPowerUpsResponse(indexes));
    }

    @Override
    public void showRoomColorSelectionView(Set<TileColor> possibleColors) {
        sendResponse(new PickRoomColorResponse(possibleColors));
    }

    @Override
    public void showDirectionSelectionView() {
        sendResponse(new PickDirectionResponse());
    }

    @Override
    public void showPositionSelectionView(Set<Position> possiblePositions) {
        sendResponse(new PickPositionResponse(possiblePositions));
    }

    @Override
    public void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets) {
        sendResponse(new PickTargetsResponse(minToSelect, maxToSelect, possibleTargets));
    }

    @Override
    public void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {
        sendResponse(new PickEffectsResponse(priorityMap, possibleEffects));
    }

    @Override
    public void showWeaponModeSelectionView(Effect effect1, Effect effect2) {
        sendResponse(new PickWeaponModeResponse(effect1, effect2));
    }

    @Override
    public void showRespawnPowerUpDiscardView() {
        sendResponse(new PickRespawnPowerUpResponse());
    }

    @Override
    public void showAmmoColorSelectionView(Set<TileColor> possibleColors) {
        sendResponse(new PickAmmoColorResponse(possibleColors));
    }

    @Override
    public void reinitialize(InitializationInfo initInfo) {
        sendResponse(new InitializationInfoResponse(initInfo));
    }

    @Override
    public void confirmEndOfInteraction() {
        sendResponse(new InteractionEndResponse());
    }

    @Override
    public void registerAll(Observer<Request> observer) {
        register(observer);
    }

    private void sendResponse(Response response) {
        String rawMessage = NetworkMessageFactory.toJson(response);

        logger.log(Level.INFO, "Sending response: {0}", response.getClass().getSimpleName());

        mConnection.sendMessage(rawMessage);
    }

    public void startCheckingForDisconnection() {
        // send pings periodically
        new Timer().scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        // logger.log(Level.INFO, "Sending ping to {0} client", getOwnerColor());

                        // send ping
                        mConnection.sendMessage(NetworkMessageFactory.makeRawPing());

                        // check if pong has been sent in response after a certain delay
                        try {
                            Thread.sleep(PONG_CHECK_DELAY);
                        } catch (InterruptedException e) {
                            logger.log(Level.SEVERE, "{0} disconnection checking thread has been interrupted!");
                            Thread.currentThread().interrupt();
                            return;
                        }

                        // logger.log(Level.INFO, "checking for pong for {0} client", getOwnerColor());

                        // reconnect if pong is received after disconnection
                        if (mIsDisconnected.get() && mHasReceivedPong.getAndSet(false)) {
                            logger.log(Level.WARNING, "{0} has reconnected!", mOwnerColor);
                            mIsDisconnected.set(false);
                        }
                        // mark as disconnected if no pong is received
                        else if (!mIsDisconnected.get() && !mHasReceivedPong.getAndSet(false)) {
                            logger.log(Level.WARNING, "{0} client has disconnected!", mOwnerColor);
                            mIsDisconnected.set(true);
                        }
                    }
                }, 0, PING_PERIOD
        );
    }

    public void startReceivingMessages() {
        new Thread(() -> {
            while (true) {
                String rawMessage = mConnection.waitForMessage();

                NetworkMessage message = NetworkMessageFactory.fromJson(rawMessage);
                switch (message.getType()) {
                    case REQUEST:
                        Request request = RequestFactory.fromJson(message.getRawContents());
                        logger.log(Level.INFO, "Handling request from client: {0}", request.getClass().getSimpleName());
                        notify(request);
                        break;
                    case PONG:
                        // TODO: filter?
                        // logger.log(Level.INFO, "Received PONG from {0} client", mOwnerColor);
                        mHasReceivedPong.set(true);
                        break;
                    default:
                        logger.severe("Received client message of unknown type!");
                }

            }
        }).start();
    }
}

