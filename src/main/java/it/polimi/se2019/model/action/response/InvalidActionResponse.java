package it.polimi.se2019.model.action.response;

import it.polimi.se2019.controller.InvalidActionResponseHandler;
import it.polimi.se2019.controller.response.Response;

public interface InvalidActionResponse {
    Response handle(InvalidActionResponseHandler handler);
}
