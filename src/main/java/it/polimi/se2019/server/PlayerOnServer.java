package it.polimi.se2019.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import it.polimi.se2019.server.Server;


public class PlayerOnServer implements Runnable{

    private DataOutputStream out;
    private DataInputStream in;
    private Socket socket;
    private Server server;
    private boolean active = true;

    public PlayerOnServer(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run(){
        try{
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            server.waitingRoom(this);
        }catch(IOException e){
            System.err.print("Error!");
        }
        finally{
            close();
        }
    }

    public void close(){
        try{
            socket.close();
        } catch (IOException e){
            System.err.print("Error during closing");
        }
        active = false;
      //TODO pay attention uou cannot onli deregister  server.deregisterConnection(this);
    }

    public boolean isActive(){
        return active;
    }


    public DataInputStream getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }
}
