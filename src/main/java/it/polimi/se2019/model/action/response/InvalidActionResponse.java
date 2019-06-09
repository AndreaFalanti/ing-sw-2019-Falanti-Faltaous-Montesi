package it.polimi.se2019.model.action.response;

import it.polimi.se2019.controller.InvalidActionResponseHandler;

public interface InvalidActionResponse {
    void handle(InvalidActionResponseHandler handler);
}
