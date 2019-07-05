package it.polimi.se2019.model;

/**
 * Exception used to signal an unhandled ammo value subtraction error
 *
 * @author Andrea Falanti
 */
public class NotEnoughAmmoException extends RuntimeException {
    public NotEnoughAmmoException (String message) {
        super (message);
    }
}
