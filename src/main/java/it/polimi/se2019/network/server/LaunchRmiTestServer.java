package it.polimi.se2019.network.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LaunchRmiTestServer {
    private static final Logger mLogger = Logger.getLogger(LaunchServer.class.getName());

    public static void main(String[] args) throws IOException {
        RegistrationServer registrationServer = new RegistrationServer(1111);

        mLogger.log(Level.INFO, "Registering players");
        registrationServer.registerPlayerRemote("Mario");
        registrationServer.registerPlayerRemote("Luigi");
        registrationServer.registerPlayerRemote("Smurfette");
    }
}

