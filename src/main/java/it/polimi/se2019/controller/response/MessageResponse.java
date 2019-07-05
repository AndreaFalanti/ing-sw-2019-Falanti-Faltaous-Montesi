package it.polimi.se2019.controller.response;

import it.polimi.se2019.view.ResponseHandler;

/**
 * Response used to notify view of a message to display
 *
 * @author Andrea Falanti
 */
public class MessageResponse implements Response {
    private String mMessage;
    private boolean mError;

    public MessageResponse(String message, boolean error) {
        mMessage = message;
        mError = error;
    }

    public String getMessage() {
        return mMessage;
    }

    public boolean isError() {
        return mError;
    }

    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}