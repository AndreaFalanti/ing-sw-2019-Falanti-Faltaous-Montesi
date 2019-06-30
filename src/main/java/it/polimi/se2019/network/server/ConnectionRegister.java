package it.polimi.se2019.network.server;

import java.net.Socket;

public interface ConnectionRegister {
    boolean isUsernameAvailable (String username);
    void registerConnection (PlayerConnection connection);
    void deregisterConnection (PlayerConnection connection);
    boolean registerPlayer (String username, ConnectionType type, Socket socket);
    void deregisterPlayer (String username);
}
