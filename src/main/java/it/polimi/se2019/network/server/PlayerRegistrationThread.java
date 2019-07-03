package it.polimi.se2019.network.server;

import it.polimi.se2019.network.connection.Connection;

import java.util.logging.Logger;

public class PlayerRegistrationThread extends Thread {
    private static final Logger logger = Logger.getLogger(PlayerRegistrationThread.class.getName());

    private Connection mConnection;
    private RegistrationServer mRegister;

    public PlayerRegistrationThread(Connection connection, RegistrationServer register) {
        mConnection = connection;
        mRegister = register;
    }

    public void close(){
        mConnection.close();
    }

    /**
     * Handle player registration from socket, it asks for a username and inform client if it's valid or is already
     * picked. If valid, create a PlayerConnection, than close itself.
     */
    @Override
    public void run () {
        String username = null;
        boolean logged = false;

        while (!logged && !isInterrupted()) {
            username = mConnection.waitForMessage();
            if (mRegister.isUsernameAvailable(username)) {
                logged = mRegister.registerPlayer(username, mConnection);
                mConnection.sendMessage("ok");
            } else {
                mConnection.sendMessage("Username is already used");
            }
        }

        logger.info("Closing registration thread " + getId());
    }
}
