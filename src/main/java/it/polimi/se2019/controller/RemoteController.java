package it.polimi.se2019.controller;

import it.polimi.se2019.view.request.ValidPositionRequest;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteController extends RequestHandler, Remote {
    void remoteHandle (ValidPositionRequest request) throws RemoteException;
}
