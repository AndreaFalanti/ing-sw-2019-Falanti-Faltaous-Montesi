package it.polimi.se2019.controller;

import it.polimi.se2019.controller.responses.Response;
import it.polimi.se2019.model.action.responses.DiscardRequiredActionResponse;
import it.polimi.se2019.model.action.responses.MessageActionResponse;
import it.polimi.se2019.model.action.responses.SelectWeaponRequiredActionResponse;

public interface InvalidActionResponseHandler {
    Response handle (MessageActionResponse actionResponse);
    Response handle (DiscardRequiredActionResponse actionResponse);
    Response handle (SelectWeaponRequiredActionResponse actionResponse);
}
