package it.polimi.se2019.view.cli;

import it.polimi.se2019.network.client.NetworkHandler;

import java.io.IOException;
import java.rmi.NotBoundException;

public class Main {


    public static void main(String[] args) throws IOException, NotBoundException {

        CLIView view = new CLIView(null);
        LoginCLI.log(view);
        new Thread(() -> ((NetworkHandler)view.getNetworkHandler()).startReceivingMessages()).start();
    }
}