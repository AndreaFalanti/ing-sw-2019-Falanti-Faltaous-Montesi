package it.polimi.se2019.view.cli;


import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.*;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.view.View;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class CLIView extends View {


    private static final String[] COMMAND_ACTION        = {"move","grab","shoot","reload","reloadshoot","teleport","tagback","target"};
    private static final String[] COMMAND_SIMPLE_REQUEST = {"myinfo","players","weapons","power","ammo","board","undo","help","quit"} ;
    private static final String POSITION_REQUEST_COMMAND = " and the position where you want " ;
    private static final String PLAYER_TARGET_REQUEST    = "  the name of target player" ;
    private static final String FIVE_DAMAGE              = " (if you have taken at least 5 damage points) ," ;
    private static final String SHOW                     = " to see";
    private static final String USE                      = " to use";
    private static final String SPACE                    =  "\n\t\t\t";
    private static final String MOVE_ACTION              =  "MOVE" + POSITION_REQUEST_COMMAND  + "to move";
    private static final String GRAB_ACTION              =  "GRAB " + POSITION_REQUEST_COMMAND + "to grab";
    private static final String SHOOT_ACTION             =  "SHOOT" + POSITION_REQUEST_COMMAND + FIVE_DAMAGE + PLAYER_TARGET_REQUEST + "to shoot";
    private static final String RELOAD_ACTION            =  "RELOAD" +  " and choose the index (from zero to two) a weapon to reload ";
    private static final String RELOADSHOOT_ACTION       =  "RELOADSHOOT" + "(in frenzy mode and if you are before first player) to move, reload and shoot ";
    private static final String TELEPORT_ACTION          =  "TELEPORT" + POSITION_REQUEST_COMMAND  + USE;
   // private static final String NEWTON_ACTION            =  "NEWTON" + " when another player damages you" + USE;
    private static final String PLAYERS                  =  "PLAYERS" + SHOW + " players";
    private static final String MYINFO                   =  "MYINFO" + SHOW + " your information";
    private static final String WEAPONS                  =  "WEAPONS" + SHOW + " your weapons";
    private static final String POWERUPS                 =  "POWER" + SHOW + " your power ups";
    private static final String AMMO                     =  "AMMO" + SHOW + " your ammo";
    private static final String BOARD                    =  "BOARD" + SHOW + " the board";
    private static final String UNDO                     =  "UNDO" + " to undo the current action" ;
    private static final String HELP                     =  "HELP" + SHOW +" available commands";
    private static final String QUIT                     =  "QUIT" + " to quit the game";//to delete is only for test
    private static final Logger logger = Logger.getLogger(CLIView.class.getName());

    private CLIInfo mCLIInfo;


    public CLIView(CLIInfo cLIInfo) {
        super(new CLIResponseHandler(), new CLIUpdateHandler(cLIInfo));
        mCLIInfo = cLIInfo;
    }

    public void availableCommands(){
        System.out.println("These are the possible commands:");
        System.out.println("\t" + MOVE_ACTION);
        System.out.println("\t" + GRAB_ACTION);
        System.out.println("\t" + SHOOT_ACTION);
        System.out.println("\t" + RELOAD_ACTION);
        System.out.println("\t" + RELOADSHOOT_ACTION);
        System.out.println("\t" + TELEPORT_ACTION);
       // System.out.println("\t" + NEWTON_ACTION);
        System.out.println("\t" + PLAYERS);
        System.out.println("\t" + MYINFO);
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
        Position pos;

//        PlayerColor ownerColor = owner.getColor();//<-- to add
        switch (command) {
            case "move":
                pos=parseDestination(otherCommandPart);
                action = new MoveAction(mCLIInfo.getOwnerColorf(),pos);
                logger.log(Level.INFO,"Action: MOVE  Pos: {0}",pos);
                break;
            case "grab":
                pos=parseDestination(otherCommandPart);
                action = new MoveGrabAction(mCLIInfo.getOwnerColorf(), pos);
             //   new GrabRequest();//<--to add request
                logger.log(Level.INFO,"Action: GRAB  Pos: {0}",pos);
                break;
            case "shoot":
                pos=parseDestination(otherCommandPart);
                action = new MoveShootAction(mCLIInfo.getOwnerColorf(), pos , 1);// TODO: to complete
                logger.log(Level.INFO,"Action: SHOOT  Pos: {0}",pos);
                break;
            case "teleport":
                pos=parseDestination(otherCommandPart);
                powerPlayer();
               // index = ();<--- to complete
                action = new TeleportAction(pos,0);//<--to change
                logger.log(Level.INFO,"Action: SHOOT  Pos: {0}",pos);
                break;
            //case "newtown":
            //    pos=parseDestination(otherCommandPart);
            //    action = new NewtoAction();
            //    break;
            case "reloadshoot":
                pos=parseDestination(otherCommandPart);
                index = reloadInteraction();
                action = new MoveReloadShootAction(mCLIInfo.getOwnerColorf(), pos, 1, index);  // TODO
                logger.log(Level.INFO,"Action: RELOADSHOOT  Pos: {0}  ",pos );
                break;
            default :
                index = reloadInteraction();
                action = new ReloadAction(index);
                logger.log(Level.INFO,"Action: RELOAD  index: {0}",index);
                break;
        }



        availableCommands();
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

  //      while(coord[0].equals("") ){
  //          System.out.println("Insert coord:");
   //         destination = requestAdditionalInfo();
   //         coordTogether = destination.replaceAll("\\D","");
   //         coord = coordTogether.split("");
   //     }

        while(!isValid){
            try {
                pos = new Position(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));

                isValid = true;

            } catch (NumberFormatException|ArrayIndexOutOfBoundsException e) {
                logger.log(Level.SEVERE,"Uncorrect insertion. Please insert coord: ");

                destination = requestAdditionalInfo();
                coordTogether = destination.replaceAll("\\D","");
                coord = coordTogether.split("");

            }
        }

        return pos;
    }

    @Override
    public int parseWeaponInformation(Weapon[] weapons){
        Integer index = null;
        boolean isValid = false;

        System.out.print("Type the index of the weapon you want ");
        for(Weapon weapon : weapons)
            System.out.print(weapon.getName());

        do{
            try{
                index = Integer.parseInt(requestAdditionalInfo());
                isValid = true;
            }catch(NumberFormatException e){
                System.err.println("Is not a number. Please type the index of the weapon that you want");
            }
        }while(!isValid);

        return index;
    }

    @Override
    public Integer weaponPlayerController(){
        Integer index = null;
        boolean isValid = false;

        System.out.print("Type the index of the weapon you want exchange");
        weaponPlayer();

        do{
            try{
                index = Integer.parseInt(requestAdditionalInfo());
                isValid = true;
            }catch(NumberFormatException e){
                System.err.println("Is not a number. Please type the index of the weapon that you want exchange");
            }
        }while(!isValid);

        return index;
    }

    @Override
    public void weaponPlayer(){
      //  for(Weapon weapon : mCLIInfo.getOwner().getWeapons()) {
      //      System.out.print(weapon.getName() + " ");
     //       if(!weapon.isLoaded())
    //            System.out.println("<----to reload");
      //  }
        System.out.println(mCLIInfo.getOwner().getPlayerWeapons());
        System.out.print("\n");
    }

    @Override
    public int reloadInteraction(){//Weapon[] weapon
        int index= -1;
        boolean isValid = false;

        System.out.println("Choose the index of a weapon to reload :" );
        weaponPlayer();
        do {
            try {
                index = Integer.parseInt(requestAdditionalInfo());
                if(index<3 && index>=0)
                    isValid = true;
                else
                    System.out.println("Please insert a number between 0 and 2");
            }catch(NumberFormatException e) {
                System.err.println("Incorrect entry.Which weapon do you want to reload?");
            }
        }
        while(!isValid);

        return index;
    }

    @Override
    public void easyCommand(String command){

        switch(command){
            case "players"    : infoPlayers();availableCommands(); break;
            case "weapons"    : weaponPlayer();availableCommands(); break;
            case "power"      : powerPlayer(); availableCommands(); break;
            case "myinfo"     : System.out.println("Your color is " + mCLIInfo.getOwnerColor());
                                powerPlayer();
                                weaponPlayer();
                                System.out.println( mCLIInfo.getOwner().getPlayerAmmo() +
                                                    "\n" + mCLIInfo.getOwner().getPlayerMarks() +//to add score and position
                                                    "\n" + mCLIInfo.getOwner().getPlayerDamage());
                                 availableCommands();
                                break;
            case "ammo"       : System.out.println(mCLIInfo.getOwner().getPlayerAmmo()); availableCommands();break;
            case "board"      : availableCommands(); break;
            case "undo"       : deleteRequest(); availableCommands(); break;
            case "help"       : availableCommands(); break;
            default           : System.out.println("quit");break;//to change
        }
    }

    public void infoPlayers(){

        for(CLIPlayer player: mCLIInfo.getPlayers()) {
            System.out.print(player.getPlayerName() + ":" + SPACE+
                            "Number of deaths: " + player.getPlayerDeaths() + SPACE +
                            "Marks: " + player.getPlayerMarks() + SPACE+
                            "Is Dead ? " + player.playerIsDead() + SPACE +
                            "Scores: " + player.getPlayerScore() + SPACE +
                            "Position: " + player.getPlayerPosition() + SPACE +
                            "Is overkilled ? " + player.playerIsOverkilled()+SPACE);
            System.out.print( "Weapons : ");
            if (mCLIInfo.getOwner().getPlayerName().equals(player.getPlayerName()))
                weaponPlayer();
            else{
                weaponOtherPlayer(player.getPlayerWeapons());
            }
            System.out.print(SPACE);
            System.out.print("Damage: ");
            System.out.println(player.getPlayerDamage());
            System.out.print(SPACE);
        }
    }

    public void weaponOtherPlayer(String weapons){
                System.out.print(weapons);
    }

    public void damagePlayers(PlayerColor[] damage){
    int count=0;

        if (damage == null){
            System.out.print("This player hasn't been damaged yet.");
            return;
        }
        System.out.println("First damage from: " + otherPlayerNameFromColor(damage[0]) );
        int size = Arrays.asList(damage).size();
        List<PlayerColor> colorPlayerThatDamage =  Arrays.stream(damage)
                                                   .distinct()
                                                   .collect(Collectors.toList());
        for (PlayerColor playerColor : colorPlayerThatDamage) {
            count = 0;
           if(playerColor!=null){
                for (int i = 0; i < size; i++) {
                    if (damage[i]!=null && playerColor.equals(damage[i]))
                        count += 1;
                }

                System.out.print(SPACE);
                System.out.print(count +" damage from: "+ otherPlayerNameFromColor(playerColor));
            }
        }

    }

    public String otherPlayerNameFromColor(PlayerColor playerc){
        String target = null ;

        for (CLIPlayer player : mCLIInfo.getPlayers()) {
            if(player.getPlayerColor().equals(playerc.getPascalName() ))
                target = player.getPlayerName();
        }

        return target;
    }


    public void powerPlayer(){
        System.out.println(mCLIInfo.getOwner().getPlayerPowerUps());

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

        System.out.println("Command not available.Write HELP to see available commands");
        interact();
    }

    @Override
    public String requestAdditionalInfo(){
        Scanner scanner = new Scanner(System.in);
        String command = "" ;
        while (command.equals("")) {
            command = scanner.nextLine();
        }
        return command;
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
    public void interact(){
        String command = requestAdditionalInfo();
        parseCommand(command);

    }

    public String getActiveP(){
        return mCLIInfo.getActivePlayer();
    }

    public String getOwnerC(){
        return mCLIInfo.getOwnerColor();
    }


}
