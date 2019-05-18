package it.polimi.se2019.network.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.view.View;

import java.util.ArrayList;
import java.util.List;

public class GameThread extends Thread {
    private Game model;
    private Controller controller;
    private View[] virtualViews;
    private List<PlayerConnection> mPlayerConnections;

    public GameThread (List<PlayerConnection> players){
        mPlayerConnections = new ArrayList<>(players);
    }

    @Override
    public void run() {
        List<Player> players = new ArrayList<>();
        for (PlayerConnection playerConnection : mPlayerConnections) {
            //TODO: find a way to communicate back to both socket and rmi clients
        }
    }

    public void addPlayer (PlayerConnection playerConnection) {
        mPlayerConnections.add(playerConnection);
    }

    public void removePlayer (PlayerConnection playerConnection) {
        mPlayerConnections.remove(playerConnection);
    }
}
