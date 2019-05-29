package it.polimi.se2019.model.update;

import it.polimi.se2019.util.AbstractHandler;

public interface UpdateHandler extends AbstractHandler<Update> {
    default void handle(PlayerPositionUpdate update) {
        fallbackHandle(update);
    }
}
