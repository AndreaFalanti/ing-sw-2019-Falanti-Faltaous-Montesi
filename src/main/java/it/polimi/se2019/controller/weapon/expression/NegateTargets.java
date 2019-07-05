package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Behaviour that negates the selection of a given set of targets
 */
public class NegateTargets extends Behaviour {
    // required for Gson; should never be called by the user
    public NegateTargets() {

    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @param selection the set of targets to negate
     */
    public NegateTargets(Expression selection) {
        putSub("selection", selection);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        Set<PlayerColor> targets = getSub("selection").eval(context).asTargets();

        Set<PlayerColor> negatedTargets = context.getPlayers().stream()
                .map(Player::getColor)
                .filter(plColor -> !targets.contains(plColor))
                .collect(Collectors.toSet());

        return new SetExpression(negatedTargets.stream()
                .map(TargetLiteral::new)
                .collect(Collectors.toSet())
        );
    }
}
