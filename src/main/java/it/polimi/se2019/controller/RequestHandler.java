package it.polimi.se2019.controller;

import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.view.request.*;

public interface RequestHandler {
    default Response handle(GrabRequest request) { return null; }
    default Response handle(ReloadRequest request) { return null; }
    default Response handle(LeaderboardRequest request) { return null; }
    default Response handle(ValidMoveRequest request) { return null; }
    default Response handle(ShootRequest request) { return null; }
    default Response handle(TargetsSelectedRequest request) { return null; }
}
