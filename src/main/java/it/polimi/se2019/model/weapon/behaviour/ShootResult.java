package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.action.Action;

import java.util.Optional;

public class ShootResult {
    // these two will never be present at the same time
    private Optional<Action> mActionValue;
    private Optional<Response> mResponseValue;

    private ShootResult() {}
    private ShootResult(Action action) {
        mResponseValue = Optional.empty();
        mActionValue = Optional.of(action);
    }
    private ShootResult(Response request) {
        mResponseValue = Optional.of(request);
        mActionValue = Optional.empty();
    }

    public static ShootResult fromAction(Action action) {
        return new ShootResult(action);
    }

    public static ShootResult fromResponse(Response response) {
        return new ShootResult(response);
    }

    public boolean isComplete() {
        if (mResponseValue.isPresent() && mActionValue.isPresent())
            throw new IllegalStateException("Shoot request is both complete and not...");

        return mActionValue.isPresent();
    }

    public Action fromAction() {
        if (!isComplete())
            throw new UnsupportedOperationException("Cannot interpret incomplete shoot result as an action!");

        return mActionValue.get();
    }

    public Response fromResponse() {
         if (isComplete())
            throw new UnsupportedOperationException("Cannot interpret complete shoot result as a response!");

        return mResponseValue.get();
    }
}
