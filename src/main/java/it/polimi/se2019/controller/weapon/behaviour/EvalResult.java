package it.polimi.se2019.controller.weapon.behaviour;

import it.polimi.se2019.controller.weapon.Expression;

/**
 * Simple utility class to contain the result of eval
 */
@Deprecated
class EvalResult {
    EvalResult(boolean shouldStopEval, Expression evaluatedExpression) {
        this.shouldStopEval = shouldStopEval;
        this.evaluatedExpression = evaluatedExpression;
    }

    boolean shouldStopEval = false;
    Expression evaluatedExpression = null;
}

