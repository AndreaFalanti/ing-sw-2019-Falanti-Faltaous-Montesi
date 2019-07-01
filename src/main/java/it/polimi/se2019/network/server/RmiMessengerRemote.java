package it.polimi.se2019.network.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiMessengerRemote extends Remote {
    void storeMessage(String senderType, String message) throws RemoteException;
    String retrieveMessage(String senderType) throws RemoteException;
}
