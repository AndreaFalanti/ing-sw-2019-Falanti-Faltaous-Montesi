package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.weapon.Selection;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TargetsLiteral extends Literal<Selection<PlayerColor>> {
    public TargetsLiteral(Selection<PlayerColor> contents) {
        super(contents);
    }

    @Override
    Selection<PlayerColor> asTargetSelection() {
        return getPrimitive();
    }

    @Override
    public Expression continueEval(ShootContext context) {
        Stream<PlayerColor> playerColors = context.getPlayers().stream()
                .map(pl -> pl.getColor());
        mContents.setDomain(playerColors);

        return this;
    }
}
