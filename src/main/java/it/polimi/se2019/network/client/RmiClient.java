package it.polimi.se2019.network.client;

import it.polimi.se2019.network.server.RmiServerRemote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient extends Client {
    private RmiServerRemote mServerRemote;
    //private RemoteController mRemoteController;

    public RmiClient(String serverIp, int serverPort) throws RemoteException, NotBoundException {
        super(serverIp, serverPort);

        Registry registry = LocateRegistry.getRegistry(mServerIp, mServerPort);

        for (String name : registry.list()) {
            System.out.println("Registry bindings: " + name);
        }
        System.out.println("\n");

        // gets a reference for the remote server
        mServerRemote = (RmiServerRemote) registry.lookup("rmiServer");
    }

    @Override
    public void run() {
        try {
            mServerRemote.registerConnection();
        }
        catch (RemoteException e) {
            throw new IllegalStateException();
        }
        System.out.println("Running rmi client");
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void receiveMessage() {

    }
}
