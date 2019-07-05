package it.polimi.se2019.network.client;

import it.polimi.se2019.util.Observer;
import it.polimi.se2019.view.request.Request;

public interface ClientNetworkHandler extends Observer<Request> {
    boolean sendUsername (String username);
    void registerObservablesFromView ();
}
