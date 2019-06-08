package it.polimi.se2019.view;

import it.polimi.se2019.controller.response.*;
import it.polimi.se2019.util.AbstractHandler;

public interface ResponseHandler extends AbstractHandler<Response> {
    default void handle(MessageResponse response) {
        fallbackHandle(response);
    }
    default void handle(PickWeaponResponse response) {
        fallbackHandle(response);
    }
    default void handle(ValidMoveResponse response) {
        fallbackHandle(response);
    }
    default void handle(DiscardPowerUpResponse response) {
        fallbackHandle(response);
    }
}
