package it.polimi.se2019.network.server;

import it.polimi.se2019.network.server.GameThread;
import it.polimi.se2019.network.server.SocketPlayerConnection;

import java.util.ArrayList;

public abstract class Server implements Runnable {

    public static final int PORT = 4567;
    private ArrayList<GameThread> games;
    private ArrayList<SocketPlayerConnection> waitingPlayer = new ArrayList<>();
    // TODO: name this variable
     private ArrayList<SocketPlayerConnection> playersOnLine = new ArrayList<>();

    public void waitingRoom(SocketPlayerConnection player){
            //TODO implementation
    }

    @Override
    public void run(){

    }

    public void registerConnection(SocketPlayerConnection player){
        waitingPlayer.add(player);
    }

    public void deregisterConnection(SocketPlayerConnection player){

        waitingPlayer.remove(player);
        //TODO playeronline
    }

    public static void main(String[] args) {
    }
}