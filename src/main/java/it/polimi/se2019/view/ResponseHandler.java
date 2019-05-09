package it.polimi.se2019.view;

import it.polimi.se2019.controller.responses.*;

public interface ResponseHandler {
    void handle (LeaderboardResponse response);
    void handle (MessageResponse response);
    void handle (PickWeaponResponse response);
    void handle (ValidMoveResponse response);
    void handle (MessageActionResponse response);
}
