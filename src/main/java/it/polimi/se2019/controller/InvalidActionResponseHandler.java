package it.polimi.se2019.controller;

import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.action.response.DiscardRequiredActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;
import it.polimi.se2019.model.action.response.SelectWeaponRequiredActionResponse;

public interface InvalidActionResponseHandler {
    Response handle(MessageActionResponse actionResponse);
    Response handle(DiscardRequiredActionResponse actionResponse);
    Response handle(SelectWeaponRequiredActionResponse actionResponse);
}
