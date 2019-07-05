package it.polimi.se2019.controller.weapon.expression;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Modified expression that encapsulates access to a hash map of subexpressions
 *
 * @author Stefano Montesi
 */
public abstract class Behaviour extends Expression {
    // subexpressions evaluated before their parent expression and used in its evaluation
    @SerializedName("subs")
    private Map<String, Expression> mSubexpressions;

    // basic constructor
    public Behaviour() {
        mSubexpressions = new HashMap<>();
    }

    /**
     * Retrieve a subexpression
     *
     * @param name name of the subexpression to retrieve
     * @return subexpression coinciding with the specified name
     */
    public Expression getSub(String name) {
        Expression result = mSubexpressions.get(name);

        if (result == null)
            throw new IllegalArgumentException(
                    getClass().getSimpleName() + " has no subexpression named " + name
            );

        return result;
    }

    /**
     * Inserts a subexpression
     *
     * @param name    name of the inserted subexpression (must be unique)
     * @param subexpr subexpression to insert
     * @return {@code this}
     */
    public Expression putSub(String name, Expression subexpr) {
        mSubexpressions.put(name, subexpr);
        return this;
    }
}

