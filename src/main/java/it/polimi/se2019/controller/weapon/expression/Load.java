package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

/**
 * Behaviour that complements the {@code Store} behaviour: it accepts a StringLiteral subexpressions and evaluates to
 * the value that was previously associated with that StringLiteral by a previous evaluation of a {@code Store}
 * behaviour
 * @author Stefano Montesi
 */
public class Load extends Behaviour {
    // required for Gson; should never be called by the user
    public Load() {

    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @param name name of the desired subexpression to retrieve from the scope
     */
    public Load(Expression name) {
        putSub("name", name);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        return context.getVar(getSub("name").eval(context).asString());
    }
}
