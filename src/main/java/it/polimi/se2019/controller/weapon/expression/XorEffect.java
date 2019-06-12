package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.view.View;

import java.util.*;

public class XorEffect extends Expression {
    public XorEffect() {

    }

    private Effect mLhs;
    private Effect mRhs;

    @Override
    public Expression eval(ShootContext context) {
        View view = context.getView();

        Set<String> selectedEffectIDs = view.selectEffects(
                new TreeMap<>(Collections.singletonMap(
                        0, new HashSet<>(Arrays.asList(mLhs, mRhs))
                )),
                0
        );

        // TODO: verify input

        String selectedEffectID = selectedEffectIDs.iterator().next();
        if (mLhs.getId().equals(selectedEffectID))
            discardEvalResult(mLhs.getBehaviour().eval(context));
        else
            discardEvalResult(mRhs.getBehaviour().eval(context));

        return new Done();
    }
}
