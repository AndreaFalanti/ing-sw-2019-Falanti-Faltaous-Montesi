package it.polimi.se2019.controller;

import it.polimi.se2019.view.requests.GrabRequest;
import it.polimi.se2019.view.requests.LeaderboardRequest;
import it.polimi.se2019.view.requests.ReloadRequest;
import it.polimi.se2019.view.requests.ValidMoveRequest;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteController extends RequestHandler, Remote {
    void remoteHandle (GrabRequest request) throws RemoteException;
    void remoteHandle (ReloadRequest request) throws RemoteException;
    void remoteHandle (LeaderboardRequest request) throws RemoteException;
    void remoteHandle (ValidMoveRequest request) throws RemoteException;
}
