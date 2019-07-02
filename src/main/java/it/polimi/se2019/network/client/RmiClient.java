package it.polimi.se2019.network.client;

import it.polimi.se2019.network.server.RegistrationRemote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RmiClient extends Client {
    private RegistrationRemote mServerRemote;

    public RmiClient(String serverIp, int serverPort) throws RemoteException, NotBoundException {
        super(serverIp, serverPort);

        // initialize server for registration
        Registry registry = LocateRegistry.getRegistry(mServerIp, mServerPort);
        for (String name : registry.list()) {
            printLineToConsole("Registry bindings: " + name);
        }
        printLineToConsole("\n");

        // gets a reference for the remote server
        mServerRemote = (RegistrationRemote) registry.lookup("rmiRegistrationServer");
    }

    private static void printLineToConsole(String message) {
        System.out.println(message);
    }

    private static void printToConsole(String message) {
        System.out.print(message);
    }

    @Override
    public void run() {
        printLineToConsole("Running rmi client");

        Scanner scanner = new Scanner(System.in);
        String username;
        boolean validUsername;

        printLineToConsole("Insert username: ");
        do {
            try {
                printToConsole(">> ");
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
