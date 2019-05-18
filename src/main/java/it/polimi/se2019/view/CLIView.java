package it.polimi.se2019.view;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.*;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.view.requests.LeaderboardRequest;

import java.util.Arrays;
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

        interact();
    }


    public void commandAction (String command,Scanner in) {
        Action action = null;
        int index;


        PlayerColor ownerColor = owner.getColor();
        switch (command) {
            case "move":
                action = new MoveAction(ownerColor,parseDestination(in));
                break;
            case "grab":
                action = new MoveGrabAction(ownerColor, parseDestination(in));
                break;
            case "shoot":
                action = new MoveShootAction(ownerColor, parseDestination(in));
                break;
            case "reloadshoot":
                index = reloadInteraction(owner.getWeapons());
                action = new MoveReloadShootAction(ownerColor, parseDestination(in), index);
                break;
            default:
                index = reloadInteraction(owner.getWeapons());
                action = new ReloadAction(index);
                break;
        }
        new ActionMessage(action);
        notifyController();
    }

    public Position parseInformationOnDestination(Position[] pos){
        Scanner scanner = null;

        System.out.println("Write one of these coordinates" + Arrays.toString(pos));
        while (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return  parseDestination(scanner);
    }

    public Position parseDestination(Scanner coord){
        Integer x = null;
        Integer y = null;

        while(coord.hasNext() && y == null){
            if(coord.hasNextInt() && x==null)
                x = coord.nextInt();
            else if (coord.hasNextInt())
                y = coord.nextInt();
        }

        if(x != null && y != null)
            return new Position(x,y);
        else
            return new Position(-1,-1);

    }

    public int parseWeaponInformation(Weapon[] weapons){

        System.out.println("Type the index of the weapon you want" + Arrays.toString(weapons));
        //to complete
        return 0;
    }

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

    public void easyCommand(String command){

        switch(command){
            case "leaderboard": new LeaderboardRequest();  break;
            case "players"    : System.out.println(mPlayers); break;
            case "weapons"    : System.out.println(Arrays.toString(owner.getWeapons())); break;
            case "ammo"       : System.out.println(owner.getAmmo()); break;
            case "board"      : break;
            case "undo"       : deleteRequest();  break;
            case "help"       : availableCommands(); break;
            default           : System.out.println("quit");break;//to change
        }
    }

    public void parseCommand(Scanner in) {

        String command = in.toString();
        command = command.toLowerCase();
        String[] compCommand = command.split(" ");


        for(String string : COMMAND_ACTION)
            if(string.equals(compCommand[0])) {
                commandAction(compCommand[0],in);
                return;
            }
        for(String string : COMMAND_SIMPLE_REQUEST)
            if(string.equals(compCommand[0])) {
                easyCommand(compCommand[0]);
                return;
            }

        System.out.println("Command not available."+ HELP);
        interact();
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
        Scanner scanner = null;
        while (scanner == null) {
            System.out.println("What do you want to do ?");
            scanner = new Scanner(System.in);
        }
        parseCommand(scanner);
    }

 //   public static void main(String[] args){
  //      System.out.println("Choose your name : ");
   //     while (owner.getColor() == activePlayer)
    //}


}