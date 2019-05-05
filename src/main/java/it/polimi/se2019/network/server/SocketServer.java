package it.polimi.se2019.network.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Server implements Runnable{
    private Socket socket;
    private ServerSocket serverSocket = null;
    private DataInputStream in;
    private DataOutputStream out;

    public SocketServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
    }

    @Override
    public void run(){
        try {
            // TODO: pass proper argument
            // SocketPlayerConnection player = new SocketPlayerConnection();
            Socket socket = serverSocket.accept();
            SocketPlayerConnection player = new SocketPlayerConnection(socket,this);
            registerConnection(player);
            //    if(waitingPlayer.size()==1)
            //TODO:          new GameThread(socket)
        } catch(IOException e){
            System.out.println("Errore di connessione");
        }
    }

    public static void main(String[] args) {
        Server server;
        try {
            server = new SocketServer();
            server.run();
        } catch (IOException e) {
            System.err.println("Impossibile inizializzare il network");
        }
    }

}
