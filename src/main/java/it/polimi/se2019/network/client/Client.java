package it.polimi.se2019.network.client;

import it.polimi.se2019.controller.responses.Response;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.requests.Request;

public abstract class Client {
    private View view;
    /*private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;*/
    private String mServerIp;
    private int mServerPort;

    public Client(String serverIp, int serverPort) {
        mServerIp = serverIp;
        mServerPort = serverPort;
    }

    public void reciveResponse(Response response){

    }

    public void sendRequest(Request request){

    }
}
