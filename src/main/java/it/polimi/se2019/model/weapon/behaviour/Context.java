package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;

import java.util.Stack;

public class Context {
    // fields
    Game mGameStatus; // TODO: discuss about necessity of GameStatus class
    PlayerColor mShooterColor;
    Stack<Expression> mProvidedInfo;

    // trivial constructors
    public Context(Game gameStatus, PlayerColor shooterColor) {
        mGameStatus = gameStatus;
        mShooterColor = shooterColor;
    }

    // trivial getters
    Board getBoard() {
        return mGameStatus.getBoard();
    }
    PlayerColor getShooterColor() {
        return mShooterColor;
    }
    Position getShooterPosition() {
        return mGameStatus.getPlayerFromColor(mShooterColor).getPos();
    }

    // for manipulating stack info
    public void pushInfo(Expression info) {
        mProvidedInfo.push(info);
    }
    public Expression popInfo() {
        if (mProvidedInfo.empty())
            return null;

        return mProvidedInfo.pop();
    }
}
