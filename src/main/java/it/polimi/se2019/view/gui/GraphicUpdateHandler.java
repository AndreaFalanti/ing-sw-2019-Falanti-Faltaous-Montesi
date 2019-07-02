package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.PowerUpCard;
import it.polimi.se2019.model.update.*;
import javafx.application.Platform;

import java.util.Map;

public class GraphicUpdateHandler implements UpdateHandler {
    private MainScreen mMainController;

    public GraphicUpdateHandler(MainScreen mainController) {
        mMainController = mainController;
    }

    public void setMainController(MainScreen mainController) {
        mMainController = mainController;
    }

    @Override
    public void handle(PlayerPositionUpdate update) {
        Platform.runLater(() -> mMainController.getBoardController().movePawnToCoordinate(update.getPlayerPos(), update.getPlayerColor()));
    }

    @Override
    public void handle(PlayerAmmoUpdate update) {
        Platform.runLater(() -> mMainController.getPlayerControllerFromColor(update.getPlayerColor()).updateAmmo(update.getPlayerAmmo()));
    }

    @Override
    public void handle(PlayerDamageUpdate update) {
        Platform.runLater(() -> mMainController.getPlayerControllerFromColor(update.getDamagedPlayerColor())
                 .addDamageTokens(update.getShooterPlayerColor(), update.getDamageTaken()));
    }

    @Override
    public void handle(PlayerMarksUpdate update) {
        Platform.runLater(() -> mMainController.getPlayerControllerFromColor(update.getTargetPlayerColor())
                 .updateMarkLabel(update.getShooterPlayerColor(), update.getMarks()));
    }

    @Override
    public void handle(PlayerWeaponsUpdate update) {
        Platform.runLater(() -> {
            if (update.getPlayerColor() == mMainController.getClientColor()) {
                int weaponNum = update.getWeapons().length;
                String[] ids = new String[weaponNum];
                boolean[] booleans = new boolean[weaponNum];

                Weapon[] weapons = update.getWeapons();
                for (int i = 0; i < 3; i++) {
                    if (weapons[i] != null) {
                        ids[i] = weapons[i].getGuiID();
                        booleans[i] = weapons[i].isLoaded();
                    }
                    else {
                        ids[i] = null;
                    }
                }
                mMainController.updateWeaponBox(ids, booleans);
            }
            else {
                OtherPlayerPane playerController = (OtherPlayerPane)mMainController
                        .getPlayerControllerFromColor(update.getPlayerColor());
                playerController.updatePlayerWeapons(update.getWeapons());
            }
        });
    }

    @Override
    public void handle(PlayerPowerUpsUpdate update) {
        Platform.runLater(() -> {
            int powerUpsNum = 4;
            if (update.getPlayerColor() == mMainController.getClientColor()) {
                String[] ids = new String[powerUpsNum];
                for (int i = 0; i < powerUpsNum; i++) {
                    PowerUpCard powerUpCard = update.getPowerUpCards()[i];
                    ids[i] = (powerUpCard != null) ? update.getPowerUpCards()[i].getGuiID() : null;
                }
                mMainController.updatePowerUpGrid(ids);
            }
            else {
                OtherPlayerPane playerController = (OtherPlayerPane)mMainController
                        .getPlayerControllerFromColor(update.getPlayerColor());

                int counter = 0;
                for (PowerUpCard powerUpCard : update.getPowerUpCards()) {
                    if (powerUpCard != null) {
                        counter++;
                    }
                }
                playerController.updatePowerUpNum(counter);
            }
        });
    }

    @Override
    public void handle(BoardTileUpdate update) {
        Platform.runLater(() -> mMainController.getBoardController().updateBoardTile(update.getTile(), update.getTilePos()));
    }

    @Override
    public void handle(KillScoredUpdate update) {
        Platform.runLater(() -> {
            mMainController.getBoardController().setPlayerPawnVisibility(update.getPlayerKilledColor(), false);
            mMainController.getBoardController().addKillToKilltrack(update.getKillerColor(), update.isOverkill());
            mMainController.getPlayerControllerFromColor(update.getPlayerKilledColor()).addDeath();
            for (Map.Entry<PlayerColor, Integer> entry : update.getScores().entrySet()) {
                mMainController.getPlayerControllerFromColor(entry.getKey()).setScore(entry.getValue());
            }
        });
    }

    @Override
    public void handle(PlayerBoardFlipUpdate update) {
        Platform.runLater(() -> mMainController.getPlayerControllerFromColor(update.getPlayerColor()).flipBoard());
    }

    @Override
    public void handle(ActivePlayerUpdate update) {
        Platform.runLater(() -> {
            mMainController.getBoardController().updateActivePlayerText(update.getPlayerColor());
            mMainController.getBoardController().updateTurnText(update.getTurnNumber());
        });
    }

    @Override
    public void handle(PlayerRespawnUpdate update) {
        Platform.runLater(() -> mMainController.getPlayerControllerFromColor(update.getPlayerColor()).eraseDamage());
    }

    @Override
    public void handle(RemainingActionsUpdate update) {
        Platform.runLater(() -> mMainController.getBoardController().updateRemainingActionsText(update.getRemainingActions()));
    }
}
