package it.polimi.se2019.model;

public class NotEnoughAmmoException extends Exception {
    public NotEnoughAmmoException (String message) {
        super (message);
    }
}
