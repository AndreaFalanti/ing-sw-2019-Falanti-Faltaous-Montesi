package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

/**
 * Literal representing infinity
 * @author Stefano Montesi
 */
public class InfLiteral extends Expression {
    @Override
    public Expression eval(ShootContext context) {
        return this;
    }

    /**
     * @return true if this expression represents infinity
     */
    @Override
    public boolean isInf() {
        return true;
    }
}
