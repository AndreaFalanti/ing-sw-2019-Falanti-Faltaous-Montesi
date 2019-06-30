package it.polimi.se2019.view.cli;


import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.*;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.view.InitializationInfo;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CLIView extends View {


    private static final String[] COMMAND_ACTION         =  {"move","grab","shoot","reload","reloadshoot","teleport","tagback","target","back"};
    private static final String[] COMMAND_SIMPLE_REQUEST =  {"myinfo","players","weapons","power","ammo","board","undo","showg","help","quit","back"} ;
    private static final String POSITION_REQUEST_COMMAND =  " and the position where you want " ;
    private static final String PLAYER_TARGET_REQUEST    =  "  the name of target player" ;
    private static final String FIVE_DAMAGE              =  " (if you have taken at least 5 damage points) ," ;
    private static final String SHOW                     =  " to see";
    private static final String USE                      =  " to use";
    private static final String SPACE                    =  "\n\t\t\t";
    private static final String NOWEAPON                 =  "not have weapon";
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

    private CLIInfo mCLIInfo = null;

    private static void printLineToConsole(String message) {
        System.out.println(message);
    }

    private static void printToConsole(String message) {
        System.out.print(message);
    }


    public CLIView(CLIInfo cLIInfo) {
        super(new CLIUpdateHandler(cLIInfo));
        mCLIInfo = cLIInfo;
    }


    public  CLIView(InitializationInfo info,PlayerColor ownerColor){

        super(new CLIUpdateHandler(new CLIInfo(info.getPlayers(),ownerColor,info.getActivePlayerColor(),info.getBoard(),info.getTurnNumber(),info.getKills(),
                info.getOverkills())));

        mCLIInfo = ((CLIUpdateHandler)mUpdateHandler).getCLIInfo();
    }


    @Override
    public void reinitialize(InitializationInfo initInfo){

        printLineToConsole("arrivato");

        mCLIInfo = new CLIInfo(initInfo.getPlayers(),initInfo.getOwnerColor(), initInfo.getActivePlayerColor(), initInfo.getBoard(), initInfo.getTurnNumber(), initInfo.getKills(),
                initInfo.getOverkills());

        ((CLIUpdateHandler)mUpdateHandler).setUpdateHandlerCLIInfo(mCLIInfo);
        printLineToConsole("arrivato");
    }

    public void actionCommand(){
        printLineToConsole("These are the possible commands:");
        printLineToConsole("\t" + MOVE_ACTION);
        printLineToConsole("\t" + GRAB_ACTION);
        printLineToConsole("\t" + SHOOT_ACTION);
        printLineToConsole("\t" + RELOAD_ACTION);
        printLineToConsole("\t" + RELOADSHOOT_ACTION);
        printLineToConsole("\t" + TELEPORT_ACTION);
        // printLineToConsole("\t" + NEWTON_ACTION);
        printLineToConsole("\t" + BACK);
        printLineToConsole("What do you want to do ?");

        interact();
    }

    public void simpleCommand(){
        printLineToConsole("These are the possible commands:");
        printLineToConsole("\t" + PLAYERS);
        printLineToConsole("\t" + SHOWG);//to test
        printLineToConsole("\t" + MYINFO);
        printLineToConsole("\t" + WEAPONS);
        printLineToConsole("\t" + BOARD);
        printLineToConsole("\t" + UNDO);
        printLineToConsole("\t" + HELP);
        printLineToConsole("\t" + BACK);
        printLineToConsole("\t" + QUIT);
        printLineToConsole("What do you want to do ?");
        interact();
    }

    public void availableCommands(){
        printLineToConsole("Choose between ACTION and INFO (or directly the command):");
        interact();
    }

    public void commandAction (String command,String otherCommandPart) {
        Action action = null;
        int index;
        Position pos;
     //   if(mCLIInfo.getActivePlayer().equalsIgnoreCase(mOwnerColor.getPascalName())) {
            switch (command) {
                case "move":
                    pos = parseDestination(otherCommandPart);
                    action = new MoveAction(mCLIInfo.getOwnerColorf(), pos);
                    logger.log(Level.INFO, "Action: MOVE  Pos: {0}", pos);
                    break;
                case "grab":
                    pos = parseDestination(otherCommandPart);
                    action = new MoveGrabAction(mCLIInfo.getOwnerColorf(), pos);
                    logger.log(Level.INFO, "Action: GRAB  Pos: {0}", pos);
                    break;
                case "shoot":
                    pos = parseDestination(otherCommandPart);
                    index = parseWeaponToAct(true);

                    action = new MoveShootAction(mCLIInfo.getOwnerColorf(), pos, index);
                    logger.log(Level.INFO, "Action: SHOOT  Pos: {0} ", pos);
                    break;
                case "teleport":
                    pos = parseDestination(otherCommandPart);
                    printLineToConsole(mCLIInfo.getOwner().getPlayerPowerUps());
                    index = parseInteger();
                    action = new TeleportAction(pos, index);
                    logger.log(Level.INFO, "Action: SHOOT  Pos: {0}", pos);
                    break;
                case "reloadshoot":
                    pos = parseDestination(otherCommandPart);
                    int indexReload = parseWeaponToAct(false);
                    index = parseWeaponToAct(true);
                    action = new MoveReloadShootAction(mCLIInfo.getOwnerColorf(), pos, indexReload, index);
                    logger.log(Level.INFO, "Action: RELOADSHOOT  Pos: {0}  ", pos);
                    break;
                case "reload":
                    index = parseWeaponToAct(false);
                    action = new ReloadAction(index);
                    logger.log(Level.INFO, "Action: RELOAD  index: {0}", index);
                    break;
                default:
                    availableCommands();
                    break;
            }


            notify(new ActionRequest(action, getOwnerColor()));
   //     }else printLineToConsole("Is not your turn!\n");

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

    public int parseWeaponToAct(boolean shoot){
        int index=5;
        List<String> possibleWeapons = new ArrayList<>();

        if(mCLIInfo.getOwner().getPlayerWeapons().get(0).equals(NOWEAPON)){
            printLineToConsole("You don't have weapon");
            return -1;
        }

        printToConsole("Type the index of the weapon you want" );
            if(shoot)
                printToConsole(" use: ");
            else
                printToConsole(" reload: ");
        System.out.println(mCLIInfo.getOwner().getPlayerWeapons());
        for (String weapon : mCLIInfo.getOwner().getWeaponsInfo().keySet()) {
            if(mCLIInfo.getOwner().getWeaponsInfo().get(weapon).equals("load")){
                if(shoot)
                    possibleWeapons.add(weapon);
            }
            else{
                printToConsole("\n"+weapon + " need to load");
                if(!shoot)
                    possibleWeapons.add(weapon);
            }
        }

        return indexWeapon(index,possibleWeapons);
    }

    public int indexWeapon(int index,List possibleWeapons){

        while(index >=mCLIInfo.getOwner().getPlayerWeapons().size() || index<0||
                !possibleWeapons.contains(mCLIInfo.getOwner().getPlayerWeapons().get(index))) {
            index = parseInteger();
            if(index >=mCLIInfo.getOwner().getPlayerWeapons().size() || index<0||
                    !possibleWeapons.contains(mCLIInfo.getOwner().getPlayerWeapons().get(index)))
                printLineToConsole("Invalid index please insert a correct index: ");
        }
        return index;
    }

    public int parseWeaponInformation(TileColor tileColor){

        printToConsole("Type the index of the weapon you want between 0 and 2" +
                mCLIInfo.getSpawnTiles().get(tileColor));

        return parseInteger();
    }

    public int parseWeaponInformation(){


        printToConsole("Type the index of the weapon you want select between 0 and 2:\n");
        System.out.println(mCLIInfo.getOwner().getPlayerWeapons());


        return parseInteger();
    }

    @Override
    public void showPowerUpsDiscardView() {//to discard one on spawn
        boolean[] discarded={false,false,false,false};
        printLineToConsole("Choose one or more power ups to discard:\n" +
                "(example 1 0 1 to discard first and third.\n" +
                " Pay attention every number different from 1 is considered as 0) \n" +
                mCLIInfo.getOwner().getPlayerPowerUps());
        String[] index = (String.valueOf(parseInteger())).split("");

        for (int i = 0; i < index.length; i++){
            if (index[i] != null && index[i].equals("1")) {
                discarded[i]=true;
            }
        }
        notify(new PowerUpDiscardedRequest(discarded, mOwnerColor));
    }

    @Override
    public void showWeaponSelectionView(TileColor spawnColor) {
        if(spawnColor!=null)
            notify(new WeaponSelectedRequest(parseWeaponInformation(spawnColor), mOwnerColor));
        else
            notify(new WeaponSelectedRequest(parseWeaponInformation(), mOwnerColor));
    }

    @Override
    public void showValidPositions(List<Position> positions) {
        printLineToConsole("These are possible positions: ");
        for (Position pos : positions)
            printLineToConsole(pos.toString());
    }

    @Override
    public void showPowerUpSelectionView(List<Integer> indexes) {
        List<Integer> powerUps= new ArrayList<>();
        powerUps.add(parseInteger());
        notify(new PowerUpsSelectedRequest(powerUps,mOwnerColor));
    }

    @Override
    public void showRoomColorSelectionView(Set<TileColor> possibleColors) {
        printLineToConsole("Choose one color from the colo of the tiles");
        System.out.println(mCLIInfo.getTilesColor().values());
        String color = requestAdditionalInfo();
        if(color.equalsIgnoreCase("undo")){
            deleteRequest();
            return;
        }
        for(TileColor tileColor: mCLIInfo.getTilesColor().keySet()){
            if(tileColor.getPascalName().equalsIgnoreCase(color))
                notify(new RoomSelectedRequest(tileColor,mOwnerColor));

        }
    }

    @Override
    public void showDirectionSelectionView() {
        Direction direction = pickDirection();
        if(direction == null)
            return;
        notify(new DirectionSelectedRequest(direction, getOwnerColor()));
    }

    @Override
    public void showPositionSelectionView(Set<Position> possiblePositions) {
        Position pos = selectPosition(possiblePositions);
        if(pos == null)
            return;
        notify(new PositionSelectedRequest(pos, getOwnerColor()));
    }

    @Override
    public void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets) {
        Set<PlayerColor> targets = selectTargets(minToSelect,maxToSelect,possibleTargets);
        if (targets == null)
            return;
        notify(new TargetsSelectedRequest(targets, getOwnerColor()) );
    }

    @Override
    public void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {
        List<String> effects =new ArrayList<>();
        printToConsole("Choose an effect: ");
        for(Effect effect: possibleEffects)
            printToConsole(effect.getName());
        String effectChoosen = requestAdditionalInfo();
        if(effectChoosen.equalsIgnoreCase("undo")){
            deleteRequest();
            return;
        }
        effects.add(effectChoosen);
        notify(new EffectsSelectedRequest(effects,mOwnerColor));
    }

    @Override
    public void showWeaponModeSelectionView(Effect effect1, Effect effect2) {
        printToConsole("Choose one of these effects: "+effect1.getName()+" "+effect2.getName());
        String effect = requestAdditionalInfo();
        notify(new WeaponModeSelectedRequest(effect, getOwnerColor()));
    }

    @Override
    public void showRespawnPowerUpDiscardView() {
        printLineToConsole(mCLIInfo.getOwner().getPlayerPowerUps());
        notify(new RespawnPowerUpRequest(parseInteger(), mOwnerColor));
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


    public void easyCommand(String command){

        switch(command){
            case "players"    : infoPlayers(); break;
            case "showg"      : showGrabbable();break;
            case "weapons"    : System.out.println(mCLIInfo.getOwner().getPlayerWeapons()); break;
            case "myinfo"     : ownerInfo();break;
            case "board"      : mCLIInfo.getBoard().addPlayers(mCLIInfo.getBoard().getBoardCLI(),mCLIInfo.getPlayersInfo()); break;
            case "help"       : break;
            case "back"       : break;
            default           : printLineToConsole("quit");break;//to change
        }
        availableCommands();
    }

    public void ownerInfo(){

        printToConsole(Colors.findColor(mCLIInfo.getOwner().getPlayerColor())+
                mCLIInfo.getOwner().getPlayerName() +
                Colors.ANSI_RESET + ":" + SPACE+
                "Color : "+mCLIInfo.getOwner().getPlayerColor() + SPACE +
                "Ammo : "+mCLIInfo.getOwner().getPlayerAmmo() + SPACE +
                "Marks: " + mCLIInfo.getOwner().getPlayerMarks() + SPACE+
                "Is Dead ? " + mCLIInfo.getOwner().playerIsDead() + SPACE +
                "Scores: " + mCLIInfo.getOwner().getPlayerScore() + SPACE +
                "Position: " + mCLIInfo.getOwner().getPlayerPosition() + SPACE +
                mCLIInfo.getOwner().getPlayerPowerUps() + SPACE +
                "Number of deaths: " + mCLIInfo.getOwner().getPlayerDeaths() + SPACE +
                "Is overkilled ? " + mCLIInfo.getOwner().playerIsOverkilled()+SPACE +
                "Damage: " + mCLIInfo.getOwner().getPlayerDamage()+SPACE +
                "Weapons :   ");
                for(String weapon: mCLIInfo.getOwner().getWeaponsInfo().keySet())
                    printToConsole(weapon + " "+ mCLIInfo.getOwner().getWeaponsInfo().get(weapon));
        System.out.println();
    }

    public void infoPlayers(){
        ownerInfo();
        for(CLIPlayer player: mCLIInfo.getPlayersInfo().values()) {
            if(!player.getPlayerName().equals(mCLIInfo.getOwner().getPlayerName())){
            printToConsole(Colors.findColor(player.getPlayerColor())+
                            player.getPlayerName() +
                            Colors.ANSI_RESET+
                            ":" + SPACE+
                            "Color : "+player.getPlayerColor() + SPACE +
                            "Ammo : "+player.getPlayerAmmo() + SPACE +
                            "Number of deaths: " + player.getPlayerDeaths() + SPACE +
                            "Marks: " + player.getPlayerMarks() + SPACE+
                            "Is Dead ? " + player.playerIsDead() + SPACE +
                            "Scores: " + player.getPlayerScore() + SPACE +
                            "Position: " + player.getPlayerPosition() + SPACE +
                            "Power up: " + player.getPlayerPowerUps() + SPACE +
                            "Number of deaths: " + player.getPlayerDeaths() + SPACE +
                            "Is overkilled ? " + player.playerIsOverkilled()+SPACE +
                            "Weapons : " + player.getPlayerWeapons()+SPACE +
                            "Damage: " + player.getPlayerDamage() + "\n");
            }
        }
    }

    public void showGrabbable(){
        for(Position pos: mCLIInfo.getNormalTiles().keySet()){
            if(mCLIInfo.getNormalTiles().get(pos)!=null)
                printLineToConsole(pos.toString() + " " + mCLIInfo.getNormalTiles().get(pos));
        }
        printLineToConsole("Spawn rooms grabbable:");
        for(TileColor color: mCLIInfo.getSpawnTiles().keySet()){
            printLineToConsole(color.getPascalName()+ " " +mCLIInfo.getSpawnTiles().get(color));
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
        printLineToConsole("Command not available.Write HELP to see available commands");
        interact();
    }

    public Direction pickDirection() {
        printLineToConsole("Choose a direction: north, south, est, west.");
        String direction= "";

        while(!direction.equalsIgnoreCase("north")&&
                !direction.equalsIgnoreCase("south")&&
                !direction.equalsIgnoreCase("west")&&
                !direction.equalsIgnoreCase("east")){

            direction = requestAdditionalInfo();
            if(direction.equalsIgnoreCase("undo")){
                deleteRequest();
                return null;
            }
        }
        switch(direction){
            case "north": return Direction.NORTH;
            case "south": return Direction.SOUTH;
            case "east":return Direction.EAST;
            default: return Direction.WEST;
        }
    }


    public Position selectPosition(Set<Position> possiblePositions) {
        Position[] positions = possiblePositions.toArray(new Position[0]);

        printLineToConsole("Write one of these coordinates");
        for(Position position: positions)
            printLineToConsole(position.toString());

        String destination = requestAdditionalInfo();
        if(destination.equalsIgnoreCase("undo")){
            deleteRequest();
            return null;
        }
        return  parseDestination(destination);
    }

    public Set<PlayerColor> selectTargets(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets) {
        Set<PlayerColor> choosen = new HashSet<>();
        printLineToConsole("Choose " + minToSelect + " to a max of " + maxToSelect + " targets from:\n" +
                "Pay attention will be considered only the name in the limits");
        for (PlayerColor possibleTarget : possibleTargets) {
           printLineToConsole(mCLIInfo.getPlayersInfo().get(possibleTarget).getPlayerName() );
        }
        printLineToConsole("then press enter");
        String target = requestAdditionalInfo();
        if(target.equalsIgnoreCase("undo")){
            deleteRequest();
            return choosen;
        }
        String[] targets = target.split("\\s+");
        while(targets.length<minToSelect ){
            target = requestAdditionalInfo();
            targets = target.split("\\s+");
        }
        for (String s : targets) {
            if(mCLIInfo.colorFromName(s)== null){
                printLineToConsole("\nPlease insert correctly the names");
                return selectTargets(minToSelect,maxToSelect,possibleTargets);
            }
            else{
                choosen.add(mCLIInfo.colorFromName(s));
                if(choosen.size()==maxToSelect)
                    return choosen;
            }
        }
        return choosen;
    }

    public void deleteRequest(){
        notify(new UndoWeaponInteractionRequest(mOwnerColor));
        availableCommands();
    }

    @Override
    public void showMessage(String message){
        printLineToConsole(message);
    }

    @Override
    public void reportError(String error){
        printLineToConsole(error);
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


}
