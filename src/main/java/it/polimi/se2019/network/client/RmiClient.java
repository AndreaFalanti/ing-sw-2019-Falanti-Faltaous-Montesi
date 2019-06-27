package it.polimi.se2019.network.client;

import it.polimi.se2019.network.server.RegistrationRemote;
import it.polimi.se2019.view.RemoteView;
import it.polimi.se2019.view.View;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RmiClient extends Client {
    private RegistrationRemote mServerRemote;

    public RmiClient(String serverIp, int serverPort) throws RemoteException, NotBoundException {
        super(serverIp, serverPort);

        // initialize server for registration
        Registry registry = LocateRegistry.getRegistry(mServerIp, mServerPort);
        for (String name : registry.list()) {
            System.out.println("Registry bindings: " + name);
        }
        System.out.println("\n");

        // gets a reference for the remote server
        mServerRemote = (RegistrationRemote) registry.lookup("rmiServer");
    }

    @Override
    public void run() {
        System.out.println("Running rmi client");

        Scanner scanner = new Scanner(System.in);
        String username;
        Boolean validUsername;

        System.out.println("Insert username: ");
        do {
            try {
                System.out.print(">> ");
                username = scanner.nextLine();
                validUsername = mServerRemote.registerPlayerRemote(username);
            } catch (RemoteException e) {
                throw new IllegalStateException();
            }
        } while (!validUsername);
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public String receiveMessage() {
        return null;
    }
}
