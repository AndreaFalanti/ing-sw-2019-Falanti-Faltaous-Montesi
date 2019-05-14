package it.polimi.se2019.view;

import java.util.Scanner;


public class CLIView extends View {

    public static final String COMMAND_PREFIX           = "Write : " ;
    public static final String POSITION_REQUEST_COMMAND = "\tand the position where you want " ;
    public static final String PLAYER_TARGET_REQUEST    = "\tor the name of target player" ;
    public static final String SHOW                     = "\tto see";
    public static final String MOVE_ACTION              = COMMAND_PREFIX + "move" +  POSITION_REQUEST_COMMAND  + "\tto move";
    public static final String GRAB_AMMO_ACTION         = COMMAND_PREFIX + "grab ammo" + POSITION_REQUEST_COMMAND + "\tto grab";
    public static final String GRAB_WEAPON_ACTION       = COMMAND_PREFIX + "grab weapon" + POSITION_REQUEST_COMMAND  + "\tto grab";
    public static final String SHOOT_ACTION             = COMMAND_PREFIX + "shoot" + POSITION_REQUEST_COMMAND + PLAYER_TARGET_REQUEST + "\tto shoot";
    public static final String LEADERBOARD              = COMMAND_PREFIX + "leaderboard" + SHOW + "\tthe leaderboard";
    public static final String PLAYERS                  = COMMAND_PREFIX + "players" + SHOW + "\tplayers";
    public static final String WEAPON                   = COMMAND_PREFIX + "weapon" + SHOW + "\tyour weapons";
    public static final String AMMO                     = COMMAND_PREFIX + "ammo" + SHOW + "\tyour ammo";
    public static final String UNDO                     = COMMAND_PREFIX + "undo" + "\tto undo the current action" ;
    public static final String HELP                     = COMMAND_PREFIX + "help" + SHOW +"\tcommands";
    public static final String QUIT                     = COMMAND_PREFIX + "quit" + "\tto quit the game";//to delete is only for test
    private Scanner scanner;
    private String command;


    public CLIView() {

    }

    public void availableCommands(){
        System.out.println("These are the possible commands:");
        System.out.println("\t" + MOVE_ACTION);
        System.out.println("\t" + GRAB_AMMO_ACTION);
        System.out.println("\t" + GRAB_WEAPON_ACTION);
        System.out.println("\t" + SHOOT_ACTION);
        System.out.println("\t" + LEADERBOARD);
        System.out.println("\t" + PLAYERS);
        System.out.println("\t" + WEAPON);
        System.out.println("\t" + AMMO);
        System.out.println("\t" + UNDO);
        System.out.println("\t" + HELP);
        System.out.println("\t" + QUIT);
    }

    public void parseCommand(String command) {
        this.command = command;
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

    }

}