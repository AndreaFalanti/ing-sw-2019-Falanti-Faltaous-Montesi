package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Position;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Expression for chaining two expression ranges
 */
public class Chain extends Behaviour {
    public Chain() {

    }

    public Chain(Expression base, Expression chainFunc) {
        putSub("base", base);
        putSub("chainFunc", chainFunc);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        final Set<Position> baseSet = new HashSet<>(
                getSub("base")
                        .eval(context).asRange()
        );
        final Behaviour chainFunc = (Behaviour) getSub("chainFunc");

        final Set<Position> result = new HashSet<>();
        baseSet.forEach(origin -> {
            chainFunc.putSub("origin", new PositionLiteral(origin));
            result.addAll(chainFunc.eval(context).asRange());
        });

        return new SetExpression(
                result.stream()
                        .map(PositionLiteral::new)
                        .collect(Collectors.toSet())
        );
    }
}

