package it.polimi.se2019.controller;

import it.polimi.se2019.controller.response.MessageActionResponse;
import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.view.request.*;

public interface RequestHandler {
    Response handle (GrabRequest request);
    Response handle (ReloadRequest request);
    Response handle (LeaderboardRequest request);
    Response handle (ValidMoveRequest request);
    Response handle (MessageActionResponse request);
    Response handle (ShootRequest request);
}
