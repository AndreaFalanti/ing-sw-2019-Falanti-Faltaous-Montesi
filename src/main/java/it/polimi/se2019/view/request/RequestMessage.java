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

    public static RequestMessage from(Action action) {
        return new RequestMessage(action);
    }

    public static RequestMessage from(Request request) {
        return new RequestMessage(request);
    }

    public boolean isAction() {
        if (mRequestValue.isPresent() && mActionValue.isPresent())
            throw new IllegalStateException("Shoot request is both complete and not...");

        return mActionValue.isPresent();
    }

    public boolean isRequest() {
        return !isAction();
    }

    public Action asAction() {
        if (!isAction())
            throw new UnsupportedOperationException("Cannot interpret request message as an action message!");

        return mActionValue.get();
    }

    public Request asRequest() {
        if (!isRequest())
            throw new UnsupportedOperationException("Cannot interpret action message as a request message!");

        return mRequestValue.get();
    }
}
