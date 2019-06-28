package it.polimi.se2019.view;

import it.polimi.se2019.controller.response.*;

public interface ResponseHandler {
    void handle(MessageResponse response);
    void handle(PickWeaponResponse response) ;
    void handle(ValidMoveResponse response);
    void handle(DiscardPowerUpResponse response);
    void handle(PickDirectionResponse response);
    void handle(PickPositionResponse response);
    void handle(PickPowerUpsResponse response);
    void handle(PickTargetsResponse response);
    void handle(PickEffectsResponse response);
    void handle(PickWeaponModeResponse response);
    void handle(PickRoomColorResponse response);
}
