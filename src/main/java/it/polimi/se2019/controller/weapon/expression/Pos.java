package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Game;

/**
 * Expression that evaluates to the position of the specified (through subexpression) player.
 */
public class Pos extends Behaviour {
    // required for Gson; should never be called by the user
    public Pos() {

    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @param who player of whom the position is of importance
     */
    public Pos(Expression who) {
        putSub("who", who);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        Game game = context.getGame();

        return new PositionLiteral(
                game.getPlayerFromColor(getSub("who").eval(context).asTarget()).getPos()
        );
    }
}
