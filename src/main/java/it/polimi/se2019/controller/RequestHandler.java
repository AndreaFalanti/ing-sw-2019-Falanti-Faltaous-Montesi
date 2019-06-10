package it.polimi.se2019.controller;

import it.polimi.se2019.view.request.*;

public interface RequestHandler {
    void handle(ValidPositionRequest request);

    void handle(TargetsSelectedRequest request);

    void handle(ActionRequest actionRequest);

    void handle(PowerUpDiscardedRequest request);

    void handle(WeaponSelectedRequest request);

    void handle(ShootRequest request);
}
