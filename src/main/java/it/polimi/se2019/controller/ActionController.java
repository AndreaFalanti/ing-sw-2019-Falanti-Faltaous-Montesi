package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.util.Observer;

public class ActionController implements Observer<Action> {
    Game mGame;

    @Override
    public void update(Action action) {
        action.perform(mGame);
    }
}
