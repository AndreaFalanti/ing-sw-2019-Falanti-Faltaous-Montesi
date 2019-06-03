package it.polimi.se2019.controller;

import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.view.request.*;

public interface RequestHandler {
    void handle(GrabRequest request);

    void handle(ReloadRequest request);

    void handle(LeaderboardRequest request);

    void handle(ValidMoveRequest request);

    void handle(MessageActionResponse request);

    void handle(ShootRequest request);

    void handle(TargetsSelectedRequest request);

    void handle(ActionRequest actionRequest);
}
