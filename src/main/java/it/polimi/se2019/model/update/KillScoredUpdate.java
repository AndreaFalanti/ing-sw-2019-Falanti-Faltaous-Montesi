package it.polimi.se2019.model.update;

import it.polimi.se2019.model.PlayerColor;

import java.util.Map;

/**
 * Update message with various info related to killtrack and scores, sent to views
 *
 * @author Andrea Falanti
 */
public class KillScoredUpdate implements Update {
    private PlayerColor mPlayerKilledColor;
    private PlayerColor mKillerColor;
    private boolean mOverkill;
    private Map<PlayerColor, Integer> mScores;  //formerly an EnumMap

    public KillScoredUpdate(PlayerColor playerKilledColor, PlayerColor killerColor,
                            boolean overkill, Map<PlayerColor, Integer> scores) {
        mPlayerKilledColor = playerKilledColor;
        mKillerColor = killerColor;
        mOverkill = overkill;
        mScores = scores;
    }

    public PlayerColor getPlayerKilledColor() {
        return mPlayerKilledColor;
    }

    public PlayerColor getKillerColor() {
        return mKillerColor;
    }

    public boolean isOverkill() {
        return mOverkill;
    }

    public Map<PlayerColor, Integer> getScores() {
        return mScores;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
