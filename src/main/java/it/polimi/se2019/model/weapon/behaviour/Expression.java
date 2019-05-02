package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.DamageAction;

import java.util.Set;

public interface Expression {
    Expression eval(Context context);

    default int asInt() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    default Set<PlayerColor> asTargets() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    default Set<Position> asRange() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    default Damage asDamage() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    default DamageAction asDamageAction() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
}
