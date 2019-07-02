package it.polimi.se2019.network.connection;

import it.polimi.se2019.network.server.RmiMessengerRemote;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RmiConnection implements Connection {
    private static final Logger logger = Logger.getLogger(RmiConnection.class.getName());

    private enum SenderType {
        SERVER,
        CLIENT
    }

    private static class RmiMessenger extends UnicastRemoteObject implements RmiMessengerRemote {
        private final BlockingQueue<String> mMessagesSentByClient = new LinkedBlockingQueue<>();
        private final BlockingQueue<String> mMessagesSentByServer = new LinkedBlockingQueue<>();

        protected RmiMessenger() throws RemoteException {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            RmiMessenger that = (RmiMessenger) o;
            return Objects.equals(mMessagesSentByClient, that.mMessagesSentByClient) &&
                    Objects.equals(mMessagesSentByServer, that.mMessagesSentByServer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), mMessagesSentByClient, mMessagesSentByServer);
        }

        @Override
        public void storeMessage(String senderType, String message) {
            try {
                if (senderType.equals(SenderType.CLIENT.toString()))
                    mMessagesSentByClient.put(message);
                else if (senderType.equals(SenderType.SERVER.toString()))
                    mMessagesSentByServer.put(message);
                else
                    throw new IllegalArgumentException("Unrecognized sender type: " + senderType);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
            }
        }

        @Override
        public String retrieveMessage(String senderType) {
            String message = null;
            try {
                if (senderType.equals(SenderType.CLIENT.toString()))
                    message = mMessagesSentByServer.take();
                else if (senderType.equals(SenderType.SERVER.toString()))
                    message = mMessagesSentByClient.take();
                else
                    throw new IllegalArgumentException("Unrecognized sender type: " + senderType);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
            }

            if (message == null)
                throw new NullPointerException();

            return message;
        }
    }

    private int mPort;
    private String mId;
    private SenderType mSenderType;

    private RmiConnection(int port, SenderType senderType, String id) {
        mPort = port;
        mSenderType = senderType;
        mId = id;
    }

    public static Connection create(int port, String id) {
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            registry.bind(id, new RmiMessenger());
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
        } catch (AlreadyBoundException e) {
            logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
        }

        return new RmiConnection(port, SenderType.SERVER, id);
    }

    public static Connection establish(int port, String id) {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(port);
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
        }

        // check if id exists
        try {
            registry.lookup(id);
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
        } catch (NotBoundException e) {
            throw new IllegalArgumentException("Connection with id [" + id + "] was never created!");
        }

        return new RmiConnection(port, SenderType.CLIENT, id);
    }

    @Override
    public void sendMessage(String message) {
        try {
            Registry registry = LocateRegistry.getRegistry(mPort);
            RmiMessengerRemote messengerRemote =
                    (RmiMessengerRemote) registry.lookup(mId);
            messengerRemote.storeMessage(mSenderType.toString(), message);
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
        } catch (NotBoundException e) {
            logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
        }
    }

    @Override
    public String waitForMessage() {
        String result = null;

        try {
            Registry registry = LocateRegistry.getRegistry(mPort);
            RmiMessengerRemote messengerRemote =
                    (RmiMessengerRemote) registry.lookup(mId);

            logger.info("waiting...");
            result = messengerRemote.retrieveMessage(mSenderType.toString());
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
        } catch (NotBoundException e) {
            logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
        }

        return result;
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isClosed() {
        throw new UnsupportedOperationException();
    }
}
