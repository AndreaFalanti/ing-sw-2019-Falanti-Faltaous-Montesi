package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.update.*;

public class CLIUpdateHandler implements UpdateHandler {

    private CLIInfo mCLIInfo;

    public CLIUpdateHandler(CLIInfo cLIInfo){mCLIInfo=cLIInfo;}

    @Override
    public void handle(PlayerPositionUpdate update) {


    }
    @Override
    public void handle(PlayerAmmoUpdate update) {

    }
    @Override
    public void handle(PlayerDamageUpdate update) {

    }
    @Override
    public void handle(PlayerMarksUpdate update) {

    }
    @Override
    public void handle(PlayerWeaponsUpdate update) {

    }
    @Override
    public void handle(PlayerPowerUpsUpdate update) {

    }
    @Override
    public void handle(BoardTileUpdate update) {

    }
    @Override
    public void handle(KillScoredUpdate update) {

    }
    @Override
    public void handle(PlayerBoardFlipUpdate update) {

    }
    @Override
    public void handle(ActivePlayerUpdate update) {

    }
}
