package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.weapon.Selection;

import java.util.stream.Stream;

public class TargetsLiteral extends SelectionLiteral<PlayerColor> {
    public TargetsLiteral(Selection<PlayerColor> contents) {
        super(contents);
    }

    @Override
    public Expression continueEval(ShootContext context) {
        Stream<PlayerColor> playerColors = context.getPlayers().stream()
                .map(Player::getColor);
        mContents.setDomain(playerColors);

        return this;
    }
}
