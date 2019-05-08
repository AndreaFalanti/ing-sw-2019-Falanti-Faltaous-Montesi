package it.polimi.se2019.resource_handler;

public class NonExistentResourceException extends RuntimeException {
    public NonExistentResourceException(String key) {
        super("Trying to reference nonexistent resource [key: " + key + "]!");
    }
}
