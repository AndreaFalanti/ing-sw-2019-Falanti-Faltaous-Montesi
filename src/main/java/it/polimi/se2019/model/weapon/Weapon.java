package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.ShootAction;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.weapon.behaviour.Context;
import it.polimi.se2019.model.weapon.behaviour.Expression;
import it.polimi.se2019.model.weapon.request.Request;
import it.polimi.se2019.model.weapon.request.SelectionRequest;

import java.util.ArrayList;


public class Weapon {
    // behaviour of weapon used to shoot
    Expression mBehaviour;

    // trivial constructor
    public Weapon(Expression behaviour) {
        mBehaviour = behaviour;
    }

    // TODO: add doc
    public Request generateShootRequest(Context context) {
        return mBehaviour.eval(context).asRequest().orElseThrow(() -> new IllegalArgumentException(
                "Trying to generate shoot request with a complete context!"
        ));
    }

    // TODO: add doc
    public Action generateShootAction(Context context) {
        return mBehaviour.eval(context).asAction().orElseThrow(() -> new IllegalArgumentException(
                "Trying to generate shoot action with incomplete context!"
        ));
    }
}