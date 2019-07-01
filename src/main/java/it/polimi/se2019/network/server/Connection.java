package it.polimi.se2019.network.server;

public interface Connection {
    void sendMessage(String message);
    String waitForMessage();
    void close();
    boolean isClosed();
}
