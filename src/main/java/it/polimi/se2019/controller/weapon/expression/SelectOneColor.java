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
        Board board = context.getBoard();
        ShootInteraction interaction = context.getInteraction();
        Player shooter = context.getShooter();

        TileColor color = interaction.pickRoomColor(
                view,
                board.getRoomColors()
                        .filter(clr -> board.getRoom(clr)
                                .noneMatch(pos -> shooter.getPos().equals(pos)))
                        .collect(Collectors.toSet())
        );

        return new ColorLiteral(color);
    }
}
