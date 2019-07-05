package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.update.*;

import java.util.Collections;

public class CLIUpdateHandler implements UpdateHandler {

    private CLIInfo mCLIInfo;
    private CLIView cliView;

    public CLIInfo getCLIInfo(){return mCLIInfo;}

    public void setUpdateHandlerCLIInfo(CLIView view,CLIInfo cliInfo){
        mCLIInfo = cliInfo;
        cliView = view;
    }

    public CLIUpdateHandler(CLIInfo cLIInfo){
        mCLIInfo = cLIInfo;
    }


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

            mCLIInfo.updateDamage(update.getDamagedPlayerColor(),update.getDamageTaken());
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


                mCLIInfo.updateKillTrack(update.getPlayerKilledColor(),update.getKillerColor(),
                     update.isOverkill(),update.getScores());

    }
    @Override
    public void handle(PlayerBoardFlipUpdate update) {
       mCLIInfo.updateBoardFlip(update.getPlayerColor());
    }
    @Override
    public void handle(ActivePlayerUpdate update) {


        if(mCLIInfo.getOwnerColor() != update.getPlayerColor()){
                 new Thread(() -> {
                     mCLIInfo.getBoard().addPlayers(mCLIInfo.getBoard().getBoardCLI(),mCLIInfo.getPlayersInfo());
                     cliView.infoPlayers();
                 }).start();
             }

                mCLIInfo.setActivePlayer(update.getPlayerColor());


        if(mCLIInfo.getOwnerColor() == update.getPlayerColor() &&
        !mCLIInfo.getOwner().getPlayerPosition().equalsIgnoreCase("not respawned")){
         System.out.println("It's your turn:\n");
         new Thread(() -> cliView.availableCommands()).start();
        }



    }
    @Override
    public void handle(PlayerRespawnUpdate update){

            mCLIInfo.updateOnRespawn(update.getPlayerColor());
    }

    @Override
    public void handle(RemainingActionsUpdate update) {
        //TODO
    }

    @Override
    public void handle(EndGameUpdate update) {
        int max = Collections.max(update.getLeaderboard().values());
        for (PlayerColor player : update.getLeaderboard().keySet()) {
            System.out.print(player.getPascalName() + update.getLeaderboard().get(player));
            if(update.getLeaderboard().get(player)== max)
                System.out.println("<-------WINNER");
            else
                System.out.println();
        }
    }
}
