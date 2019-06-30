package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.PowerUpCard;
import it.polimi.se2019.model.update.*;

import java.util.Map;

public class GraphicUpdateHandler implements UpdateHandler {
    private MainScreen mMainController;

    public GraphicUpdateHandler(MainScreen mainController) {
        mMainController = mainController;
    }

    @Override
    public void handle(PlayerPositionUpdate update) {
        mMainController.getBoardController().movePawnToCoordinate(update.getPlayerPos(), update.getPlayerColor());
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
        if (update.getPlayerColor() == mMainController.getClientColor()) {
            String[] ids = new String[3];

            Weapon[] weapons = update.getWeapons();
            for (int i = 0; i < 3; i++) {
                if (weapons[i] != null) {
                    ids[i] = weapons[i].getGuiID();
                }
                else {
                    ids[i] = null;
                }
            }
            mMainController.updateWeaponBox(ids);
        }
        else {
            OtherPlayerPane playerController = (OtherPlayerPane)mMainController
                    .getPlayerControllerFromColor(update.getPlayerColor());
            playerController.updatePlayerWeapons(update.getWeapons());
        }
    }

    @Override
    public void handle(PlayerPowerUpsUpdate update) {
        int powerUpsNum = 4;
        if (update.getPlayerColor() == mMainController.getClientColor()) {
            String[] ids = new String[powerUpsNum];
            for (int i = 0; i < powerUpsNum; i++) {
                ids[i] = update.getPowerUpCards()[i].getGuiID();
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
    }

    @Override
    public void handle(BoardTileUpdate update) {
        mMainController.getBoardController().updateBoardTile(update.getTile(), update.getTilePos());
    }

    @Override
    public void handle(KillScoredUpdate update) {
        mMainController.getBoardController().setPlayerPawnVisibility(update.getPlayerKilledColor(), false);
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
        mMainController.getBoardController().updateRemainingActionsText(update.getRemainingActions());
        mMainController.getBoardController().updateTurnText(update.getTurnNumber());
    }

    @Override
    public void handle(PlayerRespawnUpdate update) {
        mMainController.getPlayerControllerFromColor(update.getPlayerColor()).eraseDamage();
    }
}
