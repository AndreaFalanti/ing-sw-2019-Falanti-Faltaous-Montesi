package it.polimi.se2019.controller.weapon.behaviour;

import com.google.gson.annotations.SerializedName;
import it.polimi.se2019.controller.weapon.Expression;
import it.polimi.se2019.controller.weapon.ShootContext;

import java.util.List;

public class Do extends Expression {
    @SerializedName("list")
    List<Expression> mSubexpressions;

    @Override
    public Expression eval(ShootContext context) {
        // TODO: use logger
        mSubexpressions.forEach(sub -> discardEvalResult(sub.eval(context)));

        return new Done();
    }
}
