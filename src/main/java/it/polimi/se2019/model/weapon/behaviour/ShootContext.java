package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.WeaponAction;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.weapon.Weapon;

import javax.swing.text.html.Option;
import java.awt.image.ShortLookupTable;
import java.util.*;

public class ShootContext {
    // statics
    private static final String MISSING_PLAYER_MSG = "Shooter is not present among provided list of players!";

    // fields
    private Board mBoard;
    private Set<Player> mPlayers;
    private PlayerColor mShooterColor;
    private Optional<Expression> mCurrentExpression;

    // temporary info representing changed game state
    AmmoValue mPayedCost;
    Set<Player> mAffectedPlayres; // only used for keeping track of opponents shoved around by weapon,
    // ergo, only position is interesting

    // trivial constructors
    public ShootContext(Board board, Set<Player> players, PlayerColor shooterColor, Expression weaponBehaviour) {
        // safety check to assure that shooter is present among provided players
        if (!players.stream().anyMatch(pl -> pl.getColor() == shooterColor))
            throw new IllegalArgumentException(MISSING_PLAYER_MSG);

        // initialize fields
        mBoard = board;
        mPlayers = players;
        mShooterColor = shooterColor;
        mCurrentExpression = Optional.ofNullable(weaponBehaviour);
    }
    public ShootContext(Board board, Set<Player> players, PlayerColor shooterColor) {
        this(board, players, shooterColor, null);
    }

    // internal eval used to eval current expression and keep track of it during shooting
    public ShootResult eval() {
        Expression expressionResult = mCurrentExpression.orElseThrow(
                () -> new UnsupportedOperationException("Trying to call eval on a context with no associated expression!")
        ).eval(this);

        if (expressionResult.isDone())
            return ShootResult.fromAction(mCurrentShootAction);
        else
            return ShootResult.fromRequest();
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
}

