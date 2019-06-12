package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.PlayerColor;

import java.util.Set;
import java.util.stream.Collectors;

public class InflictDamage extends Behaviour {
    public InflictDamage() {

    }

    public InflictDamage(Expression damageToInflict, Expression targets) {
        putSub("amount", damageToInflict);
        putSub("to", targets);
    }

    // TODO: add doc
    @Override
    protected final Expression continueEval(ShootContext context) {
        PlayerColor inflicter = context.getShooterColor();

        // remove shooter to inflicted players
        Set<PlayerColor> targets = getSub("to").asTargets().stream()
                .filter(tar -> !tar.equals(inflicter))
                .collect(Collectors.toSet());

        // calculate resulting action
        inflictDamage(context, inflicter, targets, getSub("amount").asDamage());

        // return it
        return new Done();
    }
}
