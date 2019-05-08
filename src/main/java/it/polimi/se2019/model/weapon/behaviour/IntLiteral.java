package it.polimi.se2019.model.weapon.behaviour;

public class IntLiteral extends Literal<Integer> {
    public IntLiteral(Integer contents) {
        super(contents);
    }

    @Override
    int asInt() {
        return getPrimitive();
    }
}
