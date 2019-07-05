package it.polimi.se2019.controller.weapon.expression;

/**
 * Literal representing an integral value
 * @author Stefano Montesi
 */
public class IntLiteral extends Literal<Integer> {
    public IntLiteral(Integer contents) {
        super(contents);
    }

    /**
     * @return the expression as an integer
     */
    @Override
    public int asInt() {
        return getPrimitive();
    }
}
