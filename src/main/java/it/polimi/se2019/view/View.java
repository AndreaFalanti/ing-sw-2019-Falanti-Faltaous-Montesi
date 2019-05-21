package it.polimi.se2019.view;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.util.Observable;
import it.polimi.se2019.util.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class View implements Observable, Observer {

    protected ArrayList<Player> mPlayers;
    private Board board;
    protected PlayerColor activePlayer;
    protected Player owner;

    public abstract void showMessage(String message);

    public abstract void reportError(String error);

    public abstract void updateBoard();

    public abstract void updatePlayers();

    public abstract void commandAction (String command,String otherCommandPart);//to distinguish and make request for an action

    public abstract Position parseInformationOnDestination(List<Position> pos);//used from controller to request more info on destination

    public abstract int parseWeaponInformation(Weapon[] weapons);// the weapon index that you want to grab

    public abstract Integer weaponPlayerController();// the weapon index of the weapon that you want exchange

    public abstract void weaponPlayer();//to see weapon of player

    public abstract int reloadInteraction(Weapon[] weapons);//to choose the weapon that you want reload

    public abstract void easyCommand(String command);//command as print Players name

    public abstract void parseCommand(String command);//to distinguish the type of command

    public abstract String requestAdditionalInfo();//used to request more information about action



    public abstract void interact();//used to parse the command

    public void notifyController() {
        // TODO: implement
    }

    public PlayerColor getActivePlayer() {

        return null;
    }

    public List<Player> getPlayers(){
        return mPlayers;
    }

}