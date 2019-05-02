package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;

public class PlayerColorLiteral implements Expression {
    PlayerColor mContents;

    @Override
    public Expression eval(Context context) {
        return this;
    }

    // TODO: find a way to handle conversion to non-literals properly (prototype)
    public PlayerColor getAsPlayerColor() {
        return mContents;
    }
}
