package it.polimi.se2019.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class PlayerOnServer {

    private DataOutputStream out;
    private DataInputStream in;

    public PlayerOnServer(DataInputStream in, DataOutputStream out) {
        this.in = in;
        this.out = out;
    }

    public DataInputStream getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }
}
