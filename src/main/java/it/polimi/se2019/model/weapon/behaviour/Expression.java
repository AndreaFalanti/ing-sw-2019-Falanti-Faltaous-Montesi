package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.request.Request;

import java.util.Optional;
import java.util.Set;

public interface Expression {
    Expression eval(Context context);

    default int asInt() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    default Optional<Set<PlayerColor>> asTargets() {
        return Optional.empty();
    }
    default Optional<Set<Position>> asRange() {
        return Optional.empty();
    }
    default Optional<Damage> asDamage() {
        return Optional.empty();
    }
    default Optional<Action> asAction() {
        return Optional.empty();
    }
    default Optional<Request> asRequest() {
        return Optional.empty();
    }
}
