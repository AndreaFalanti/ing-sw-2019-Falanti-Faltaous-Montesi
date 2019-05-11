package it.polimi.se2019.model.action;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.NotEnoughAmmoException;

public class PayAction implements Action {
    AmmoValue mCost;

    // TODO: add doc
    public PayAction(AmmoValue cost) {
        mCost = cost;
    }

    // TODO: maybe Exception is unneeded?
    @Override
    public void perform(Game game) throws NotEnoughAmmoException {
        // TODO: maybe implement Player.pay?
        AmmoValue result = game.getActivePlayer().getAmmo();
        result.subtract(mCost);
        game.getActivePlayer().setAmmo(result);
    }

    // TODO: implement
    @Override
    public boolean isValid(Game game) {
        throw new UnsupportedOperationException("NOT IMPLEMENTED!");
    }

    // TODO: implement
    @Override
    public ResponseCode getCode() {
        throw new UnsupportedOperationException("NOT IMPLEMENTED!");
    }
}
