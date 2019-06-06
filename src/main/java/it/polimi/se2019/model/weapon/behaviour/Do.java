package it.polimi.se2019.model.weapon.behaviour;

import com.google.gson.annotations.SerializedName;
import it.polimi.se2019.model.weapon.Expression;
import it.polimi.se2019.model.weapon.ShootContext;

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
