package it.polimi.se2019.view.cli;


import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.*;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CLIView extends View {


    private static final String[] COMMAND_ACTION        = {"move","grab","shoot","reload","reloadshoot","teleport","tagback","target","back"};
    private static final String[] COMMAND_SIMPLE_REQUEST = {"myinfo","players","weapons","power","ammo","board","undo","help","quit","back"} ;
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
    private static final String SHOWG                    =  "SHOWG" + SHOW + " what you can grab";
    private static final String PLAYERS                  =  "PLAYERS" + SHOW + " players";
    private static final String MYINFO                   =  "MYINFO" + SHOW + " your information";
    private static final String WEAPONS                  =  "WEAPONS" + SHOW + " your weapons";
    private static final String BOARD                    =  "BOARD" + SHOW + " the board";
    private static final String UNDO                     =  "UNDO" + " to undo the current action" ;
    private static final String BACK                     =  "BACK" + " to go back" ;
    private static final String HELP                     =  "HELP" + SHOW +" available commands";
    private static final String QUIT                     =  "QUIT" + " to quit the game";//to delete is only for test
    private static final Logger logger = Logger.getLogger(CLIView.class.getName());

    private CLIInfo mCLIInfo;


    public CLIView(CLIInfo cLIInfo) {
        super(new CLIUpdateHandler(cLIInfo));
        mCLIInfo = cLIInfo;
    }

    public void actionCommand(){
        System.out.println("These are the possible commands:");
        System.out.println("\t" + MOVE_ACTION);
        System.out.println("\t" + GRAB_ACTION);
        System.out.println("\t" + SHOOT_ACTION);
        System.out.println("\t" + RELOAD_ACTION);
        System.out.println("\t" + RELOADSHOOT_ACTION);
        System.out.println("\t" + TELEPORT_ACTION);
        // System.out.println("\t" + NEWTON_ACTION);
        System.out.println("\t" + BACK);
        System.out.println("What do you want to do ?");

        interact();
    }

    public void simpleCommand(){
        System.out.println("These are the possible commands:");
        System.out.println("\t" + PLAYERS);
        System.out.println("\t" + SHOWG);//to test
        System.out.println("\t" + MYINFO);
        System.out.println("\t" + WEAPONS);
        System.out.println("\t" + BOARD);
        System.out.println("\t" + UNDO);
        System.out.println("\t" + HELP);
        System.out.println("\t" + BACK);
        System.out.println("\t" + QUIT);
        System.out.println("What do you want to do ?");
        interact();
    }

    public void availableCommands(){
        System.out.println("Choose between; ACTION and INFO (or directly the command):");
        interact();
    }

    public void commandAction (String command,String otherCommandPart) {
        Action action = null;
        int index;
        Position pos;

        switch (command) {
            case "move":
                pos=parseDestination(otherCommandPart);
                action = new MoveAction(mCLIInfo.getOwnerColorf(),pos);
                logger.log(Level.INFO,"Action: MOVE  Pos: {0}",pos);
                break;
            case "grab":
                pos=parseDestination(otherCommandPart);
                action = new MoveGrabAction(mCLIInfo.getOwnerColorf(), pos);
                logger.log(Level.INFO,"Action: GRAB  Pos: {0}",pos);
                break;
            case "shoot":
                pos=parseDestination(otherCommandPart);
                action = new MoveShootAction(mCLIInfo.getOwnerColorf(), pos , 1);// TODO: to complete
                logger.log(Level.INFO,"Action: SHOOT  Pos: {0}",pos);
                break;
            case "teleport":
                pos=parseDestination(otherCommandPart);
                System.out.println(mCLIInfo.getOwner().getPlayerPowerUps());
               // index = ();<--- to complete
                action = new TeleportAction(pos,0);//<--to change
                logger.log(Level.INFO,"Action: SHOOT  Pos: {0}",pos);
                break;
            case "reloadshoot":
                pos=parseDestination(otherCommandPart);
                index = reloadInteraction();
                action = new MoveReloadShootAction(mCLIInfo.getOwnerColorf(), pos, 1, index);  // TODO
                logger.log(Level.INFO,"Action: RELOADSHOOT  Pos: {0}  ",pos );
                break;
            case "reload" :
                index = reloadInteraction();
                action = new ReloadAction(index);
                logger.log(Level.INFO,"Action: RELOAD  index: {0}",index);
                break;
            default:
                availableCommands();
                break;
        }


        notify(new ActionRequest(action,this));
        availableCommands();
    }

    public Position parseDestination(String destination){
        Position pos = null;
        boolean isValid = false ;

        String coordTogether = destination.replaceAll("\\D","");
        String[] coord = coordTogether.split("");

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

    public int parseWeaponInformation(TileColor tileColor){
        int index = 3;

        System.out.print("Type the index of the weapon you want between 0 and 2" +
                mCLIInfo.getSpawnTiles().get(tileColor));
        while(index >=3 || index<0) {
            index = parseInteger();
            if(index >=3 || index<0)
                System.out.println("Invalid index please insert a correct index: ");
        }
        return index;
    }

    public int parseWeaponInformation(){
        int index = 4;

        System.out.print("Type the index of the weapon you want exchange");
        System.out.println(mCLIInfo.getOwner().getPlayerWeapons());
        String[] weapons =  mCLIInfo.getOwner().getPlayerWeapons().split(",");
        while(index >=weapons.length || index<0) {
            index = parseInteger();
            if(index >=weapons.length || index<0)
                System.out.println("Invalid index please insert a correct index: ");
        }

        return index;
    }

    @Override
    public void showPowerUpsDiscardView() {
        boolean[] discarded={false,false,false,false};
        System.out.println("Choose one or more power ups to discard:\n" +
                "(example 1 0 1 to discard first and third.\n" +
                " Pay attention every number different from 1 is considered as 0) \n" +
                mCLIInfo.getOwner().getPlayerPowerUps());
        String[] index = (String.valueOf(parseInteger())).split("");

        for (int i = 0; i < index.length; i++){
            if (index[i] != null && index[i].equals("1")) {
                discarded[i]=true;
            }
        }
        notify(new PowerUpDiscardedRequest(discarded,this));
    }

    @Override
    public void showWeaponSelectionView(TileColor spawnColor) {
        if(spawnColor!=null)
            notify(new WeaponSelectedRequest(parseWeaponInformation(spawnColor),this));
        else
            notify(new WeaponSelectedRequest(parseWeaponInformation(),this));
    }

    @Override
    public void showValidPositions(List<Position> positions) {
        System.out.println("These are possible positions: ");
        for (Position pos : positions)
            System.out.println(pos.toString());
    }

    @Override
    public void showDirectionSelectionView() {
        notify(new DirectionSelectedRequest(pickDirection(),this));
    }

    @Override
    public void showPositionSelectionView(Set<Position> possiblePositions) {
        notify(new PositionSelectedRequest(selectPosition(possiblePositions),this));
    }

    @Override
    public void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets) {
        notify(new TargetsSelectedRequest(selectTargets(minToSelect,maxToSelect,possibleTargets),this) );
    }

    @Override
    public void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap, int currentPriority) {

    }

    public int parseInteger(){
        Integer index = null;
        boolean isValid = false;

        do{
            try{
                index = Integer.parseInt(requestAdditionalInfo());
                isValid = true;
            }catch(NumberFormatException e){
                System.err.println("Is not a number. Please type correctly:");
            }
        }while(!isValid);

        return index;
    }

    public int reloadInteraction(){//Weapon[] weapon
        int index= -1;
        boolean isValid = false;

        System.out.println("Choose the index of a weapon to reload :" );
        System.out.println(mCLIInfo.getOwner().getPlayerWeapons());
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

    public void easyCommand(String command){

        switch(command){
            case "players"    : infoPlayers();availableCommands(); break;
            case "showg"      : showGrabbable();break;
            case "weapons"    : System.out.println(mCLIInfo.getOwner().getPlayerWeapons());availableCommands(); break;
            case "myinfo"     : ownerInfo();availableCommands();break;
            case "board"      : mCLIInfo.getBoard().addPlayers(mCLIInfo.getBoard().getBoardCLI(),mCLIInfo.getPlayersInfo());availableCommands(); break;
            case "undo"       : deleteRequest(); availableCommands(); break;
            case "help"       : availableCommands(); break;
            case "break"      : availableCommands();break;
            default           : System.out.println("quit");break;//to change
        }
    }

    public void ownerInfo(){

        System.out.print(Colors.getColorTile(mCLIInfo.getOwner().getPlayerColor())+
                mCLIInfo.getOwner().getPlayerName() +
                Colors.ANSI_RESET + ":" + SPACE+
                "Color : "+mCLIInfo.getOwner().getPlayerColor() + SPACE +
                "Marks: " + mCLIInfo.getOwner().getPlayerMarks() + SPACE+
                "Is Dead ? " + mCLIInfo.getOwner().playerIsDead() + SPACE +
                "Scores: " + mCLIInfo.getOwner().getPlayerScore() + SPACE +
                "Position: " + mCLIInfo.getOwner().getPlayerPosition() + SPACE +
                mCLIInfo.getOwner().getPlayerPowerUps() + SPACE +
                "Number of deaths: " + mCLIInfo.getOwner().getPlayerDeaths() + SPACE +
                "Is overkilled ? " + mCLIInfo.getOwner().playerIsOverkilled()+SPACE +
                "Weapons : " + mCLIInfo.getOwner().getPlayerWeapons()+SPACE +
                "Damage: " + mCLIInfo.getOwner().getPlayerDamage() + SPACE);
    }

    public void infoPlayers(){
        ownerInfo();
        for(CLIPlayer player: mCLIInfo.getPlayersInfo().values()) {
            if(!player.getPlayerName().equals(mCLIInfo.getOwner().getPlayerName())){
            System.out.print(Colors.getColorTile(player.getPlayerColor())+
                            player.getPlayerName() +
                            Colors.ANSI_RESET+
                            ":" + SPACE+
                            "Color : "+player.getPlayerColor() + SPACE +
                            "Number of deaths: " + player.getPlayerDeaths() + SPACE +
                            "Marks: " + player.getPlayerMarks() + SPACE+
                            "Is Dead ? " + player.playerIsDead() + SPACE +
                            "Scores: " + player.getPlayerScore() + SPACE +
                            "Position: " + player.getPlayerPosition() + SPACE +
                            "Power up: " + player.getPlayerPowerUps() + SPACE +
                            "Number of deaths: " + player.getPlayerDeaths() + SPACE +
                            "Is overkilled ? " + player.playerIsOverkilled()+SPACE +
                            "Weapons : " + player.getPlayerWeapons()+SPACE +
                            "Damage: " + player.getPlayerDamage() + SPACE);
            }
        }
    }

    public void showGrabbable(){
        for(Position pos: mCLIInfo.getNormalTiles().keySet()){
            System.out.println(pos.toString() + " " + mCLIInfo.getNormalTiles().get(pos));
        }
        System.out.println("Spawn rooms grabbable:");
        for(TileColor color: mCLIInfo.getSpawnTiles().keySet()){
            System.out.println(color.getPascalName()+" " +mCLIInfo.getSpawnTiles().get(color));
        }
    }

    public void parseCommand(String command) {

        command = command.toLowerCase();
        String[] compCommand = command.split(" ");

        if(compCommand[0].equalsIgnoreCase("action")){
            actionCommand();
            return;
        }

        if(compCommand[0].equalsIgnoreCase("info")){
            simpleCommand();
            return;
        }

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

    public Direction pickDirection() {
        System.out.println("Choose a direction: north, sud, est, ovest.");
        String direciton= "";

        while(!direciton.equalsIgnoreCase("north")&&
                !direciton.equalsIgnoreCase("south")&&
                !direciton.equalsIgnoreCase("west")&&
                !direciton.equalsIgnoreCase("east")){

            direciton = requestAdditionalInfo();
        }

        switch(direciton){
            case "north": return Direction.NORTH;
            case "south": return Direction.SOUTH;
            case "east":return Direction.EAST;
            default: return Direction.WEST;
        }
    }


    public Position selectPosition(Set<Position> possiblePositions) {
        Position[] positions = possiblePositions.toArray(new Position[0]);

        System.out.println("Write one of these coordinates");
        for(Position position: positions)
            System.out.println(position.toString());

        String destination = requestAdditionalInfo();
        return  parseDestination(destination);
    }


    public Set<PlayerColor> selectTargets(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets) {
        Set<PlayerColor> choosen = new HashSet<>();
        System.out.println("Choose " + minToSelect + " to a max of " + maxToSelect + " targets from:\n" +
                "Pay attention will be considered only the name in the limits\n");
        for (PlayerColor possibleTarget : possibleTargets) {
           System.out.println(mCLIInfo.getPlayersInfo().get(possibleTarget).getPlayerName() + " ");
        }
        System.out.println(" then press enter");
        String target = requestAdditionalInfo();
        String[] targets = target.split("\\s+");
        while(targets.length<=minToSelect){
            target = requestAdditionalInfo();
            targets = target.split("\\s+");
        }
        for (String s : targets) {
            if(mCLIInfo.colorFromName(s)== null){
                System.out.println("Please insert correctly the names");
                return selectTargets(minToSelect,maxToSelect,possibleTargets);
            }
            else
                choosen.add(mCLIInfo.colorFromName(s));
        }

        return choosen;
    }


    public Set<String> selectEffects(SortedMap<Integer, Set<Effect>> priorityMap, int currentPriority) {
        return null;
    }


    public void deleteRequest(){

    }

    @Override
    public void showMessage(String message){
        System.out.println(message);
    }

    @Override
    public void reportError(String error){
        System.out.println(error);
    }

    public String requestAdditionalInfo(){
        Scanner scanner = new Scanner(System.in);
        String command = "" ;
        while (command.equals("")) {
            command = scanner.nextLine();
        }
        return command;
    }

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
