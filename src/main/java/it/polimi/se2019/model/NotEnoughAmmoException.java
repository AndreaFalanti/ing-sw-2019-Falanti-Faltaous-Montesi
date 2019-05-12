package it.polimi.se2019.model;

public class NotEnoughAmmoException extends RuntimeException {
    public NotEnoughAmmoException (String message) {
        super (message);
    }
}
