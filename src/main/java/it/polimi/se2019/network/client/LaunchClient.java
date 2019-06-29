package it.polimi.se2019.network.client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LaunchClient {
    private static void printLineToConsole(String message) {
        System.out.println(message);
    }

    private static void printToConsole(String message) {
        System.out.print(message);
    }

    public static void main(String[] args) throws IOException, NotBoundException {
        if (args.length != 3) {
            printLineToConsole("Provide host and ports in cmd");
            return;
        }

        String host = args[0];
        int socketPort = Integer.parseInt(args[1]);
        int rmiPort = Integer.parseInt(args[2]);

        Scanner scanner = new Scanner(System.in);
        printLineToConsole("Choose client connection type: ");
        printLineToConsole("Press 1 for socket");
        printLineToConsole("Press 2 for rmi");
        printToConsole(">> ");

        int result = -1;
        boolean validCmd;
        do {
            try {
                result = scanner.nextInt();
                if (result < 1 || result > 2) {
                    printLineToConsole("Invalid input");
                    printToConsole("\n>> ");
                    validCmd = false;
                }
                else {
                    validCmd = true;
                }
            }
            catch (InputMismatchException e) {
                printLineToConsole("Choose a number please");
                printToConsole("\n>> ");
                // flush remaining \n in buffer
                scanner.next();
                validCmd = false;
            }
        } while (!validCmd);

        // TODO: choose type of view

        ClientInterface client;
        switch (result) {
            case 1:
                client = new SocketClient(host, socketPort);
                break;
            case 2:
                client = new RmiClient(host, rmiPort);
                break;
                //throw new UnsupportedOperationException("WIP");
            default:
                throw new IllegalStateException("invalid client selected");
        }

        client.run();
    }
}
