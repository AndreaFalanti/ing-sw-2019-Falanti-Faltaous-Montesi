package it.polimi.se2019.view;

import it.polimi.se2019.controller.response.*;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.model.weapon.response.TargetSelectionResponse;
import it.polimi.se2019.view.request.Request;

import java.util.List;

public class RmiVirtualView extends View {
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

    @Override
    public void update(Update update) {

    }

    @Override
    public void handle(LeaderboardResponse response) {
    }

    @Override
    public void handle(MessageResponse response) {
    }

    @Override
    public void handle(PickWeaponResponse response) {
    }

    @Override
    public void handle(ValidMoveResponse response) {
    }

    @Override
    public void handle(MessageActionResponse response) {
    }

    @Override
    public void handle(TargetSelectionResponse response) {

    }
}
