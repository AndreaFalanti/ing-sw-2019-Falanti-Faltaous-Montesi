package it.polimi.se2019.controller.weapon.behaviour;

public class StringLiteral extends Literal<String> {
    public StringLiteral(String contents) {
        super(contents);
    }

    @Override
    public String asString() {
        return getPrimitive();
    }
}
