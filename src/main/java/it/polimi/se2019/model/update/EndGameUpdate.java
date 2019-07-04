package it.polimi.se2019.model.update;

import it.polimi.se2019.model.PlayerColor;

import java.util.SortedMap;

public class EndGameUpdate implements Update {
    private SortedMap<PlayerColor, Integer> mLeaderboard;

    public EndGameUpdate(SortedMap<PlayerColor, Integer> leaderboard) {
        mLeaderboard = leaderboard;
    }

    public SortedMap<PlayerColor, Integer> getLeaderboard() {
        return mLeaderboard;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
