package it.polimi.se2019.network.server;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerRemote extends Remote, Serializable {
    void registerConnection () throws RemoteException;
    void deregisterConnection () throws RemoteException;
}
