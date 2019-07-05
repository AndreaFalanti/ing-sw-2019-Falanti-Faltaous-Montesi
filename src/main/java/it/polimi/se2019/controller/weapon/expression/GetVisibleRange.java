package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Position;

import java.util.stream.Collectors;

/**
 * Behaviour that evaluates to the range visible by an hypothetical observer standing on
 * at a specified position (store in the {@code origin} subexpression)
 */
public class GetVisibleRange extends Behaviour {
    private static final String ORIGIN = "origin";

    // required for Gson; should never be called by the user
    public GetVisibleRange() {
        putSub(ORIGIN, new Pos(new You()));
    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @param origin position on which the observer stands
     */
    public GetVisibleRange(Expression origin) {
        putSub(ORIGIN, origin);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        Position observerPos = getSub(ORIGIN).eval(context).asPosition();

        return new SetExpression(
                context.getBoard().getAllSeenBy(observerPos).stream()
                        .map(PositionLiteral::new)
                        .collect(Collectors.toSet())
        );
    }
}
