package it.polimi.se2019.controller;

import it.polimi.se2019.controller.responses.Response;
import it.polimi.se2019.view.requests.GrabRequest;
import it.polimi.se2019.view.requests.LeaderboardRequest;
import it.polimi.se2019.view.requests.ReloadRequest;
import it.polimi.se2019.view.requests.ValidMoveRequest;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteController extends RequestHandler, Remote {
    Response remoteHandle (GrabRequest request) throws RemoteException;
    Response remoteHandle (ReloadRequest request) throws RemoteException;
    Response remoteHandle (LeaderboardRequest request) throws RemoteException;
    Response remoteHandle (ValidMoveRequest request) throws RemoteException;
}
