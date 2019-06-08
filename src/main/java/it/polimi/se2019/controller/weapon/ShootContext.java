package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.view.View;

import java.util.Map;
import java.util.Set;

public class ShootContext {
    // statics
    private static final String MISSING_PLAYER_MSG = "Shooter is not present among provided list of players!";

    // fields
    private Board mBoard;
    private Set<Player> mPlayers;
    private PlayerColor mShooterColor;
    private int mCurrentPriority;
    private View mView;
    private Map<String, Expression> mScope;

    // trivial constructors
    public ShootContext(Board board, Set<Player> players, PlayerColor shooterColor) {
        // safety check to assure that shooter is present among provided players
        if (!players.stream().anyMatch(pl -> pl.getColor() == shooterColor))
            throw new IllegalArgumentException(MISSING_PLAYER_MSG);

        // initialize fields
        mBoard = board;
        mPlayers = players;
        mShooterColor = shooterColor;
    }

    // trivial getters
    public Board getBoard() {
        return mBoard;
    }

    public Set<Player> getPlayers() {
        return mPlayers;
    }

    public PlayerColor getShooterColor() {
        return mShooterColor;
    }

    public Player getShooter() {
        return mPlayers.stream()
                .filter(pl -> pl.getColor() == getShooterColor())
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(MISSING_PLAYER_MSG));
    }

    public Position getShooterPosition() {
        return getShooter().getPos();
    }

    public View getView() {
        return mView;
    }

    // manipulate scope
    public Expression getVar(String name) {
        if (mScope.get(name) == null)
            throw new IllegalArgumentException("No variable named " + name + " found in scope...");

        return mScope.get(name);
    }

    public void setVar(String name, Expression value) {
        mScope.put(name, value);
    }

    public int getCurrentPriority() {
        return mCurrentPriority;
    }
}

