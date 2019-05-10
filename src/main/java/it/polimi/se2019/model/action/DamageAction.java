package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.weapon.Selection;

import java.util.Set;

public class DamageAction implements Action {
    // fields
    PlayerColor mAttackerColor;
    Set<PlayerColor> mDefenderColors;
    Damage mDamageToInflict;
    private ResponseCode mCode;
    private String message;

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
                    defendingPlayer.sufferedDamage(mAttackerColor, mDamageToInflict.getDamage());
                    defendingPlayer.sufferedMarks (mAttackerColor, mDamageToInflict.getMarksNum());
                });
    }

    // TODO: add doc
    @Override
    public boolean isValid(Game game) {
        // TODO: see if this is correct according to game rules
        // return isValidPlayerColor(mAttackerColor) &&
                // isValidPlayerColor(mDefenderColor);
        return true;
    }

    public ResponseCode getCode(){return mCode;}
}
