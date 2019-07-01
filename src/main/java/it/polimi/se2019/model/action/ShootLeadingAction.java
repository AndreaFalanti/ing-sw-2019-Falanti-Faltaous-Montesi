package it.polimi.se2019.model.action;

import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.model.Game;

public interface ShootLeadingAction extends Action {
    public Expression getShotBehaviour (Game game);
}
