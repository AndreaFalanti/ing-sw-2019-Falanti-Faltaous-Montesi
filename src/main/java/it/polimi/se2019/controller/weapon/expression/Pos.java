package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import org.w3c.dom.ranges.Range;

import java.util.Collections;
import java.util.Set;

public class Pos extends Behaviour {
    public Pos() {

    }

    public Pos(Expression who) {
        putSub("who", who);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        Game game = context.getGame();

        return new RangeLiteral(Collections.singleton(
                game.getPlayerFromColor(getSub("who").asTarget()).getPos()
        ));
    }
}
