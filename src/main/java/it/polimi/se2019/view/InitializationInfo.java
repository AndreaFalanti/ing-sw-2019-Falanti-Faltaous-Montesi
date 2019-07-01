package it.polimi.se2019.view;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.Board;

import java.util.List;

public class InitializationInfo {
    private PlayerColor mOwnerColor;
    private Board mBoard;
    private List<Player> mPlayers;
    private int mTurnNumber;
    private int mSkullNum;
    private List<PlayerColor> mKills;
    private List<PlayerColor> mOverkills;
    private boolean mFinalFrenzy;
    private PlayerColor mActivePlayerColor;
    private int mRemainingActions;

    public Board getBoard() {
        return mBoard;
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }

    public int getTurnNumber() {
        return mTurnNumber;
    }

    public int getSkullNum() {
        return mSkullNum;
    }

    public List<PlayerColor> getKills() {
        return mKills;
    }

    public List<PlayerColor> getOverkills() {
        return mOverkills;
    }

    public boolean isFinalFrenzy() {
        return mFinalFrenzy;
    }

    public PlayerColor getActivePlayerColor() {
        return mActivePlayerColor;
    }

    public int getRemainingActions() {
        return mRemainingActions;
    }

    public PlayerColor getOwnerColor() {
        return mOwnerColor;
    }

    public void setOwnerColor(PlayerColor ownerColor) {
        mOwnerColor = ownerColor;
    }

    public InitializationInfo(Game game, PlayerColor ownerColor) {
        mBoard = game.getBoard();
        mPlayers = game.getPlayers();
        mTurnNumber = game.getTurnNumber();
        mSkullNum = game.getSkullNum();
        mKills = game.getKills();
        mOverkills = game.getOverkills();
        mFinalFrenzy = game.isFinalFrenzy();
        mActivePlayerColor = game.getActivePlayer().getColor();
        mRemainingActions = game.getRemainingActions();
        mOwnerColor = ownerColor;
    }
}
