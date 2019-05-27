package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.response.InvalidActionResponse;

import java.util.Optional;
import java.util.Set;

public class DamageAction implements Action {
    // fields
    PlayerColor mAttackerColor;
    Set<PlayerColor> mDefenderColors;
    Damage mDamageToInflict;

    // trivial constructor
    public DamageAction(PlayerColor attackerColor, Set<PlayerColor> defenderColors, Damage damageToInflict) {
        mAttackerColor = attackerColor;
        mDefenderColors = defenderColors;
        mDamageToInflict = damageToInflict;
    }

    // TODO: add doc
    @Override
    public void perform(Game game) {
        mDefenderColors.stream()
                .map(defenderColor -> game.getPlayerFromColor(defenderColor))
                .forEach(defendingPlayer -> {
                    defendingPlayer.onDamageTaken(mDamageToInflict, mAttackerColor);
                });
    }

    // TODO: add doc
    @Override
    public Optional<InvalidActionResponse> getErrorResponse(Game game) {
        return Optional.empty();
    }

    @Override
    public boolean consumeAction() {
        return false;
    }
}
