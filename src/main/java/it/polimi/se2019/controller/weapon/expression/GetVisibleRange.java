package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Position;

import java.util.stream.Collectors;

public class GetVisibleRange extends Behaviour {
    private static final String ORIGIN = "origin";
    
    public GetVisibleRange() {
        putSub(ORIGIN, new Pos(new You()));
    }

    public GetVisibleRange(Expression origin) {
        putSub(ORIGIN, origin);
    }

    @Override
    public final Expression eval(ShootContext context) {
        // TODO: make this better by streamlining selections (todo file)
        Position observerPos = getSub(ORIGIN).eval(context).asPosition();

        return new SetExpression(
                context.getBoard().getAllSeenBy(observerPos).stream()
                        .map(PositionLiteral::new)
                        .collect(Collectors.toSet())
        );
    }
}
