package it.polimi.se2019.network.connection;

public interface Connection {
    void sendMessage(String message);
    String waitForMessage();
    void close();
    boolean isClosed();
    String getId();
}
