package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.view.View;

public class SelectOneDirection extends Behaviour {
    @Override
    public final Expression eval(ShootContext context) {
        ShootInteraction interaction = context.getShootInteraction();
        View view = context.getView();

        return new DirectionLiteral(interaction.pickDirection(view));
    }
}
