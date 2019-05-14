package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.WeaponAction;
import it.polimi.se2019.model.board.Board;

import java.util.*;

public class ShootContext {
    // statics
    private static final String MISSING_PLAYER_MSG = "Shooter is not present among provided list of players!";

    // fields
    private Board mBoard;
    private Set<Player> mPlayers;
    private PlayerColor mShooterColor;
    private final Deque<Expression> mInfo = new ArrayDeque();
    private final Deque<Action> mProducedActions = new ArrayDeque();

    // temporary info representing changed game state
    AmmoValue mPayedCost;
    Set<Player> mAffectedPlayres; // only used for keeping track of opponents shoved around by weapon,
                                  // ergo, only position is interesting

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
    Board getBoard() {
        return mBoard;
    }
    Set<Player> getPlayers() {
        return mPlayers;
    }
    PlayerColor getShooterColor() {
        return mShooterColor;
    }
    Player getShooter() {
        return mPlayers.stream()
                .filter(pl -> pl.getColor() == getShooterColor())
                .findFirst()
                .orElseThrow(() ->
                    new IllegalStateException(MISSING_PLAYER_MSG));
    }
    Position getShooterPosition() {
        return getShooter().getPos();
    }

    // for manipulating actions stack
    public void pushAction(Action info) {
        mProducedActions.push(info);
    }
    public Optional<Action> popAction() {
        if (mProducedActions.isEmpty())
            return Optional.empty();

        return Optional.of(mProducedActions.pop());
    }

    // for manipulating info stack
    public void pushInfo(Expression info) {
        mInfo.push(info);
    }
    public Optional<Expression> popInfo() {
        if (mInfo.isEmpty())
            return Optional.empty();

        return Optional.of(mInfo.pop());
    }

    // true if context is complete, and thus does not need any additional info for generating shoot
    public boolean isComplete() {
        return mInfo.isEmpty();
    }

    // get the resulting shoot action
    public Action getResultingAction() {
        return new WeaponAction(mProducedActions.stream()
                .toArray(Action[]::new)
        );
    }
}
