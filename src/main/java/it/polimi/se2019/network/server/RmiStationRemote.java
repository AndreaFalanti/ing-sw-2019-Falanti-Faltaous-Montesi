package it.polimi.se2019.network.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiStationRemote extends Remote {
    int generateUniqueAddress() throws RemoteException;
    void addMailbox(int id) throws RemoteException;
    void storeMessage(int senderAddress, String message) throws RemoteException;
    String retrieveMessage(int senderAddress) throws RemoteException;
}
