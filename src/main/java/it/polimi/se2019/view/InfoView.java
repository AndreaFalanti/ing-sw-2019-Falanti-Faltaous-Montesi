package it.polimi.se2019.view;

import it.polimi.se2019.model.PlayerColor;

import java.util.ArrayList;
import java.util.List;

public class InfoView {
    private PlayerColor mActivePlayer;
    private int mTurnNumber;
    private ArrayList<PlayerColor> mDeaths;

    public int getTurnNumber(){
        return mTurnNumber;
    }

    public PlayerColor getActivePlayer(){
        return mActivePlayer;
    }

    public List<PlayerColor> getDeaths(){
        return mDeaths;
    }

}
