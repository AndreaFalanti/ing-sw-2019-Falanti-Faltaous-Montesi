package it.polimi.se2019.network.server;

public interface ConnectionRegister {
    boolean isUsernameAvailable (String username);
    void registerConnection (PlayerConnection connection);
    void deregisterConnection (PlayerConnection connection);
    boolean registerPlayer (String username);
    void deregisterPlayer (String username);
}
