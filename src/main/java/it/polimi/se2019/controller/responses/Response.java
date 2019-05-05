package it.polimi.se2019.controller.responses;

import it.polimi.se2019.view.ResponseHandler;

import java.io.Serializable;
import java.util.*;

public interface Response extends Serializable {
    void handle(ResponseHandler handler);
}