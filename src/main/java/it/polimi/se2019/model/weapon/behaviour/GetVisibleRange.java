package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.weapon.Expression;
import it.polimi.se2019.model.weapon.ShootContext;

public class GetVisibleRange extends Behaviour {
    public GetVisibleRange() {
        putSub("observer", new You());
    }

    public GetVisibleRange(Expression observer) {
        putSub("observer", observer);
    }

    @Override
    protected Expression handleSubDefaultValue(String subName, ShootContext context) {
        if (subName.equals("observer")) {
            return new You();
        }

        return super.handleSubDefaultValue(subName, context);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        // TODO: make this better by streamlining selections (todo file)
        Position observerPos = getSub("observer").asRange().iterator().next();

        return new RangeLiteral(
                context.getBoard().getAllSeenBy(observerPos)
        );
    }
}
