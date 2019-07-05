package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

/**
 * Stores the result of its subexpression into the given scope.
 * @author Stefano Montesi
 */
public class Store extends Behaviour {
    /**
     * Constructs the behaviour with using the given subexpressions
     * @param name the name of the variable in which to store {@code value}
     * @param value the subexpression to store inside {@code name} when {@code this} is evaluated
     */
    public Store(Expression name, Expression value) {
        super();

        putSub("name", name);
        putSub("value", value);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        Expression evaluatedValue = getSub("value").eval(context);

        context.setVar(
                getSub("name").eval(context).asString(),
                evaluatedValue
        );

        return evaluatedValue;
    }
}
