package it.polimi.se2019.model.weapon.behaviour;

import java.util.Optional;

public class CollectInfo extends Expression {
    private @SubExpression Expression mInfo;

    public CollectInfo(Expression info) {
        super();

        mInfo  = info;
    }

    @Override
    protected Expression continueEval(ShootContext shootContext) {
        Optional<Expression> maybeInfo = shootContext.popCollectedInfo();

        // TODO: check if collected info satisfies requested instead of ignoring it
        // if (maybeInfo.isPresent()) {
            // Expression collectedInfo = maybeInfo.get();
            //
            // if (collectedInfo.satisfies(mInfo.asRequest()));
        // }

        return maybeInfo.orElse(new WaitForInfo());
    }
}
