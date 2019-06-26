package it.polimi.se2019.view;

import it.polimi.se2019.model.PlayerColor;

import java.net.Socket;

public final class ViewFactory {
    public static SocketVirtualView createSocketVirtualView (Socket socket, PlayerColor ownerColor) {
        SocketVirtualView socketVirtualView = new SocketVirtualView(ownerColor);
        socketVirtualView.setupUpdateHandler();
        socketVirtualView.setupSocket(socket);

        return socketVirtualView;
    }
}
