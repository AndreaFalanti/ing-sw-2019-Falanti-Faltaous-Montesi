package it.polimi.se2019.network.server;

import it.polimi.se2019.network.connection.Connection;

/**
 * Interface used by registration server
 *
 * @author Andrea Falanti
 */
public interface ConnectionRegister {
    boolean isUsernameAvailable (String username);
    void registerConnection (PlayerConnection connection);
    void deregisterConnection (PlayerConnection connection);
    boolean registerPlayer (String username, Connection connection);
    void deregisterPlayer (String username);
}
