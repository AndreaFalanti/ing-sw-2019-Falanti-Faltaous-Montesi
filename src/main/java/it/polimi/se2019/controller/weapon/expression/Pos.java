package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Game;

public class Pos extends Behaviour {
    public Pos() {

    }

    public Pos(Expression who) {
        putSub("who", who);
    }

    @Override
    public final Expression eval(ShootContext context) {
        Game game = context.getGame();

        return new PositionLiteral(
                game.getPlayerFromColor(getSub("who").eval(context).asTarget()).getPos()
        );
    }
}
