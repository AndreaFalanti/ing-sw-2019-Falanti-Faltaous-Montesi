package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;

import java.util.Stack;

public class Context {
    Game mGameStatus;
    PlayerColor mShooterColor;
    Stack<Expression> mProvidedInfo;

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
    Expression popInfo() {
        if (mProvidedInfo.empty())
            return null;

        return mProvidedInfo.pop();
    }
}
