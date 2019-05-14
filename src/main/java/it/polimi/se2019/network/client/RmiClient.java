package it.polimi.se2019.network.client;

import it.polimi.se2019.controller.RemoteController;

public class RmiClient extends Client {
    private RemoteController mRemoteController;

    public RmiClient(String serverIp, int serverPort, RemoteController remoteController) {
        super(serverIp, serverPort);
        mRemoteController = remoteController;
    }
}
