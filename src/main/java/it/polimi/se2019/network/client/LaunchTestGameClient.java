package it.polimi.se2019.network.client;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.network.server.Connection;
import it.polimi.se2019.network.server.LaunchTestGameServer;
import it.polimi.se2019.network.server.RmiConnection;
import it.polimi.se2019.network.server.SocketConnection;
import it.polimi.se2019.view.TestView;
import it.polimi.se2019.view.cli.CLIView;
import it.polimi.se2019.view.request.serialization.RequestFactory;

import java.net.Socket;
import java.util.Scanner;

import static it.polimi.se2019.network.server.LaunchTestGameServer.RMI_PORT;
import static it.polimi.se2019.network.server.LaunchTestGameServer.SOCKET_PORT;
import static it.polimi.se2019.util.InteractionUtils.*;

public class LaunchTestGameClient {
    public static void main(String[] args) {
        // pick connection
        print("Pick connection type: ");
        Connection connection =
                input().equals("rmi") ?
                        RmiConnection.establish(RMI_PORT, "connection") :
                        SocketConnection.establish("localhost", SOCKET_PORT);

        // create view and register message sending to server
        print("Pick player color: ");
        TestView view = new TestView(PlayerColor.valueOf(input()));

        // initialize network handler
        NetworkHandler networkHandler = new NetworkHandler(view, connection);
        networkHandler.startReceivingMessages();

        // start view user interaction
        view.startInteraction();
    }
}

