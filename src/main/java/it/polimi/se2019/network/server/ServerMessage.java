package it.polimi.se2019.network.server;

public class ServerMessage {
    private ServerMessageType mType;
    private String mRawContents;

    // trivial constructor
    public ServerMessage(ServerMessageType type, String rawContents) {
        mType = type;
        mRawContents = rawContents;
    }

    // trivial getters
    public ServerMessageType getType() {
        return mType;
    }

    public String getRawContents() {
        return mRawContents;
    }
}
