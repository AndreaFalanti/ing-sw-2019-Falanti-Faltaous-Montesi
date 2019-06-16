package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.Request;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class ShootContext {
    // statics
    private static final String MISSING_PLAYER_MSG = "Shooter is not present among provided list of players!";

    // special variables handled directly by expression other than Load and Look
    public static final String SPECIAL_VAR_LAST_SELECTED = "$last";

    // fields
    private Game mGame;
    private View mView;
    private PlayerColor mShooterColor;
    private Map<String, Expression> mScope;
    private ShootInteraction mShootInteraction;

    // trivial constructor
    public ShootContext(Game game, View view, PlayerColor shooterColor, ShootInteraction shootInteraction) {
        // safety check to assure that shooter is present among provided players
        List<Player> players = game.getPlayers();
        if (!players.stream().anyMatch(pl -> pl.getColor() == shooterColor))
            throw new IllegalArgumentException(MISSING_PLAYER_MSG);

        // initialize fields
        mGame = game;
        mView = view;
        mShooterColor = shooterColor;
        mScope = new HashMap<>();
        mShootInteraction = shootInteraction;
    }

    // trivial getters
    public Board getBoard() {
        return mGame.getBoard();
    }

    public Set<Player> getPlayers() {
        return new HashSet<>(mGame.getPlayers());
    }

    public PlayerColor getShooterColor() {
        return mShooterColor;
    }

    public Player getShooter() {
        return getPlayers().stream()
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

    public Game getGame() {
        return mGame;
    }

    public BlockingQueue<Request> getRequestQueue() {
        return mShootInteraction.getRequestQueue();
    }
}

