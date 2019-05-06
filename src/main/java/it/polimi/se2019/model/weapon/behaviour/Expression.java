package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.request.Request;

import java.util.*;

public abstract class Expression {
    final List<Expression> mSubexpressions = new ArrayList<>();

    // TODO: add doc
    protected Expression(Expression... subExpressions) {
        mSubexpressions.addAll(Arrays.asList(subExpressions));
    }

    // TODO: add doc
    public final Expression eval(ShootContext shootContext) {
        // first evaluate all subexpressions
        boolean infoIsMissing = false;
        for (Expression subexpr : mSubexpressions) {
            subexpr = subexpr.eval(shootContext);
            if (subexpr instanceof MissingInfo)
                infoIsMissing = true;
        }

        // if info is missing, return the expression partially evaluated
        if (infoIsMissing)
            return this;
        // else use info acquired from complete subexpressions to finish evaluation of
        // this expression
        else
            return continueEval(shootContext);
    }

    // TODO: add doc
    public abstract Expression continueEval(ShootContext shootContext);

    // TODO: add doc and refine error messages
    int asInt() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    Set<PlayerColor> asTargets() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    Set<Position> asRange() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    Damage asDamage() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    Action asAction() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    Request asRequest() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
}
