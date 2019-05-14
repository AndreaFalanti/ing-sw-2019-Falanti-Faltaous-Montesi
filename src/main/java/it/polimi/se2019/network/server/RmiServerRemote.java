package it.polimi.se2019.network.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerRemote extends Remote {
    void registerConnection () throws RemoteException;
    void deregisterConnection () throws RemoteException;
}
