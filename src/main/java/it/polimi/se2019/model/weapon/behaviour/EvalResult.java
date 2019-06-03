package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.Expression;

/**
 * Simple utility class to contain the result of eval
 */
// TODO: make eval interface better, without the need for this
class EvalResult {
    EvalResult(boolean shouldStopEval, Expression evaluatedExpression) {
        this.shouldStopEval = shouldStopEval;
        this.evaluatedExpression = evaluatedExpression;
    }

    boolean shouldStopEval = false;
    Expression evaluatedExpression = null;
}

