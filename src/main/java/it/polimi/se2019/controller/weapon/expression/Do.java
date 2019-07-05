package it.polimi.se2019.controller.weapon.expression;

import com.google.gson.annotations.SerializedName;
import it.polimi.se2019.controller.weapon.ShootContext;

import java.util.List;

/**
 * Expression that executes all its subexpressions in order, discarding the results
 * @author Stefano Montesi
 */
public class Do extends Expression {
    @SerializedName("list")
    private List<Expression> mSubexpressions;

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        mSubexpressions.forEach(sub -> discardEvalResult(sub.eval(context)));

        return new Done();
    }
}
