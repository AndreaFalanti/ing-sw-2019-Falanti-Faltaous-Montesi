package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.WeaponAction;
import it.polimi.se2019.model.board.Board;

import javax.swing.text.html.Option;
import java.util.*;

public class ShootContext {
    // statics
    private static final String MISSING_PLAYER_MSG = "Shooter is not present among provided list of players!";

    // fields
    private Board mBoard;
    private Set<Player> mPlayers;
    private PlayerColor mShooterColor;
    final private List<Action> mCachedActions = new ArrayList<>();
    private Optional<Expression> mRequestedInfo = Optional.empty();
    private Optional<Expression> mProvidedInfo = Optional.empty();

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

    // true if no info is required from context
    public boolean isComplete() {
        return !mProvidedInfo.isPresent();
    }


    // request and collect info
    public void requestInfo(Expression infoRequested) {
         if (mRequestedInfo.isPresent())
            throw new UnsupportedOperationException("Info already requested!");

        mRequestedInfo = Optional.of(infoRequested);
    }
    public Optional<Expression> peekRequestedInfo() {
        return mRequestedInfo;
    }
    public Expression consumeRequestedInfo() {
        Expression toReturn = mRequestedInfo.orElseThrow(() ->
                new UnsupportedOperationException("No info requested to consume!")
        );

        mRequestedInfo = Optional.empty();

        return toReturn;
    }
    public void provideInfo(Expression infoProvided) {
        if (mProvidedInfo.isPresent())
            throw new UnsupportedOperationException("Info already provided!");

        mProvidedInfo = Optional.of(infoProvided);
    }
    public Optional<Expression> peekProvidedInfo() {
        return mProvidedInfo;
    }
    public Expression consumeProvidedInfo() {
        Expression toReturn = mProvidedInfo.orElseThrow(() ->
                new UnsupportedOperationException("No info provided to consume!")
        );

        mProvidedInfo = Optional.empty();

        return toReturn;
    }

    // build resulting action
    void pushAction(Action action) {
        mCachedActions.add(action);
    }
    public Action getResultingAction() {
        List<Action> tmp = new ArrayList<>(mCachedActions);
        mCachedActions.clear();
        return new WeaponAction(tmp);
    }
}

