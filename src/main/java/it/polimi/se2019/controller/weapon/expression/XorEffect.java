package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.view.View;

public class XorEffect extends Expression {
    public XorEffect() {

    }

    private Effect mLhs;
    private Effect mRhs;

    @Override
    public final Expression eval(ShootContext context) {
        ShootInteraction interaction = context.getInteraction();
        View view = context.getView();

        // get input
        String selectedEffectID = interaction.selectWeaponMode(view, mLhs, mRhs);

        // TODO: check if input needs to be validated

        // use input to decide which effect should be chosen
        if (mLhs.getId().equals(selectedEffectID))
            discardEvalResult(mLhs.getBehaviour().eval(context));
        else
            discardEvalResult(mRhs.getBehaviour().eval(context));

        return new Done();
    }
}
