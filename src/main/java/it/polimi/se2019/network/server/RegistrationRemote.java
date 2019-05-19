package it.polimi.se2019.network.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RegistrationRemote extends Remote {
    boolean registerPlayerRemote (String username) throws RemoteException;
    void deregisterPlayerRemote (String username) throws RemoteException;
}
