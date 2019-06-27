package it.polimi.se2019.network.client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LaunchClient {
    public static void main(String[] args) throws IOException, NotBoundException {
        if (args.length != 3) {
            System.out.println("Provide host and ports in cmd");
            return;
        }

        String host = args[0];
        int socketPort = Integer.parseInt(args[1]);
        int rmiPort = Integer.parseInt(args[2]);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose client connection type: ");
        System.out.println("Press 1 for socket");
        System.out.println("Press 2 for rmi");
        System.out.print(">> ");

        int result = -1;
        boolean validCmd;
        do {
            try {
                result = scanner.nextInt();
                if (result < 1 || result > 2) {
                    System.out.println("Invalid input");
                    System.out.print("\n>> ");
                    validCmd = false;
                }
                else {
                    validCmd = true;
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Choose a number please");
                System.out.print("\n>> ");
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
                // client = new RmiClient(host, rmiPort);
                throw new UnsupportedOperationException("WIP");
            default:
                throw new IllegalStateException("invalid client selected");
        }

        client.run();
    }
}
