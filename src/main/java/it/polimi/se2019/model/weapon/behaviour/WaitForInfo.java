package it.polimi.se2019.model.weapon.behaviour;

public class WaitForInfo extends AtomicExpression {
    private AtomicExpression mAwaitedInfo; // TODO: use this to check if acquired info is rightly formed


    public WaitForInfo() {}

    public WaitForInfo(AtomicExpression awaitedInfo) {
        super();

        mAwaitedInfo = awaitedInfo;
    }

    // TODO: add doc
    @Override
    protected AtomicExpression continueEval(ShootContext shootContext) {
        // if info is available, consume it and substitute this expression with it
        if (shootContext.peekProvidedInfo().isPresent()) {
            // TODO: check if this is correct
            return shootContext.popProvidedInfo().eval(shootContext);
        }

        // if info is not available, just keep waiting
        return this;
    }
}

