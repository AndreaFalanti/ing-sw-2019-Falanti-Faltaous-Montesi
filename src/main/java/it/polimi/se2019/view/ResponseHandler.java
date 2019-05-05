package it.polimi.se2019.view;

import it.polimi.se2019.controller.responses.LeaderboardResponse;
import it.polimi.se2019.controller.responses.MessageResponse;
import it.polimi.se2019.controller.responses.PickWeaponResponse;
import it.polimi.se2019.controller.responses.ValidMoveResponse;

public interface ResponseHandler {
    void handle (LeaderboardResponse response);
    void handle (MessageResponse response);
    void handle (PickWeaponResponse response);
    void handle (ValidMoveResponse response);
}
