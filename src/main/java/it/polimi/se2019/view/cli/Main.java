package it.polimi.se2019.view.cli;

import it.polimi.se2019.network.client.NetworkHandler;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {


    public static void main(String[] args) throws IOException, NotBoundException {
        // create view
        CLIView view = new CLIView(null);

        // deactivate all loggers
        LogManager.getLogManager().reset();

        // start login
        LoginCLI.log(view);
        new Thread(() -> ((NetworkHandler)view.getNetworkHandler()).startReceivingMessages()).start();
    }
}