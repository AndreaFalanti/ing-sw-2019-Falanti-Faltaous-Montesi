package it.polimi.se2019.view;

import it.polimi.se2019.controller.response.*;
import it.polimi.se2019.model.weapon.response.TargetSelectionResponse;
import it.polimi.se2019.util.AbstractHandler;

public interface ResponseHandler extends AbstractHandler<Response> {
    default void handle(LeaderboardResponse response) {
        fallbackHandle(response);
    }
    default void handle(MessageResponse response) {
        fallbackHandle(response);
    }
    default void handle(PickWeaponResponse response) {
        fallbackHandle(response);
    }
    default void handle(ValidMoveResponse response) {
        fallbackHandle(response);
    }
    default void handle(MessageActionResponse response) {
        fallbackHandle(response);
    }
    default void handle(TargetSelectionResponse response) {
        fallbackHandle(response);
    }
}
