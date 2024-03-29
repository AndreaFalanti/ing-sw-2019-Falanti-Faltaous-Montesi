package it.polimi.se2019.model.update;

import it.polimi.se2019.util.AbstractHandler;

/**
 * Handler for update messages
 *
 * @author Andrea Falanti, Stefano Montesi
 */
public interface UpdateHandler extends AbstractHandler<Update> {
    default void handle(PlayerPositionUpdate update) {
        fallbackHandle(update);
    }
    default void handle(PlayerAmmoUpdate update) {
        fallbackHandle(update);
    }
    default void handle(PlayerDamageUpdate update) {
        fallbackHandle(update);
    }
    default void handle(PlayerMarksUpdate update) {
        fallbackHandle(update);
    }
    default void handle(PlayerWeaponsUpdate update) {
        fallbackHandle(update);
    }
    default void handle(PlayerPowerUpsUpdate update) {
        fallbackHandle(update);
    }
    default void handle(BoardTileUpdate update) {
        fallbackHandle(update);
    }
    default void handle(KillScoredUpdate update) {
        fallbackHandle(update);
    }
    default void handle(PlayerBoardFlipUpdate update) {
        fallbackHandle(update);
    }
    default void handle(ActivePlayerUpdate update) {
        fallbackHandle(update);
    }
    default void handle(PlayerRespawnUpdate update) {
        fallbackHandle(update);
    }
    default void handle(RemainingActionsUpdate update) {
        fallbackHandle(update);
    }
    default void handle(EndGameUpdate update) {
        fallbackHandle(update);
    }
}
