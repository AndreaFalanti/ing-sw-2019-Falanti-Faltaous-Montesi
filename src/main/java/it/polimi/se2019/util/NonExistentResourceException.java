package it.polimi.se2019.util;

public class NonExistentResourceException extends RuntimeException {
    public NonExistentResourceException(String key) {
        super("Trying to reference nonexistent resource [key: " + key + "]!");
    }
}
