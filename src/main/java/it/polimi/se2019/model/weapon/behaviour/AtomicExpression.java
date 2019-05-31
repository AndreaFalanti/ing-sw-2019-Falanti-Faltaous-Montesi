package it.polimi.se2019.model.weapon.behaviour;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public abstract class AtomicExpression extends Expression {
    // subexpressions evaluated before their parent expression and used in its evaluation
    @SerializedName("subs")
    Map<String, Expression> mSubexpressions;

    // default constructor to fill in default values during deserialization
    public AtomicExpression() {
        mSubexpressions = new HashMap<>();
    }

    // trivial getters
    public Map<String, Expression> getSubexpressions() {
        return mSubexpressions;
    }

    // TODO: add doc
    public Expression getSub(String name) {
        Expression result = mSubexpressions.get(name);

        if (result == null)
            throw new IllegalArgumentException(
                    getClass().getSimpleName() + " has no subexpression named " + name
            );

        return result;
    }

    // TODO: add doc
    public Expression putSub(String name, Expression subexpr) {
        return mSubexpressions.put(name, subexpr);
    }

    // TODO: add doc
    private void evalSubexpressions(ShootContext shootContext) {
        // evaluate subexpressions
        for (Map.Entry<String, Expression> entry : mSubexpressions.entrySet()) {
            String subexprName = entry.getKey();
            Expression subexpr = entry.getValue();

            if (subexpr == null)
                throw new IllegalStateException("FATAL: subexpression was found null during evaluation!");

            Expression evalResult = subexpr.eval(shootContext);

            mSubexpressions.put(subexprName, evalResult);
        }
    }

    // TODO: add doc
    @Override
    public final Expression eval(ShootContext shootContext) {
        // evaluate all registered subexpressions
        evalSubexpressions(shootContext);

        // continue evaluation of expression tree (for now)
        return continueEval(shootContext);
    }

    // TODO: add doc
    protected  abstract Expression continueEval(ShootContext shootContext);

}
