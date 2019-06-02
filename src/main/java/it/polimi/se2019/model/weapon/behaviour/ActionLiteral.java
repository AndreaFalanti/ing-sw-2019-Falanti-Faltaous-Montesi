package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.action.Action;

public class ActionLiteral extends Literal<Action> {
    // construct from primitive
    public ActionLiteral(Action contents) {
        super(contents);
    }

    // conversion to primitive
    @Override
    public Action asAction() {
        return getPrimitive();
    }
}
