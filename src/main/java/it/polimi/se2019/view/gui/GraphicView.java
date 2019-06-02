package it.polimi.se2019.view.gui;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.view.View;

import java.util.List;

public class GraphicView extends View {
    private MainScreen mMainFrameController;


    public GraphicView(MainScreen mainFrameController) {
        super(new GraphicResponseHandler(), new GraphicUpdateHandler(mainFrameController));
        mMainFrameController = mainFrameController;
    }

    public void showPanel(int panel) {

    }


    @Override
    public void showMessage(String message){

    }

    @Override
    public void reportError(String error){

    }

    @Override
    public void updateBoard(){

    }

    @Override
    public void updatePlayers(){

    }

    @Override
    public void commandAction(String command, String otherCommandPart) {

    }

    @Override
    public Position parseInformationOnDestination(List<Position> pos) {
        return null;
    }

    @Override
    public int parseWeaponInformation(Weapon[] weapons) {
        return 0;
    }

    @Override
    public Integer weaponPlayerController() {
        return null;
    }

    @Override
    public void weaponPlayer() {

    }

    @Override
    public int reloadInteraction(Weapon[] weapons) {
        return 0;
    }

    @Override
    public void easyCommand(String command) {

    }

    @Override
    public void parseCommand(String command) {

    }

    @Override
    public String requestAdditionalInfo() {
        return null;
    }

    @Override
    public void interact(){

    }
}