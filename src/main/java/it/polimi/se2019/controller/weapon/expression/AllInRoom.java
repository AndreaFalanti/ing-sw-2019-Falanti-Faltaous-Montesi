package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.TileColor;

import java.util.Set;
import java.util.stream.Collectors;

public class AllInRoom extends Behaviour {
    public AllInRoom() {

    }

    public AllInRoom(Expression color) {
        putSub("color", color);
    }

    @Override
    public Expression eval(ShootContext context) {
        Board board = context.getBoard();
        Set<Player> players = context.getPlayers();

        TileColor roomColor = getSub("color").eval(context).asColor();
        Set<Expression> playersInRoom = board.getRoom(roomColor)
                .flatMap(pos -> players.stream().filter(pl -> pl.getPos().equals(pos)))
                .map(Player::getColor)
                .map(TargetLiteral::new)
                .map(l -> (Expression) l)
                .collect(Collectors.toSet());

        return new SetExpression(playersInRoom);
    }
}
