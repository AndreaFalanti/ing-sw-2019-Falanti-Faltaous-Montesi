package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.Direction;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Line extends Behaviour {
    public Line() {
        putSub("origin", new Pos(new You()));
        putSub("minLength", new IntLiteral(0));
        putSub("maxLength", new InfLiteral());
    }

    private static Position directionalIncrement(Position position, Direction direction, int amount) {
        switch (direction) {
            case NORTH:
                return position.add(new Position(0, -amount));
            case WEST:
                return position.add(new Position(amount, 0));
            case SOUTH:
                return position.add(new Position(0, amount));
            case EAST:
                return position.add(new Position(-amount, 0));
            default:
                throw new UnsupportedOperationException("Unknown direction");
        }
    }
    private static Position directionalIncrement(Position position, Direction direction) {
        return directionalIncrement(position, direction, 1);
    }

    @Override
    public final Expression eval(ShootContext context) {
        Board board = context.getBoard();

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

        // stretch line as far as it can go
        final Set<Position> resultSet = new HashSet<>();
        for (Position pos = directionalIncrement(origin, direction, minLength);
             maxLength > 0 && !board.isOutOfBounds(pos);
             pos = directionalIncrement(pos, direction), --maxLength)
            resultSet.add(pos);

        // encapsulate result and return it
        return new SetExpression(
                resultSet.stream()
                        .map(PositionLiteral::new)
                        .collect(Collectors.toSet())
        );
    }
}
