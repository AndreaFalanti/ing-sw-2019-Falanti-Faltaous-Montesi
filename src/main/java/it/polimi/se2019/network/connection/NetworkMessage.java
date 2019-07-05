package it.polimi.se2019.network.connection;

public class NetworkMessage {
    private NetworkMessageType mType;
    private String mRawContents;

    // trivial constructor
    public NetworkMessage(NetworkMessageType type, String rawContents) {
        mType = type;
        mRawContents = rawContents;
    }

    // trivial getters
    public NetworkMessageType getType() {
        return mType;
    }

    public String getRawContents() {
        return mRawContents;
    }
}

