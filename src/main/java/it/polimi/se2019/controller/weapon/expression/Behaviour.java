package it.polimi.se2019.controller.weapon.expression;

import com.google.gson.annotations.SerializedName;
import it.polimi.se2019.controller.weapon.ShootContext;

import java.util.HashMap;
import java.util.Map;

public abstract class Behaviour extends Expression {
    // subexpressions evaluated before their parent expression and used in its evaluation
    @SerializedName("subs")
    Map<String, Expression> mSubexpressions;

    // default constructor to fill in default values during deserialization
    public Behaviour() {
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
        mSubexpressions.put(name, subexpr);
        return this;
    }

    // TODO: add doc
    public Expression putSubs(Map<String, Expression> newSubs) {
        mSubexpressions.putAll(newSubs);
        return this;
    }

    // TODO: add doc
    @Override
    public final Expression eval(ShootContext shootContext) {
        mSubexpressions.values()
                .forEach(sub -> sub.eval(shootContext));

        return continueEval(shootContext);
    }

    // TODO: add doc
    protected Expression handleSubDefaultValue(String subName, ShootContext context) {
        throw new UnsupportedOperationException(
                subName + " was not set in " + getClass().getSimpleName() + " expression and" +
                        "has no assigned defaults."
        );
    }

    // TODO: add doc
    protected abstract Expression continueEval(ShootContext context);
}
