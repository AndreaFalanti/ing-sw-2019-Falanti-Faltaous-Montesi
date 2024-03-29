package it.polimi.se2019.controller;

import it.polimi.se2019.view.request.*;

/**
 * Interface that provides handler methods for all request messages
 *
 * @author Andrea Falanti
 */
public interface RequestHandler {
    void handle(ValidPositionRequest request);
    void handle(TargetsSelectedRequest request);
    void handle(ActionRequest actionRequest);
    void handle(PowerUpDiscardedRequest request);
    void handle(WeaponSelectedRequest request);
    void handle(DirectionSelectedRequest request);
    void handle(PositionSelectedRequest request);
    void handle(EffectsSelectedRequest request);
    void handle(UndoWeaponInteractionRequest request);
    void handle(WeaponModeSelectedRequest request);
    void handle(PowerUpsSelectedRequest request);
    void handle(RoomSelectedRequest request);
    void handle(TurnEndRequest request);
    void handle(RespawnPowerUpRequest request);
    void handle(UsePowerUpRequest request);
    void handle(AmmoColorSelectedRequest request);
    void handle(ReconnectionRequest request);
    void handle(DisconnectionRequest request);
}
