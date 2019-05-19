package it.polimi.se2019.view;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Observable;
import it.polimi.se2019.util.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class View implements Observable, Observer {

    protected ArrayList<Player> mPlayers;
    private Board board;
    protected PlayerColor activePlayer;
    protected Player owner;

    public abstract void showMessage(String message);

    public abstract void reportError(String error);

    public abstract void updateBoard();

    public abstract void updatePlayers();

    public abstract void interact();

    public void notifyController() {
        // TODO: implement
    }

    public PlayerColor getActivePlayer() {

        return null;
    }

    public List<Player> getPlayers(){
        return mPlayers;
    }

}