package it.polimi.se2019.view;

import com.google.gson.Gson;
import it.polimi.se2019.controller.response.*;
import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.UpdateHandler;
import it.polimi.se2019.network.server.PlayerThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.logging.Logger;

public class SocketVirtualView extends View {
    private static final Logger logger = Logger.getLogger(PlayerThread.class.getName());

    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;

    private Gson mGson = new Gson();

    public SocketVirtualView() {
        super(
                new UpdateHandler() {
                    @Override
                    public void fallbackHandle(Update update) {
                        /*String json = gson.toJson(update);
                        mOut.print(json);*/
                    }
                }
        );

        try {
            mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void showMessage(String message) {
        mOut.print(mGson.toJson(new MessageResponse(message, false)));
    }

    @Override
    public void reportError(String error) {
        mOut.print(mGson.toJson(new MessageResponse(error, true)));
    }

    @Override
    public void showPowerUpsDiscardView() {
        mOut.print(mGson.toJson(new DiscardPowerUpResponse()));
    }

    @Override
    public void showWeaponSelectionView(TileColor spawnColor) {
        mOut.print(mGson.toJson(new PickWeaponResponse(spawnColor)));
    }

    @Override
    public void showValidPositions(List<Position> positions) {

    }

    @Override
    public void showDirectionSelectionView() {
        mOut.print(mGson.toJson(new PickDirectionResponse()));
    }

    @Override
    public void showPositionSelectionView(Set<Position> possiblePositions) {
        mOut.print(mGson.toJson(new PickPositionResponse(possiblePositions)));
    }

    @Override
    public void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets) {

    }

    @Override
    public void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap, int currentPriority) {

    }

    @Override
    public void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {
        
    }

    @Override
    public void showWeaponModeSelectionView(Effect effect1, Effect effect2) {

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
