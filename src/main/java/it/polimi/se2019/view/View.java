package it.polimi.se2019.view;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Observable;
import it.polimi.se2019.util.Observer;

public abstract class View implements Observable, Observer {

    private Player players;
    private Board board;
    private PlayerColor activePlayer;

    public abstract void showMessage(String message);

    public abstract void reportError(String error);

    public abstract void updateBoard();

    public abstract void updatePlayers();

    public abstract void interact();

    public void notifyController() {

    }

    public PlayerColor getActivePlayer() {

        return null;
    }

}