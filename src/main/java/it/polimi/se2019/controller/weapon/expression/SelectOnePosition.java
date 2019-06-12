package it.polimi.se2019.controller.weapon.expression;

import com.google.gson.annotations.SerializedName;
import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.view.View;

import java.util.Collections;

public class SelectOnePosition extends Behaviour {
    public SelectOnePosition() {

    }

    public SelectOnePosition(Expression from) {
        putSub("from", from);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        View view = context.getView();

        Position selectedPosition = view.selectPosition(getSub("from").asRange());

        return new PositionLiteral(selectedPosition);
    }
}
