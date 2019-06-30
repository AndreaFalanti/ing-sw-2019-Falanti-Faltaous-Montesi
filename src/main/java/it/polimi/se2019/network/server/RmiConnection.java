package it.polimi.se2019.network.server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RmiConnection implements Connection {
    private enum SenderType {
        Server,
        Client
    }

    private static class RmiMeseenger extends UnicastRemoteObject implements RmiMessengerRemote {
        private final BlockingQueue<String> mMessagesSentByClient = new LinkedBlockingQueue<>();
        private final BlockingQueue<String> mMessagesSentByServer = new LinkedBlockingQueue<>();

        protected RmiMeseenger() throws RemoteException {
        }

        @Override
        public void storeMessage(String senderType, String message) {
            try {
                if (senderType.equals(SenderType.Client.toString()))
                    mMessagesSentByClient.put(message);
                else if (senderType.equals(SenderType.Server.toString()))
                    mMessagesSentByServer.put(message);
                else
                    throw new IllegalArgumentException("Unrecognized sender type: " + senderType);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String retrieveMessage(String senderType) {
            String message = null;
            try {
                if (senderType.equals(SenderType.Client.toString()))
                    message = mMessagesSentByServer.take();
                else if (senderType.equals(SenderType.Server.toString()))
                    message = mMessagesSentByClient.take();
                else
                    throw new IllegalArgumentException("Unrecognized sender type: " + senderType);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
            registry.bind(id, new RmiMeseenger());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

        return new RmiConnection(port, SenderType.Server, id);
    }

    public static Connection establish(int port, String id) {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // check if id exists
        try {
            registry.lookup(id);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            throw new IllegalArgumentException("Connection with id [" + id + "] was never created!");
        }

        return new RmiConnection(port, SenderType.Client, id);
    }

    @Override
    public void sendMessage(String message) {
        try {
            Registry registry = LocateRegistry.getRegistry(mPort);
            RmiMessengerRemote messengerRemote =
                    (RmiMessengerRemote) registry.lookup(mId);
            messengerRemote.storeMessage(mSenderType.toString(), message);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String waitForMessage() {
        String result = null;

        try {
            Registry registry = LocateRegistry.getRegistry(mPort);
            RmiMessengerRemote messengerRemote =
                    (RmiMessengerRemote) registry.lookup(mId);

            System.out.println("waiting...");
            result = messengerRemote.retrieveMessage(mSenderType.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
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
