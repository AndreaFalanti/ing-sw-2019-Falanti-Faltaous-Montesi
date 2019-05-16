package it.polimi.se2019.network.server;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RegistrationRemote extends Remote, Serializable {
    boolean registerPlayerRemote (String username) throws RemoteException;
    void deregisterPlayerRemote (String username) throws RemoteException;
}
