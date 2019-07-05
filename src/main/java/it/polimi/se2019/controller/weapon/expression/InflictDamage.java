package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Behaviour that evaluates to nothing and inflicts damage to all specified players.
 * @author Stefano Montesi
 */
public class InflictDamage extends Behaviour {
    // required for Gson; should never be called by the user
    public InflictDamage() {

    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @param damageToInflict damage inflicted on {@code targets}
     * @param targets targets on which {@code damageToInflict} is inflicted
     */
    public InflictDamage(Expression damageToInflict, Expression targets) {
        putSub("amount", damageToInflict);
        putSub("to", targets);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        PlayerColor inflicter = context.getShooterColor();
        Game game = context.getGame();
        ShootInteraction interaction = context.getInteraction();

        // remove shooter to inflicted players
        Set<PlayerColor> targets = getSub("to").eval(context).asTargets().stream()
                .filter(tar -> !tar.equals(inflicter))
                .collect(Collectors.toSet());

        // inflict damage to specified targets
        interaction.inflictDamage(game, inflicter, targets, getSub("amount").eval(context).asDamage());

        return new Done();
    }
}
