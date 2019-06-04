package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.update.*;

public class CLIUpdateHandler implements UpdateHandler {

    private CLIInfo mCLIInfo;

    public CLIUpdateHandler(CLIInfo cLIInfo){mCLIInfo=cLIInfo;}

    public void handle(PlayerPositionUpdate update) {
        mCLIInfo.getPlayerFromColor(update.getPlayerColor()).move(update.getPlayerPos());

    }
    public void handle(PlayerAmmoUpdate update) {
        mCLIInfo.getPlayerFromColor(update.getPlayerColor());
    }
    public void handle(PlayerDamageUpdate update) {

    }
    public void handle(PlayerMarksUpdate update) {

    }
    public void handle(PlayerWeaponsUpdate update) {

    }
    public void handle(PlayerPowerUpsUpdate update) {

    }
    public void handle(BoardTileUpdate update) {

    }
    public void handle(KillScoredUpdate update) {

    }
    public void handle(PlayerBoardFlipUpdate update) {

    }
    public void handle(ActivePlayerUpdate update) {

    }
}
