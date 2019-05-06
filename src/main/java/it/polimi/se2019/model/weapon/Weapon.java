package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.behaviour.ShootContext;
import it.polimi.se2019.model.weapon.behaviour.Expression;
import it.polimi.se2019.model.weapon.request.Request;


public class Weapon {
    // behaviour of weapon used to shoot
    Expression mBehaviour;

    // trivial constructor
    public Weapon(Expression behaviour) {
        mBehaviour = behaviour;
    }

    // TODO: add doc
    public Request generateShootRequest(ShootContext shootContext) {
        return mBehaviour.eval(shootContext).asRequest().orElseThrow(() -> new IllegalArgumentException(
                "Trying to generate shoot request with a complete shootContext!"
        ));
    }

    // TODO: add doc
    public Action generateShootAction(ShootContext shootContext) {
        return mBehaviour.eval(shootContext).asAction().orElseThrow(() -> new IllegalArgumentException(
                "Trying to generate shoot action with incomplete shootContext!"
        ));
    }
}