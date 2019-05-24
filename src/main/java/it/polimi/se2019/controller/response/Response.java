package it.polimi.se2019.controller.response;

import it.polimi.se2019.view.ResponseHandler;

import java.io.Serializable;

public interface Response extends Serializable {
    void handleMe(ResponseHandler handler);
}