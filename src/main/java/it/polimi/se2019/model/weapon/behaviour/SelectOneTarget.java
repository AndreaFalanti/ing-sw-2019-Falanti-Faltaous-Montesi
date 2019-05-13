package it.polimi.se2019.model.weapon.behaviour;

public class SelectOneTarget extends SelectTargets {
    public SelectOneTarget(Expression targetsToSelectFrom) {
        super(new IntLiteral(1), new IntLiteral(1), targetsToSelectFrom);
    }
}
