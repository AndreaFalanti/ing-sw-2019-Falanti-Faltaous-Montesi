package it.polimi.se2019.network.client;

public class ClientMessage {
    private ClientMessageType mType;
    private String mRawContents;

    // trivial constructor
    public ClientMessage(ClientMessageType type, String rawContents) {
        mType = type;
        mRawContents = rawContents;
    }

    // trivial getters
    public ClientMessageType getType() {
        return mType;
    }

    public String getRawContents() {
        return mRawContents;
    }
}

