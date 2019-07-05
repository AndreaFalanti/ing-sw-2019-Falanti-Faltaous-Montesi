package it.polimi.se2019.view.cli;

import it.polimi.se2019.network.client.ClientNetworkHandler;
import it.polimi.se2019.network.client.NetworkHandler;
import it.polimi.se2019.network.connection.RmiConnection;
import it.polimi.se2019.network.connection.SocketConnection;

import java.util.InputMismatchException;
import java.util.Scanner;

public class LoginCLI {
    private static final int SOCKETPORT = 4567;
    private static final int RMIPORT = 4568;

    private LoginCLI() {
    }

    private static void printLineToConsole(String message) {
        System.out.println(message);
    }

    private static void printToConsole(String message) {
        System.out.print(message);
    }
    
    public static void log(CLIView view) {
        Scanner scanner = new Scanner(System.in);


        printLineToConsole("Choose client connection type: ");
        printLineToConsole("Press 1 for socket");
        printLineToConsole("Press 2 for rmi");
        printLineToConsole(">> ");

        int result = -1;
        boolean validCmd;
        do {
            try {
                result = scanner.nextInt();
                if (result < 1 || result > 2) {
                    printLineToConsole("Invalid input");
                    printToConsole("\n>> ");
                    validCmd = false;
                } else {
                    validCmd = true;
                }
            } catch (InputMismatchException e) {
                printLineToConsole("Choose a number please");
                printToConsole("\n>> ");
                // flush remaining \n in buffer
                scanner.next();
                validCmd = false;
            }
        } while (!validCmd);

        ClientNetworkHandler mNetworkHandler;
        boolean isValid=false;
        switch (result) {
            case 1:
                mNetworkHandler = new NetworkHandler(
                        view,
                        SocketConnection.establish("localhost", 4567)
                );

                break;
            case 2:
                mNetworkHandler = new NetworkHandler(
                        view,
                        RmiConnection.establish()
                );
                break;
            default:
                throw new IllegalStateException("invalid client selected");
        }

        String username;
        Scanner scanner1 = new Scanner(System.in);
        while(!isValid){
            printLineToConsole("Choose username");
            username=scanner1.nextLine();
            if ( !username.equals("") && mNetworkHandler.sendUsername(username)) {
                view.setNetworkHandler(mNetworkHandler);
                isValid=true;
            }
            else {
                printLineToConsole("username already used");
            }
        }

        printLineToConsole("\nWaiting for players...");
    }

}

