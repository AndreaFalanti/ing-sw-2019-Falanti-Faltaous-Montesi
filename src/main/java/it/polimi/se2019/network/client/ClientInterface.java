package it.polimi.se2019.network.client;

public interface ClientInterface {
    void run();
    void sendMessage (String message);
    String receiveMessage ();
}
