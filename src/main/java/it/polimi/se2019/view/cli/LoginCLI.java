package it.polimi.se2019.view.cli;

import it.polimi.se2019.network.client.ClientInterface;
import it.polimi.se2019.network.client.RmiClient;
import it.polimi.se2019.network.client.SocketClient;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LoginCLI {
    private static int SOCKETPORT = 4567;
    private static int RMIPORT = 4568;
    public static void log() throws IOException, NotBoundException {

        String username="";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose a username:");
        System.out.print(">> ");
        while (username.equals("")) {
            username = scanner.nextLine();
        }


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
                } else {
                    validCmd = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Choose a number please");
                System.out.print("\n>> ");
                // flush remaining \n in buffer
                scanner.next();
                validCmd = false;
            }
        } while (!validCmd);

        ClientInterface client;
        switch (result) {
            case 1:
                client = new SocketClient("localhost", SOCKETPORT);
                break;
            case 2:
                client = new RmiClient("localhost", RMIPORT);
                break;
            default:
                throw new IllegalStateException("invalid client selected");
        }

        client.run();
    }
}

