package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Direction;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Behaviour evaluating to a range shaped like a line segment starting at a specified origin and
 * ending after a specified length going in a specified direction. A "minimum length" can also be provided
 * to offset the beginning of the line by a certain amount.
 * @author Stefano Montesi
 */
public class Line extends Behaviour {
    // required for Gson; should never be called by the user
    public Line() {
        putSub("origin", new Pos(new You()));
        putSub("minLength", new IntLiteral(0));
        putSub("maxLength", new InfLiteral());
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        Position origin = getSub("origin").eval(context).asPosition();
        Direction direction = getSub("direction").eval(context).asDirection();

        // be careful to only evaluate a subexpr once
        Expression evaluatedMaxLengthExpr = getSub("maxLength").eval(context);

        // get max and min length as normal ints
        int minLength = getSub("minLength").eval(context).asInt();
        int maxLength = evaluatedMaxLengthExpr.isInf() ?
                5 : // there cannot be a line with a length greater than 5
                evaluatedMaxLengthExpr.asInt();

        if (minLength > maxLength)
            throw new IllegalArgumentException("Trying to create line with minLength > maxLength");

        final Set<Position> resultSet = IntStream.rangeClosed(minLength, maxLength)
                .mapToObj(len -> origin.directionalIncrement(direction, len))
                .collect(Collectors.toSet());

        // encapsulate result and return it
        return new SetExpression(
                resultSet.stream()
                        .map(PositionLiteral::new)
                        .collect(Collectors.toSet())
        );
    }
}
