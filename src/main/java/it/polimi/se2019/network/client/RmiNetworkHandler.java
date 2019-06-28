package it.polimi.se2019.network.client;

import it.polimi.se2019.view.request.Request;

public class RmiNetworkHandler implements ClientNetworkHandler {
    @Override
    public void update(Request request) {
        //TODO: add calls to controller
    }

    @Override
    public boolean sendUsername(String username) {
        return false;
    }
}
