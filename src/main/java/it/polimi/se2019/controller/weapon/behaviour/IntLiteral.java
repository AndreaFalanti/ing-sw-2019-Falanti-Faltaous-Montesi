package it.polimi.se2019.controller.weapon.behaviour;

public class IntLiteral extends Literal<Integer> {
    public IntLiteral(Integer contents) {
        super(contents);
    }

    @Override
    public int asInt() {
        return getPrimitive();
    }
}
