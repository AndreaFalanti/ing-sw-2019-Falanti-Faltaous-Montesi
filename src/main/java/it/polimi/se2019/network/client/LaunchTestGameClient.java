package it.polimi.se2019.network.client;

import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.network.server.Connection;
import it.polimi.se2019.network.server.LaunchTestGameServer;
import it.polimi.se2019.network.server.RmiConnection;
import it.polimi.se2019.view.cli.CLIView;
import it.polimi.se2019.view.request.serialization.RequestFactory;


public class LaunchTestGameClient {

    public static void main(String[] args) {
        CLIView view = new CLIView(null);

        // Connection connection = SocketConnection.establish("localhost", LaunchTestGameServer.SOCKET_PORT);
        Connection connection = RmiConnection.establish(
                LaunchTestGameServer.RMI_PORT,
                PlayerColor.BLUE.getPascalName()
        );

        view.register(request ->
                connection.sendMessage(RequestFactory.toJson(request))
        );

        NetworkHandler networkHandler = new NetworkHandler(view, connection);
        networkHandler.startRecievingServerMessages();

        view.availableCommands();
    }
}

