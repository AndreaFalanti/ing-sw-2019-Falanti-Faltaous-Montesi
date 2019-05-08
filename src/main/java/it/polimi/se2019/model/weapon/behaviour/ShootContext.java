package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;

import java.util.Deque;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

public class ShootContext {
    // statics
    private static final String MISSING_PLAYER_MSG = "Shooter is not present among provided list of players!";

    // fields
    Board mBoard;
    Set<Player> mPlayers;
    PlayerColor mShooterColor;
    Deque<Expression> mProvidedInfo;

    // trivial constructors
    public ShootContext(Board board, Set<Player> players, PlayerColor shooterColor) {
        // safety check to assure that shooter is present among provided players
        if (!players.stream().filter(pl -> pl.getColor() == shooterColor).findFirst().isPresent())
            throw new IllegalArgumentException(MISSING_PLAYER_MSG);

        // initialize fields
        mBoard = board;
        mPlayers = players;
        mShooterColor = shooterColor;
    }

    // trivial getters
    Board getBoard() {
        return mBoard;
    }
    PlayerColor getShooterColor() {
        return mShooterColor;
    }
    Player getShooter() {
        return mPlayers.stream().
                filter(pl -> pl.getColor() == getShooterColor())
                .findFirst()
                .orElseThrow(() ->
                    new IllegalStateException(MISSING_PLAYER_MSG));
    }
    Position getShooterPosition() {
        return getShooter().getPos();
    }

    // for manipulating stack info
    public void pushInfo(Expression info) {
        mProvidedInfo.push(info);
    }
    public Optional<Expression> popInfo() {
        if (mProvidedInfo.isEmpty())
            return Optional.empty();

        return Optional.of(mProvidedInfo.pop());
    }
}
