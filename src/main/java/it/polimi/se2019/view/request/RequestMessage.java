package it.polimi.se2019.view.request;

import it.polimi.se2019.model.action.Action;

import java.util.Optional;

public class RequestMessage {
    // these two will never be present at the same time
    private Optional<Action> mActionValue;
    private Optional<Request> mRequestValue;

    private RequestMessage() {}
    private RequestMessage(Action action) {
        mRequestValue = Optional.empty();
        mActionValue = Optional.of(action);
    }
    private RequestMessage(Request request) {
        mRequestValue = Optional.of(request);
        mActionValue = Optional.empty();
    }

    public static RequestMessage fromAction(Action action) {
        return new RequestMessage(action);
    }

    public static RequestMessage fromRequest(Request request) {
        return new RequestMessage(request);
    }

    public boolean isComplete() {
        if (mRequestValue.isPresent() && mActionValue.isPresent())
            throw new IllegalStateException("Shoot request is both complete and not...");

        return mActionValue.isPresent();
    }

    public Action asAction() {
        if (!isComplete())
            throw new UnsupportedOperationException("Cannot interpret incomplete shoot result as an action!");

        return mActionValue.get();
    }

    public Request asRequest() {
        if (isComplete())
            throw new UnsupportedOperationException("Cannot interpret complete shoot result as a response!");

        return mRequestValue.get();
    }
}
