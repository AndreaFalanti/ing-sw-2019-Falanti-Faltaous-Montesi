package it.polimi.se2019.controller;

import it.polimi.se2019.view.requests.GrabRequest;
import it.polimi.se2019.view.requests.LeaderboardRequest;
import it.polimi.se2019.view.requests.ReloadRequest;
import it.polimi.se2019.view.requests.ValidMoveRequest;

import java.rmi.Remote;

public interface RemoteController extends RequestHandler, Remote {
    void handle (GrabRequest request) throws RuntimeException;
    void handle (ReloadRequest request) throws RuntimeException;
    void handle (LeaderboardRequest request) throws RuntimeException;
    void handle (ValidMoveRequest request) throws RuntimeException;
}
