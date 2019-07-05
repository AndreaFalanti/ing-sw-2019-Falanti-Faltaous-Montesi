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
import java.util.Objects;
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
                logger.log(Level.SEVERE, e.getMessage(), e);
                Thread.currentThread().interrupt();
            }

            return message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            RmiStation that = (RmiStation) o;
            return mNextUniqueAddress == that.mNextUniqueAddress &&
                    Objects.equals(mMailboxes, that.mMailboxes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), mMailboxes, mNextUniqueAddress);
        }
    }

    // logger
    private static final Logger logger = Logger.getLogger(RmiConnection.class.getName());

    // static constants
    private static final String RMI_STATION_REGISTRY_ID = "station";
    private static final int ACCEPTATION_MAILBOX_ADDRESS = 0;

    // statics set by init
    private static String rmiServerHost;

    // fields
    private final int mAddress;
    private final int mPenPalAddress;
    private final String mServerHost;
    private final int mPort;

    // constructor
    private RmiConnection(String serverHost, int port, int address, int penPalAddress) {
        mPort = port;
        mAddress = address;
        mServerHost = serverHost;
        mPenPalAddress = penPalAddress;
    }

    public static void init(String serverHost, int port) {
        try {
            rmiServerHost = serverHost;

            // set the server ip address
            System.setProperty("java.rmi.server.hostname", serverHost);

            // create registry
            Registry registry = LocateRegistry.createRegistry(port);

            // create station
            registry.bind(
                    "rmi://" + serverHost + ":" + port + "/" + RMI_STATION_REGISTRY_ID,
                    new RmiStation()
            );

        } catch (RemoteException|AlreadyBoundException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void startAccepting(int port, Consumer<Connection> connectionConsumer) {
        new Thread(() -> {
            while (true) {
                connectionConsumer.accept(accept(port));
            }
        }).start();
    }

    /**
     * Waits for a connection to be established and accepts it, returning one of its acceptor endpoint
     * @return RmiConnection created
     */
    public static RmiConnection accept(int port) {
        RmiConnection result = null;
        try {
            RmiStationRemote station = getStation(rmiServerHost, port);

            int acceptorAddress = station.generateUniqueAddress();

            logger.log(Level.INFO, "Accepting rmi connections on {0}", acceptorAddress);
            String rawHandshakeMessage = station.retrieveMessage(ACCEPTATION_MAILBOX_ADDRESS);

            int acceptedAddress = Integer.parseInt(rawHandshakeMessage);

            station.addMailbox(acceptedAddress);
            station.storeMessage(acceptedAddress, String.valueOf(acceptorAddress));

            logger.log(Level.INFO, "Accepted connection: {0} - {1}",
                    new Object[]{ acceptorAddress, acceptedAddress });
            result = new RmiConnection(rmiServerHost, port, acceptorAddress, acceptedAddress);
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    public static RmiConnection establish(String host, int port) {

        RmiConnection result = null;
        try {
            RmiStationRemote station = getStation(host, port);

            int addressToAccept = station.generateUniqueAddress();

            // TODO: include something else to decorate the address to accept
            station.storeMessage(ACCEPTATION_MAILBOX_ADDRESS, String.valueOf(addressToAccept));

            logger.log(Level.INFO, "Trying to establish connection on {0}", addressToAccept);
            int acceptorAddress = Integer.parseInt(station.retrieveMessage(addressToAccept));

            logger.log(Level.INFO, "Established connection: {0} - {1}",
                    new Object[]{ addressToAccept, acceptorAddress });
            result = new RmiConnection(host, port, addressToAccept, acceptorAddress);
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    private static RmiStationRemote getStation(String host, int port) {
        RmiStationRemote station = null;

        try {
            station =
                    (RmiStationRemote) LocateRegistry.getRegistry(port)
                            .lookup("rmi://" + host + ":" + port + "/" + RMI_STATION_REGISTRY_ID);
        } catch (RemoteException|NotBoundException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            // force shutdown if something is wrong
            System.exit(0);
        }

        return station;
    }

    @Override
    public void sendMessage(String message) {
        try {
            getStation(mServerHost, mPort).storeMessage(mPenPalAddress, message);
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public String waitForMessage() {
        String message = null;

        try {
            message = getStation(mServerHost, mPort).retrieveMessage(mAddress);
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RmiConnection that = (RmiConnection) o;
        return mAddress == that.mAddress &&
                mPenPalAddress == that.mPenPalAddress;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mAddress, mPenPalAddress);
    }
}
