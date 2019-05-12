package it.polimi.se2019.controller;

import it.polimi.se2019.controller.responses.MessageActionResponse;
import it.polimi.se2019.controller.responses.Response;
import it.polimi.se2019.view.requests.GrabRequest;
import it.polimi.se2019.view.requests.LeaderboardRequest;
import it.polimi.se2019.view.requests.ReloadRequest;
import it.polimi.se2019.view.requests.ValidMoveRequest;

public interface RequestHandler {
    void handle (GrabRequest request);
    void handle (ReloadRequest request);
    void handle (LeaderboardRequest request);
    void handle (ValidMoveRequest request);
    Response handle (MessageActionResponse request);
}
