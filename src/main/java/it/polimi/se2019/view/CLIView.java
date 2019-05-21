package it.polimi.se2019.view;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.*;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.view.requests.LeaderboardRequest;

import java.util.List;
import java.util.Scanner;


public class CLIView extends View {

    protected static final String[] COMMAND_ACTION        = {"move","grab","shoot","reload","reloadshoot","teleport","tagback","target"};
    protected static final String[] COMMAND_SIMPLE_REQUEST = {"leaderboard","players","weapons","power","ammo","board","undo","help","quit"} ;
    public static final String COMMAND_PREFIX           = "Write : " ;
    public static final String POSITION_REQUEST_COMMAND = " and the position where you want " ;
    public static final String PLAYER_TARGET_REQUEST    = "  the name of target player" ;
    public static final String FIVE_DAMAGE              = " (if you have taken at least 5 damage points) ," ;
    public static final String SHOW                     = " to see";
    public static final String USE                      = " to use";
    public static final String MOVE_ACTION              = COMMAND_PREFIX + "move" + POSITION_REQUEST_COMMAND  + " to move";
    public static final String GRAB_ACTION              = COMMAND_PREFIX + "grab " + POSITION_REQUEST_COMMAND + " to grab";
    public static final String SHOOT_ACTION             = COMMAND_PREFIX + "shoot" + POSITION_REQUEST_COMMAND + FIVE_DAMAGE + PLAYER_TARGET_REQUEST + " to shoot";
    public static final String RELOAD_ACTION            = COMMAND_PREFIX + "reload" +  " and choose the index (from zero to two) a weapon to reload ";
    public static final String RELOADSHOOT_ACTION       = COMMAND_PREFIX + "reloadshoot" + "(in frenzy mode and if you are before first player) to move, reload and shoot ";
    public static final String TELEPORT_ACTION          = COMMAND_PREFIX + "teleport" + POSITION_REQUEST_COMMAND  + USE;
    public static final String TAGBACKGRANADE_ACTION    = COMMAND_PREFIX + "tagback" + " when another player damages you " + USE;
    public static final String TARGETSCOPE_ACTION       = COMMAND_PREFIX + "target" + "and a player that you are hurting " + USE;
    public static final String LEADERBOARD              = COMMAND_PREFIX + "leaderboard" + SHOW + " the leaderboard";
    public static final String PLAYERS                  = COMMAND_PREFIX + "players" + SHOW + " players";
    public static final String WEAPONS                  = COMMAND_PREFIX + "weapons" + SHOW + " your weapons";
    public static final String POWERUPS                 = COMMAND_PREFIX + "power" + SHOW + " your power ups";
    public static final String AMMO                     = COMMAND_PREFIX + "ammo" + SHOW + " your ammo";
    public static final String BOARD                    = COMMAND_PREFIX + "board" + SHOW + " the board";
    public static final String UNDO                     = COMMAND_PREFIX + "undo" + " to undo the current action" ;
    public static final String HELP                     = COMMAND_PREFIX + "help" + SHOW +" available commands";
    public static final String QUIT                     = COMMAND_PREFIX + "quit" + " to quit the game";//to delete is only for test


    public CLIView() {

    }


    public void availableCommands(){
        System.out.println("These are the possible commands:");
        System.out.println("\t" + MOVE_ACTION);
        System.out.println("\t" + GRAB_ACTION);
        System.out.println("\t" + SHOOT_ACTION);
        System.out.println("\t" + RELOAD_ACTION);
        System.out.println("\t" + RELOADSHOOT_ACTION);
        System.out.println("\t" + TELEPORT_ACTION);
        System.out.println("\t" + TAGBACKGRANADE_ACTION);
        System.out.println("\t" + TARGETSCOPE_ACTION);
        System.out.println("\t" + LEADERBOARD);
        System.out.println("\t" + PLAYERS);
        System.out.println("\t" + WEAPONS);
        System.out.println("\t" + POWERUPS);
        System.out.println("\t" + BOARD);
        System.out.println("\t" + AMMO);
        System.out.println("\t" + UNDO);
        System.out.println("\t" + HELP);
        System.out.println("\t" + QUIT);
        System.out.println("What do you want to do ?");

        interact();
    }

    @Override
    public void commandAction (String command,String otherCommandPart) {
        Action action = null;
        int index;


        PlayerColor ownerColor = owner.getColor();
        switch (command) {
            case "move":
                action = new MoveAction(ownerColor,parseDestination(otherCommandPart));
                break;
            case "grab":
                action = new MoveGrabAction(ownerColor, parseDestination(otherCommandPart));
                break;
            case "shoot":
                action = new MoveShootAction(ownerColor, parseDestination(otherCommandPart));// to complete
                break;
            case "reloadshoot":
                index = reloadInteraction(owner.getWeapons());
                action = new MoveReloadShootAction(ownerColor, parseDestination(otherCommandPart), index);//<----tochange
                break;
            default:
                index = reloadInteraction(owner.getWeapons());
                action = new ReloadAction(index);
                break;
        }
        new ActionMessage(action);
        notifyController();
    }

    @Override
    public Position parseInformationOnDestination(List<Position> pos){
        Scanner scanner = null;

        System.out.println("Write one of these coordinates");
        for(Position position: pos)
            System.out.println(position.getX()+" "+position.getY());
        String destination = requestAdditionalInfo();

        return  parseDestination(destination);
    }


    public Position parseDestination(String destination){
        Position pos = null;
        boolean isValid = false ;

        String coordTogether = destination.replaceAll("\\D","");
        String[] coord = coordTogether.split("");

        if(coord[0].equals("") || coord.length < 2){
            return new Position(-1,-1);
        }

        do {
            try {
                pos = new Position(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
                isValid = true;
            } catch (NumberFormatException e) {
                System.err.println("Uncorrect insertion. Please insert coord: ");
            }
        } while(!isValid);

        return pos;
    }

    @Override
    public int parseWeaponInformation(Weapon[] weapons){
        int index;
        boolean isValid = true;

        System.out.print("Type the index of the weapon you want ");
        for(Weapon weapon : weapons)
            System.out.print(weapon.getName());

        do{
            try{
                Integer.parseInt(requestAdditionalInfo());
                isValid = false;
            }catch(NumberFormatException e){
                System.err.println("non va");
            }
        }while(isValid);

        return  Integer.parseInt(requestAdditionalInfo());//throws exception if is not a number
    }

    @Override
    public Integer weaponPlayerController(){
        System.out.print("Type the index of the weapon you want exchange");
        weaponPlayer();
        try {
            return Integer.parseInt(requestAdditionalInfo());//throws exception if is not a number
        }catch(NumberFormatException e){
            return Integer.parseInt(requestAdditionalInfo());//throws exception if is not a number
        }
    }

    @Override
    public void weaponPlayer(){
        for(Weapon weapon : owner.getWeapons())
            System.out.print(weapon.getName());
    }

    @Override
    public int reloadInteraction(Weapon[] weapons){
        int index= -1;

        System.out.println("Choose the index of a weapon to reload :" );
        for(Weapon weapon : weapons){
            System.out.print(weapon);
            if(!weapon.isLoaded())
                System.out.println("<-- to load");
        }
        Scanner scanner = new Scanner(System.in);
        try {
            index = scanner.nextInt();
            return index;
        }catch(NumberFormatException e) {
            System.err.println("Incorrect entry");
        }
        return index;
    }

    @Override
    public void easyCommand(String command){

        switch(command){
            case "leaderboard": new LeaderboardRequest();  break;
            case "players"    : System.out.println(mPlayers); break;// to complete
            case "weapons"    : weaponPlayer(); break;
            case "ammo"       : System.out.println(owner.getAmmo()); break;
            case "board"      : break;
            case "undo"       : deleteRequest();  break;
            case "help"       : availableCommands(); break;
            default           : System.out.println("quit");break;//to change
        }
    }

    @Override
    public void parseCommand(String command) {

        command = command.toLowerCase();
        String[] compCommand = command.split(" ");


        for(String string : COMMAND_ACTION)
            if(string.equals(compCommand[0])) {
                commandAction(compCommand[0],command);
                return;
            }
        for(String string : COMMAND_SIMPLE_REQUEST)
            if(string.equals(compCommand[0])) {
                easyCommand(compCommand[0]);
                return;
            }

        System.out.println("Command not available."+ HELP);
    }

    @Override
    public String requestAdditionalInfo(){
        Scanner scanner = new Scanner(System.in);
        String command = "" ;
        while (!command.equals("quit")) {
            command = scanner.nextLine();
        }
        return command;
    }



    public void deleteRequest(){

    }

    public void ownerCLI(){

    }

    @Override
    public void showMessage(String message){
        System.out.println(message);
    }

    @Override
    public void reportError(String error){
        System.out.println(error);
    }

    @Override
    public void updateBoard(){

    }

    @Override
    public void updatePlayers(){

    }

    @Override
    public void interact(){
        String command = requestAdditionalInfo();
        parseCommand(command);

    }

 //   public static void main(String[] args){
  //      System.out.println("Choose your name : ");
   //     while (owner.getColor() == activePlayer)
    //}


}