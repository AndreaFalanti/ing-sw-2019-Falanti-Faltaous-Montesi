package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

import java.util.Set;
import java.util.stream.Collectors;

public class GetTargets extends Behaviour {
    public GetTargets(Expression positions) {
        super();

        putSub("from", positions);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        Set<Player> players = context.getPlayers();
        Set<Position> positions = getSub("from").asRange();

        // get all players standing in provided range and are not the shooter
        Set<PlayerColor> selectedPlayers = players.stream()
                .filter(pl -> positions.contains(pl.getPos()))
                .filter(pl -> pl.getColor() != context.getShooterColor())
                .map(Player::getColor)
                .collect(Collectors.toSet());

        return new TargetsLiteral(selectedPlayers);
    }
}
