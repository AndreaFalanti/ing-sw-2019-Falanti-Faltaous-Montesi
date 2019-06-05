package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;

public class EndTurn extends Controller{
    private Game mGame;

    public EndTurn(Game game) {
        super(game);
    }

    public void endingTurn() {
        mGame.onTurnEnd();
    }

    public void beginningTurn(){
        mGame.startNextTurn();
    }

}
