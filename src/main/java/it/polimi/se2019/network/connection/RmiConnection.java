package it.polimi.se2019.network.connection;

import it.polimi.se2019.network.server.RmiStationRemote;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RmiConnection implements Connection {
    /**
     * Remote object representing a station with the mailboxes of all registered connections
     */
    private static class RmiStation extends UnicastRemoteObject implements RmiStationRemote {
        private final Map<Integer, BlockingQueue<String>> mMailboxes = new HashMap<>();
        private int mNextUniqueAddress = ACCEPTATION_MAILBOX_ADDRESS + 1;

        protected RmiStation() throws RemoteException {
        }

        @Override
        public int generateUniqueAddress() {
            return mNextUniqueAddress++;
        }

        @Override
        public void addMailbox(int address) {
            mMailboxes.put(address, new LinkedBlockingQueue<>());
        }

        @Override
        public void storeMessage(int senderAddress, String message) {
            if (mMailboxes.get(senderAddress) == null)
                mMailboxes.put(senderAddress, new LinkedBlockingQueue<>());
            mMailboxes.get(senderAddress).add(message);
        }

        @Override
        public String retrieveMessage(int senderAddress) {
            String message = null;

            try {
                if (mMailboxes.get(senderAddress) == null)
                    mMailboxes.put(senderAddress, new LinkedBlockingQueue<>());
                message = mMailboxes.get(senderAddress).take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return message;
        }
    }

    // logger
    private static final Logger logger = Logger.getLogger(RmiConnection.class.getName());

    // static constants
    public static final String RMI_STATION_REGISTRY_ID = "$station";
    private static final int ACCEPTATION_MAILBOX_ADDRESS = 0;
    static final int RMI_PORT = 4568;

    // fields
    private final int mAddress;
    private final int mPenPalAddress;

    // constructor
    private RmiConnection(int address, int penPalAddress) {
        mAddress = address;
        mPenPalAddress = penPalAddress;
    }

    public static void init() {
        try {
            // create registry
            Registry registry = LocateRegistry.createRegistry(RMI_PORT);

            // create station
            registry.bind(RMI_STATION_REGISTRY_ID, new RmiStation());

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    public static void startAccepting(Consumer<Connection> connectionConsumer) {
        new Thread(() -> {
            while (true) {
                connectionConsumer.accept(accept());
            }
        }).start();
    }

    /**
     * Waits for a connection to be established and accepts it, returning one of its acceptor endpoint
     * @return
     */
    public static RmiConnection accept() {
        RmiConnection result = null;
        try {
            RmiStationRemote station = getStation();

            int acceptorAddress = station.generateUniqueAddress();

            logger.log(Level.INFO, "Accepting rmi connections on {0}", acceptorAddress);
            String rawHandshakeMessage = station.retrieveMessage(ACCEPTATION_MAILBOX_ADDRESS);

            // TODO: include something else to decorate the accepted address
            int acceptedAddress = Integer.parseInt(rawHandshakeMessage);

            station.addMailbox(acceptedAddress);
            station.storeMessage(acceptedAddress, String.valueOf(acceptorAddress));

            logger.log(Level.INFO, "Accepted connection: {0} - {1}",
                    new Object[]{ acceptorAddress, acceptedAddress });
            result = new RmiConnection(acceptorAddress, acceptedAddress);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static RmiConnection establish() {

        RmiConnection result = null;
        try {
            RmiStationRemote station = getStation();

            int addressToAccept = station.generateUniqueAddress();

            // TODO: include something else to decorate the address to accept
            station.storeMessage(ACCEPTATION_MAILBOX_ADDRESS, String.valueOf(addressToAccept));

            logger.log(Level.INFO, "Trying to establish connection on {0}", addressToAccept);
            int acceptorAddress = Integer.parseInt(station.retrieveMessage(addressToAccept));

            logger.log(Level.INFO, "Established connection: {0} - {1}",
                    new Object[]{ addressToAccept, acceptorAddress });
            result = new RmiConnection(addressToAccept, acceptorAddress);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static RmiStationRemote getStation() {
        RmiStationRemote station = null;

        try {
            station =
                    (RmiStationRemote) LocateRegistry.getRegistry(RMI_PORT)
                            .lookup(RMI_STATION_REGISTRY_ID);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        return station;
    }

    @Override
    public void sendMessage(String message) {
        try {
            getStation().storeMessage(mPenPalAddress, message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String waitForMessage() {
        String message = null;

        try {
            message = getStation().retrieveMessage(mAddress);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return message;
    }

    @Override
    public void close() {
        // TODO: implement this
        // getStation().removeMailbox(mAddress);
        throw new UnsupportedOperationException("WIP");
    }

    @Override
    public boolean isClosed() {
        // TODO: implement this
        // getStation().removeMailbox(mAddress);
        throw new UnsupportedOperationException("WIP");
    }

    @Override
    public String getId() {
        return String.valueOf(mAddress);
    }
}
