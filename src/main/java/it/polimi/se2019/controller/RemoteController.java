package it.polimi.se2019.controller;

import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.view.request.GrabRequest;
import it.polimi.se2019.view.request.LeaderboardRequest;
import it.polimi.se2019.view.request.ReloadRequest;
import it.polimi.se2019.view.request.ValidMoveRequest;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteController extends RequestHandler, Remote {
    Response remoteHandle (GrabRequest request) throws RemoteException;
    Response remoteHandle (ReloadRequest request) throws RemoteException;
    Response remoteHandle (LeaderboardRequest request) throws RemoteException;
    Response remoteHandle (ValidMoveRequest request) throws RemoteException;
}
