package it.polimi.se2019.model.weapon_behaviour;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;

public class Context {
    Game mGameStatus;
    PlayerColor mShooterColor;

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
}
