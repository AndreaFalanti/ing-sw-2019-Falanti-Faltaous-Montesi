package it.polimi.se2019.view.gui;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.update.*;

import java.util.Map;

public class GraphicUpdateHandler implements UpdateHandler {
    private MainScreen mMainController;

    public GraphicUpdateHandler(MainScreen mainController) {
        mMainController = mainController;
    }

    @Override
    public void handle(PlayerPositionUpdate update) {
        mMainController.getBoardController().addPawnToCoordinate(update.getPlayerPos(), update.getPlayerColor());
    }

    @Override
    public void handle(PlayerAmmoUpdate update) {
        mMainController.getPlayerControllerFromColor(update.getPlayerColor()).updateAmmo(update.getPlayerAmmo());
    }

    @Override
    public void handle(PlayerDamageUpdate update) {
        mMainController.getPlayerControllerFromColor(update.getDamagedPlayerColor())
                .addDamageTokens(update.getShooterPlayerColor(), update.getDamageTaken());
    }

    @Override
    public void handle(PlayerMarksUpdate update) {
        mMainController.getPlayerControllerFromColor(update.getTargetPlayerColor())
                .updateMarkLabel(update.getShooterPlayerColor(), update.getMarks());
    }

    @Override
    public void handle(PlayerWeaponsUpdate update) {
        // TODO: update this when ids are correctly set in weapons
        String[] ids = new String[3];
        for (int i = 0; i < 3; i++) {
            //ids[i] = update.getWeapons()[i].
        }
        mMainController.updateWeaponBox(ids);
    }

    @Override
    public void handle(PlayerPowerUpsUpdate update) {
        String[] ids = new String[3];
        for (int i = 0; i < 3; i++) {
            ids[i] = update.getPowerUpCards()[i].getGuiID();
        }
        mMainController.updatePowerUpGrid(ids);
    }

    @Override
    public void handle(BoardUpdate update) {

    }

    @Override
    public void handle(KillScoredUpdate update) {
        mMainController.getBoardController().addKillToKilltrack(update.getKillerColor(), update.isOverkill());
        mMainController.getPlayerControllerFromColor(update.getPlayerKilledColor()).addDeath();
        for (Map.Entry<PlayerColor, Integer> entry : update.getScores().entrySet()) {
            mMainController.getPlayerControllerFromColor(entry.getKey()).setScore(entry.getValue());
        }
    }

    @Override
    public void handle(PlayerBoardFlipUpdate update) {
        mMainController.getPlayerControllerFromColor(update.getPlayerColor()).flipBoard();
    }

    @Override
    public void handle(ActivePlayerUpdate update) {
        mMainController.getBoardController().updateActivePlayerText(update.getPlayerColor());
    }
}
