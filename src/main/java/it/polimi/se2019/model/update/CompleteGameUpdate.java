package it.polimi.se2019.model.update;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.Board;

import java.util.ArrayList;
import java.util.List;

public class CompleteGameUpdate {
    private Board mBoard;
    private List<Player> mPlayers;
    private int mTurnNumber;
    private List<PlayerColor> mKills = new ArrayList<>();
    private List<PlayerColor> mOverkills = new ArrayList<>();
    private boolean mFinalFrenzy;
    private int mRemainingActions;
    private PlayerColor mActivePlayer;
}
