package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.controller.response.Response;

public class WaitForInfo extends Expression {
    private @SubExpression Expression mAwaitedInfo; // TODO: use this to check if acquired info is rightly formed

    public WaitForInfo(Expression awaitedInfo) {
        super();

        mAwaitedInfo = awaitedInfo;
    }

    // TODO: add doc
    @Override
    protected Expression continueEval(ShootContext shootContext) {
        // if info is available, consume it and substitute this expression with it
        if (shootContext.peekProvidedInfo().isPresent()) {
            // TODO: check if this is correct
            return shootContext.consumeProvidedInfo();
        }

        // if info is not available, just keep waiting
        return this;
    }
}

