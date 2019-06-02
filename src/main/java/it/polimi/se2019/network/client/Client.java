package it.polimi.se2019.network.client;

import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.Request;

public abstract class Client implements ClientInterface {
    protected View view;
    protected String mServerIp;
    protected int mServerPort;

    public Client(String serverIp, int serverPort) {
        mServerIp = serverIp;
        mServerPort = serverPort;
    }

    public void receiveResponse(Response response){

    }

    public void sendRequest(Request request){

    }
}
