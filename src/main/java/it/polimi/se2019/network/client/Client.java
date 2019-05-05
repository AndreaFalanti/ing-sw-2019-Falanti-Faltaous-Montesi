package it.polimi.se2019.network.client;

import it.polimi.se2019.controller.responses.Response;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.requests.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class Client {

    private View view;
    public static final int PORT = 4567;
    private Socket socket;
    private DataOutputStream out ;
    private DataInputStream in ;
    private String serverURL = "127.0.0.1";

    public void reciveResponse(Response response){

    }

    public void sendRequest(Request request){

    }

    public static void main(String[] args){
        String serverURL = "127.0.0.1" ;
        DataInputStream in;
        DataOutputStream out;

        try(Socket socket = new Socket(serverURL,PORT)){
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

        } catch (IOException e) {
            System.err.println("Error.");
        }
    }

}
