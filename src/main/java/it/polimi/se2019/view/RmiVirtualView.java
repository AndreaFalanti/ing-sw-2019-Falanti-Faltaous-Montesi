package it.polimi.se2019.view;

import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.UpdateHandler;
import it.polimi.se2019.model.weapon.Weapon;

import java.util.List;

public class RmiVirtualView extends View {
    public RmiVirtualView() {
        super(
                new ResponseHandler() {
                    @Override
                    public void fallbackHandle(Response response) {
                        // TODO: serialize and send
                    }
                },
                new UpdateHandler() {
                    @Override
                    public void fallbackHandle(Update update) {
                        // TODO: serialize and send
                    }
                }
        );
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void reportError(String error) {

    }

    @Override
    public void updateBoard() {

    }

    @Override
    public void updatePlayers() {

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
    public void interact() {

    }
}
