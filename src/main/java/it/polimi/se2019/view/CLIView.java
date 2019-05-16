package it.polimi.se2019.view;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.MoveAction;
import it.polimi.se2019.model.action.MoveGrabAction;
import it.polimi.se2019.model.action.MoveShootAction;
import it.polimi.se2019.view.requests.LeaderboardRequest;

import java.util.Scanner;


public class CLIView extends View {

    public static final String COMMAND_PREFIX           = "Write : " ;
    public static final String POSITION_REQUEST_COMMAND = " and the position where you want " ;
    public static final String PLAYER_TARGET_REQUEST    = " or the name of target player" ;
    public static final String SHOW                     = " to see";
    public static final String USE                      = " to use";
    public static final String MOVE_ACTION              = COMMAND_PREFIX + "move" +  POSITION_REQUEST_COMMAND  + " to move";
    public static final String GRAB_ACTION              = COMMAND_PREFIX + "grab ammo" + POSITION_REQUEST_COMMAND + " to grab";
    public static final String SHOOT_ACTION             = COMMAND_PREFIX + "shoot" + POSITION_REQUEST_COMMAND + PLAYER_TARGET_REQUEST + " to shoot";
    public static final String TELEPORT_ACTION          = COMMAND_PREFIX + "teleport" + POSITION_REQUEST_COMMAND  + USE;
    public static final String TAGBACKGRANADE_ACTION    = COMMAND_PREFIX + "tagback" + PLAYER_TARGET_REQUEST + USE;
    public static final String TARGETSCOPE_ACTION       = COMMAND_PREFIX + "target" + PLAYER_TARGET_REQUEST + USE;
    public static final String LEADERBOARD              = COMMAND_PREFIX + "leaderboard" + SHOW + " the leaderboard";
    public static final String PLAYERS                  = COMMAND_PREFIX + "players" + SHOW + " players";
    public static final String WEAPONS                  = COMMAND_PREFIX + "weapons" + SHOW + " your weapons";
    public static final String POWERUPS                 = COMMAND_PREFIX + "power" + SHOW + " your power ups";
    public static final String AMMO                     = COMMAND_PREFIX + "ammo" + SHOW + " your ammo";
    public static final String BOARD                    = COMMAND_PREFIX + "board" + SHOW + " the board";
    public static final String UNDO                     = COMMAND_PREFIX + "undo" + " to undo the current action" ;
    public static final String HELP                     = COMMAND_PREFIX + "help" + SHOW +" available commands";
    public static final String QUIT                     = COMMAND_PREFIX + "quit" + " to quit the game";//to delete is only for test
    private Scanner scanner;
    private String command;


    public CLIView() {

    }

    public void availableCommands(){
        System.out.println("These are the possible commands:");
        System.out.println("\t" + MOVE_ACTION);
        System.out.println("\t" + GRAB_ACTION);
        System.out.println("\t" + SHOOT_ACTION);
        System.out.println("\t" + LEADERBOARD);
        System.out.println("\t" + PLAYERS);
        System.out.println("\t" + WEAPONS);
        System.out.println("\t" + POWERUPS);
        System.out.println("\t" + BOARD);
        System.out.println("\t" + AMMO);
        System.out.println("\t" + UNDO);
        System.out.println("\t" + HELP);
        System.out.println("\t" + QUIT);
    }

    public void interpretate(){

    }

    public void parseCommand(String command) {
        int size,x,y;
        Action action = null;
        Position position = new Position(0,0);
        PlayerColor ownerColor = owner.getColor();

        command = command.toLowerCase();
        String[] compCommand = command.split(" ");
        size = compCommand.length;
        if(size == 3){
            try {
                x = Integer.parseInt(compCommand[1]);
                y = Integer.parseInt(compCommand[2]);
                position = new Position(x,y);
            }catch(NumberFormatException e){
                System.err.println("Something Wrong!");
            }
        }
        if(compCommand[0].equals("move")){
            action = new MoveAction(ownerColor,position);
        } else {
            if(compCommand[0].equals("grab")){
                action = new MoveGrabAction(ownerColor,position);
            } else {
                if(compCommand[0].equals("shoot")){
                    action = new MoveShootAction(ownerColor,position);
                } else {
                    if(compCommand[0].equals("leaderboard")){
                        new LeaderboardRequest();
                    } else {
                        if(compCommand[0].equals("players")){
                            System.out.println(mPlayers);
                        } else {
                            if(compCommand[0].equals("weapons")){
                                System.out.println(owner.getWeapons().toString());
                            } else {
                                if(compCommand[0].equals("ammo")){
                                    System.out.println(owner.getAmmo());
                                } else {
                                    if(compCommand[0].equals("board")){
                                        //TODO
                                    } else {
                                        if(compCommand[0].equals("undo")){
                                            //TODO
                                        } else {
                                            if(compCommand[0].equals("help")){
                                                availableCommands();
                                            } else {
                                                if (compCommand[0].equals("quit")) {
                                                    System.out.println("quit");//to change
                                                } else {
                                                    System.out.println("Command not found. " + HELP);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        return;
                    }
                }
            }
        }
        new ActionMessage(action);
        notifyController();
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
        System.out.println("What do you want to do ?");
        scanner = new Scanner(System.in);
        parseCommand(scanner.toString());
    }

 //   public static void main(String[] args){
  //      System.out.println("Choose your name : ");
   //     while (owner.getColor() == activePlayer)
    //}


}