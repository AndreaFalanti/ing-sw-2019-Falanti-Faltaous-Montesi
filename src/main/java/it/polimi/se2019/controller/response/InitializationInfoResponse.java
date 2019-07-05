package it.polimi.se2019.controller.response;

import it.polimi.se2019.view.InitializationInfo;
import it.polimi.se2019.view.ResponseHandler;

/**
 * Response used to notify view about initialization data
 *
 * @author Andrea Falanti
 */
public class InitializationInfoResponse implements Response {
    private InitializationInfo mInitializationInfo;

    public InitializationInfoResponse(InitializationInfo initializationInfo) {
        mInitializationInfo = initializationInfo;
    }

    public InitializationInfo getInitializationInfo() {
        return mInitializationInfo;
    }

    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}
