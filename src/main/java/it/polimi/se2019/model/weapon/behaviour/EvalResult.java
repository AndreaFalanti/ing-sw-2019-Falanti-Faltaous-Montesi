package it.polimi.se2019.model.weapon.behaviour;

/**
 * Simple utility class to contain the result of eval
 */
// TODO: make eval interface better, without the need for this
class EvalResult {
    EvalResult(boolean shouldStopEval, AtomicExpression evaluatedExpression) {
        this.shouldStopEval = shouldStopEval;
        this.evaluatedExpression = evaluatedExpression;
    }

    boolean shouldStopEval = false;
    AtomicExpression evaluatedExpression = null;
}

