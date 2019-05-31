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
    final private List<Action> mCachedActions = new ArrayList<>();
    private final Deque<AtomicExpression> mRequestedInfo = new ArrayDeque<>();
    private final Deque<AtomicExpression> mProvidedInfo = new ArrayDeque<>();

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
        return mRequestedInfo.isEmpty() && mProvidedInfo.isEmpty();
    }


    // request and collect info
    public void requestInfo(AtomicExpression infoRequested) {
        mRequestedInfo.push(infoRequested);
    }
    public Optional<AtomicExpression> peekRequestedInfo() {
        return Optional.ofNullable(mRequestedInfo.peek());
    }
    public AtomicExpression popRequestedInfo() {
        if (mRequestedInfo.isEmpty())
            throw new UnsupportedOperationException("Trying to pop requested info from and empty stack!");

        return mRequestedInfo.pop();
    }
    public void provideInfo(AtomicExpression infoProvided) {
        mProvidedInfo.push(infoProvided);
    }
    public void provideInfo(List<AtomicExpression> infoProvided) {
        infoProvided.forEach(mProvidedInfo::push);
    }
    public Optional<AtomicExpression> peekProvidedInfo() {
        return Optional.ofNullable(mProvidedInfo.peek());
    }
    public AtomicExpression popProvidedInfo() {
        if (mProvidedInfo.isEmpty())
            throw new UnsupportedOperationException("Trying to pop provided info from and empty stack!");

        return mProvidedInfo.pop();
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

