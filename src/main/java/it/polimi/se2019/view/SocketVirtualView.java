package it.polimi.se2019.view;

import com.google.gson.Gson;
import it.polimi.se2019.controller.response.*;
import it.polimi.se2019.controller.response.serialization.ResponseFactory;
import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.UpdateHandler;
import it.polimi.se2019.model.update.serialization.UpdateFactory;
import it.polimi.se2019.view.request.Request;
import it.polimi.se2019.view.request.serialization.RequestFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketVirtualView extends View {
    private static final Logger logger = Logger.getLogger(SocketVirtualView.class.getName());

    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;

    public SocketVirtualView(PlayerColor ownerColor) {
        super(
                ownerColor,
                new UpdateHandler() {
                    @Override
                    public void fallbackHandle(Update update) {
                        throw new UnsupportedOperationException("Custom update handler not set");
                    }
                }
        );


    }

    public void setupUpdateHandler () {
        mUpdateHandler = new UpdateHandler() {
            @Override
            public void fallbackHandle(Update update) {
                        String json = UpdateFactory.toJson(update);
                        mOut.print(json);
            }
        };
    }

    public void setupSocket (Socket socket) {
        mSocket = socket;

        try {
            mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
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
    public void reinitialize(InitializationInfo initInfo) {
        sendResponse(new InitializationInfoResponse(initInfo));
    }

    private void sendResponse (Response response) {
        logger.log(Level.INFO, "Sending {0}...", response.getClass().getSimpleName());
        mOut.println(ResponseFactory.toJson(response));
    }

    public void getRequests () {
        while (!mSocket.isClosed()) {
            logger.info("Waiting for a request...");
            try {
                String json = mIn.readLine();
                Request request = RequestFactory.fromJson(json);
                notify(request);
            } catch (IOException e) {
                logger.severe(e.getMessage());
            }
        }
    }
}
