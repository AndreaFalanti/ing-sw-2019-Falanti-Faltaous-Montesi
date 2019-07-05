package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Game;

import static it.polimi.se2019.controller.weapon.ShootInteraction.move;

/**
 * Behaviour that evaluates to nothing and moves the specified targets in the specified position
 */
public class Move extends Behaviour {
    // required for Gson; should never be called by the user
    public Move() {

    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @param from targets to move
     * @param to position to move targets to
     */
    public Move(Expression from, Expression to) {
        putSub("from", from);
        putSub("to", to);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        Game game = context.getGame();

        move(
                game,
                getSub("from").eval(context).asTargets(),
                getSub("to").eval(context).asPosition()
        );

        return new Done();
    }
}
