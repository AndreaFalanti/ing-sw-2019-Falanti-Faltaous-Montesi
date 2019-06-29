package it.polimi.se2019.network.client;

import com.google.gson.Gson;
import it.polimi.se2019.controller.response.*;
import it.polimi.se2019.controller.response.serialization.ResponseFactory;
import it.polimi.se2019.view.ResponseHandler;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.Request;
import it.polimi.se2019.view.request.serialization.RequestFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketNetworkHandler implements ClientNetworkHandler, ResponseHandler {
    private static final Logger logger = Logger.getLogger(SocketNetworkHandler.class.getName());

    private View mView;
    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;

    public SocketNetworkHandler(View view, Socket socket) {
        mView = view;
        mSocket = socket;

        try {
            mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void update(Request request) {
        logger.log(Level.INFO, "Sending a request of type: {0}", request.getClass().getSimpleName());
        mOut.print(RequestFactory.toJson(request));
    }

    public void activateGameMessageReception () {
        new Thread(this::receiveResponses).start();
    }

    private void receiveResponses() {
        while (!mSocket.isClosed()) {
            logger.info("Waiting for a request...");
            try {
                String json = mIn.readLine();
                Response response = ResponseFactory.fromJson(json);
                logger.info("Handling request...");
                response.handleMe(this);
            } catch (IOException e) {
                logger.severe(e.getMessage());
                break;
            }
        }
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
        mOut.println(username);
        String message = null;
        try {
            message = mIn.readLine();
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
        if (message == null || !message.equals("ok")) {
            logger.info(message);
            return false;
        }

        return true;
    }
}
