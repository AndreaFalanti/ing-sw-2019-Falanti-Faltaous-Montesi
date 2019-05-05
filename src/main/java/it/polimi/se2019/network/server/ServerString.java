package it.polimi.se2019.network.server;

public class ServerString {
    private String serverURL;
    private String port;

    public ServerString(String serverURL,String port){
        this.port = port;
        this.serverURL = serverURL;
    }

    public String getServerURL(){
        return serverURL;
    }

    public String getPort() {
        return port;
    }
}
