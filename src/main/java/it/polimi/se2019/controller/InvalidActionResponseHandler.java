package it.polimi.se2019.controller;

import it.polimi.se2019.model.action.response.DiscardRequiredActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;
import it.polimi.se2019.model.action.response.SelectWeaponRequiredActionResponse;

public interface InvalidActionResponseHandler {
    void handle(MessageActionResponse actionResponse);
    void handle(DiscardRequiredActionResponse actionResponse);
    void handle(SelectWeaponRequiredActionResponse actionResponse);
}
