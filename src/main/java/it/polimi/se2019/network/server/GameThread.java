package it.polimi.se2019.network.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class GameThread extends Thread {
    private static final Logger logger = Logger.getLogger(GameThread.class.getName());

    private Game mGame;
    private Controller mController;
    private View[] mVirtualViews;
    private List<PlayerConnection> mPlayerConnections;
    private boolean mStarted;

    private Random mRandom = new Random();
    private List<PlayerColor> mColors = new ArrayList<>(Arrays.asList(PlayerColor.values()));

    public GameThread (List<PlayerConnection> players){
        mPlayerConnections = new ArrayList<>(players);
    }

    public int getPlayersNum () {
        return mPlayerConnections.size();
    }

    public boolean isStarted() {
        return mStarted;
    }

    @Override
    public void run() {
        logger.info("Starting game creation ...");
        List<Player> players = new ArrayList<>();

        // TODO: make players choose colors and board? If not, put all this block in constructor
        for (PlayerConnection playerConnection : mPlayerConnections) {
            int randomIndex = mRandom.nextInt(mColors.size());
            players.add(new Player(playerConnection.getUsername(), mColors.get(randomIndex)));
            mColors.remove(randomIndex);
        }
        Board board = Board.fromJson(Jsons.get("boards/game/board1"));
        int killsTarget = 8;

        // TODO: add a timer and start only when players are full or timer is done
        initializeGame(board, players, killsTarget);
    }

    private void initializeGame (Board board, List<Player> players, int killsTarget) {
        mGame = new Game(board, players, killsTarget);
        mController = new Controller();
        // TODO: create virtual views

        mStarted = true;
        logger.info("Game created successfully");
    }

    public void addPlayer (PlayerConnection playerConnection) {
        mPlayerConnections.add(playerConnection);
    }

    public void removePlayer (PlayerConnection playerConnection) {
        mPlayerConnections.remove(playerConnection);
    }
}
