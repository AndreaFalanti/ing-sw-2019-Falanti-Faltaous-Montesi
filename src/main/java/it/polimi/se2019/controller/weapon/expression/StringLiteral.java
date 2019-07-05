package it.polimi.se2019.controller.weapon.expression;

/**
 * Literal containing a string value
 * @author Stefano Montesi
 */
public class StringLiteral extends Literal<String> {
    public StringLiteral(String contents) {
        super(contents);
    }

    /**
     * @return this expression as a string
     */
    @Override
    public String asString() {
        return getPrimitive();
    }
}
