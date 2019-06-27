package it.polimi.se2019.view;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.Board;
import org.omg.CORBA.INITIALIZE;

import java.util.ArrayList;
import java.util.List;

public class InitializationInfo {
    private Board mBoard;
    private List<Player> mPlayers;
    private int mTurnNumber;
    private int mSkullNum;
    private List<PlayerColor> mKills;
    private List<PlayerColor> mOverkills;
    private boolean mFinalFrenzy;
    private PlayerColor mActivePlayerColor;
    private int mRemainingActions;

    public InitializationInfo(Game game) {
        mBoard = game.getBoard();
        mPlayers = game.getPlayers();
        mTurnNumber = game.getTurnNumber();
        mSkullNum = game.getSkullNum();
        mKills = game.getKills();
        mOverkills = game.getOverkills();
        mFinalFrenzy = game.isFinalFrenzy();
        mActivePlayerColor = mPlayers.get(game.getActivePlayerIndex()).getColor();
        mRemainingActions = game.getRemainingActions();
    }
}
