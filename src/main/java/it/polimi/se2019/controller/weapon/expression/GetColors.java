package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.Tile;

import java.util.stream.Collectors;

/**
 * Behaviour for retrieving the colors of the tiles from a given range
 * @author Stefano Montesi
 */
public class GetColors extends Behaviour {
    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public Expression eval(ShootContext context) {
        Board board = context.getBoard();

        Expression from = getSub("from").eval(context);

        return new SetExpression(
                from.asRange().stream()
                        .map(board::getTileAt)
                        .map(Tile::getColor)
                        .map(ColorLiteral::new)
                        .collect(Collectors.toSet())
        );
    }
}
