package it.polimi.se2019.resource_handler;

public class BadLoadException extends RuntimeException {
    public BadLoadException(String msg) {
        super(msg);
    }
}

