package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.update.*;

public class CLIUpdateHandler implements UpdateHandler {

    private CLIInfo mCLIInfo;

    public CLIInfo getCLIInfo(){return mCLIInfo;}

    public void setUpdateHandlerCLIInfo(CLIInfo cliInfo){mCLIInfo = cliInfo;}

    public CLIUpdateHandler(CLIInfo cLIInfo){mCLIInfo=cLIInfo;}

    @Override
    public void handle(PlayerPositionUpdate update) {
        mCLIInfo.updatePosition(update.getPlayerColor(),update.getPlayerPos());
    }
    @Override
    public void handle(PlayerAmmoUpdate update) {
        mCLIInfo.updateAmmo(update.getPlayerColor(), update.getPlayerAmmo());
    }
    @Override
    public void handle(PlayerDamageUpdate update) {
        mCLIInfo.updateDamage(update.getDamagedPlayerColor(),update.getDamageTaken(),update.getShooterPlayerColor());
    }
    @Override
    public void handle(PlayerMarksUpdate update) {
        mCLIInfo.updateMarks(update.getTargetPlayerColor(),update.getMarks(),update.getShooterPlayerColor());
    }
    @Override
    public void handle(PlayerWeaponsUpdate update) {
        mCLIInfo.updateWeapon(update.getPlayerColor(),update.getWeapons());
    }
    @Override
    public void handle(PlayerPowerUpsUpdate update) {
        mCLIInfo.updatePowerUps(update.getPlayerColor(),update.getPowerUpCards());
    }
    @Override
    public void handle(BoardTileUpdate update) {
        if(update.getTile().getTileType().equalsIgnoreCase("spawn"))
            mCLIInfo.setSpawnTiles(update.getTile());
        else
            mCLIInfo.setNormalTiles(update.getTile(),update.getTilePos());
    }
    @Override
    public void handle(KillScoredUpdate update) {
        mCLIInfo.updateKillTrak(update.getPlayerKilledColor(),update.getKillerColor(),
                update.isOverkill(),update.getScores());
    }
    @Override
    public void handle(PlayerBoardFlipUpdate update) {
        mCLIInfo.updateBoardFlip(update.getPlayerColor());
    }
    @Override
    public void handle(ActivePlayerUpdate update) {
        mCLIInfo.setActivePlayer(update.getPlayerColor());
    }
    @Override
    public void handle(PlayerRespawnUpdate update){
        mCLIInfo.updateOnRespawn(update.getPlayerColor());
    }

}
