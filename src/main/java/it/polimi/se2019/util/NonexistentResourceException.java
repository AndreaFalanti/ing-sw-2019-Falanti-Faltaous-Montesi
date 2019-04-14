package it.polimi.se2019.util;

public class NonexistentResourceException extends RuntimeException {
    public NonexistentResourceException(String key) {
        super("Trying to reference nonexistent resource [key: " + key + "]!");
    }
}
