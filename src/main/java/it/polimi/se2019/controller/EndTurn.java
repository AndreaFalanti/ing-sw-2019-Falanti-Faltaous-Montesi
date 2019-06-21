package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;

import java.util.HashMap;

public class EndTurn extends Controller{
    private Game mGame;

    public EndTurn(Game game) {
        // TODO: link real view map
        super(game, new HashMap<>());
    }

    public void endingTurn() {
        mGame.onTurnEnd();
    }

    public void beginningTurn(){
        mGame.startNextTurn();
    }

}
