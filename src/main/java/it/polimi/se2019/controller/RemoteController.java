package it.polimi.se2019.controller;

import it.polimi.se2019.view.requests.GrabRequest;
import it.polimi.se2019.view.requests.LeaderboardRequest;
import it.polimi.se2019.view.requests.ReloadRequest;
import it.polimi.se2019.view.requests.ValidMoveRequest;

public interface RemoteController extends RequestHandler{
    void handle (GrabRequest request);
    void handle (ReloadRequest request);
    void handle (LeaderboardRequest request);
    void handle (ValidMoveRequest request);
}
