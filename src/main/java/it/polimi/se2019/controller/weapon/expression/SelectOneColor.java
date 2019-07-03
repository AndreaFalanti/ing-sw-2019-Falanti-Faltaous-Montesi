package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.view.View;

import java.util.stream.Collectors;

public class SelectOneColor extends Behaviour {
    public SelectOneColor() {
    }

    @Override
    public Expression eval(ShootContext context) {
        View view = context.getView();
        ShootInteraction interaction = context.getInteraction();

        Expression from = getSub("from").eval(context);

        TileColor color = interaction.pickRoomColor(
                view,
                from.asColors()
        );

        return new ColorLiteral(color);
    }
}
