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

        // set inputs as optionals so that view does not force the user to pick both of them
        mLhs.setOptional(true);
        mRhs.setOptional(true);

        // get input
        String selectedEffectID = interaction.selectWeaponMode(view, mLhs, mRhs);
        Effect selectedEffect = selectedEffectID.equals(mLhs.getId()) ? mLhs : mRhs;

        // pay and execute selected effect
        interaction.manageAmmoPayment(
                view.getOwnerColor(),
                selectedEffect.getCost(),
                selectedEffect.getName(),
                () -> eval(context)
        );
        selectedEffect.getBehaviour().eval(context);

        return new Done();
    }
}
