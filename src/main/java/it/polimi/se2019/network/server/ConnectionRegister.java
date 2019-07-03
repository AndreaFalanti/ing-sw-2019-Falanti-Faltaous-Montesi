package it.polimi.se2019.network.server;

import it.polimi.se2019.network.connection.Connection;
import it.polimi.se2019.network.connection.ConnectionType;

import java.net.Socket;

public interface ConnectionRegister {
    boolean isUsernameAvailable (String username);
    void registerConnection (PlayerConnection connection);
    void deregisterConnection (PlayerConnection connection);
    boolean registerPlayer (String username, Connection connection);
    void deregisterPlayer (String username);
}
