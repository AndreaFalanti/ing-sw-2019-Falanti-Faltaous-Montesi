package it.polimi.se2019.view;

import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.UpdateHandler;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

public class SocketVirtualView extends View {
    public SocketVirtualView() {
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
    public int reloadInteraction() {
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
    public Direction pickDirection() {
        return null;
    }

    @Override
    public Set<Position> selectPositions(int minToSelect, int maxToSelect, Set<Position> possiblePositions) {
        return null;
    }

    @Override
    public Set<PlayerColor> selectTargets(int possibleTargets, int minToSelect, Set<PlayerColor> maxToSelect) {
        return null;
    }

    @Override
    public Set<String> selectEffects(SortedMap<Integer, Set<Effect>> priorityMap, int currentPriority) {
        return null;
    }

    @Override
    public void interact() {

    }
}
