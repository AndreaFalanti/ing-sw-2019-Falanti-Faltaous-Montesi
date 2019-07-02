package it.polimi.se2019.view.cli;


import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.*;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.network.client.ClientNetworkHandler;
import it.polimi.se2019.network.client.NetworkHandler;
import it.polimi.se2019.util.Observer;
import it.polimi.se2019.view.InitializationInfo;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CLIView extends View {


    private static final String[] COMMAND_ACTION         =  {"move","grab","turn","shoot","reload","reloadshoot","use","back"};
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
    private static final String POWER_UPS                =  "USE" + " and choose the index of the power up" + USE;
    private static final String SHOWG                    =  "SHOWG" + SHOW + " what you can grab";
    private static final String PLAYERS                  =  "PLAYERS" + SHOW + " players";
    private static final String MYINFO                   =  "MYINFO" + SHOW + " your information";
    private static final String WEAPONS                  =  "WEAPONS" + SHOW + " your weapons";
    private static final String BOARD                    =  "BOARD" + SHOW + " the board";
    private static final String KILL                     =  "KILL" + SHOW + " kills and overkills";
    private static final String UNDO                     =  "UNDO" + " to undo the current action" ;
    private static final String BACK                     =  "BACK" + " to go back" ;
    private static final String HELP                     =  "HELP" + SHOW +" available commands";
    private static final String QUIT                     =  "QUIT" + " to quit the game";//to delete is only for test
    private static final Logger logger = Logger.getLogger(CLIView.class.getName());

    private CLIInfo mCLIInfo = null;
    private ClientNetworkHandler networkHandler;
    private Thread t1;

    public void setNetworkHandler(ClientNetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
        t1 = new Thread(() -> ((NetworkHandler)this.networkHandler).startReceivingMessages());
        t1.start();
        this.networkHandler.registerObservablesFromView();

    }


    public ClientNetworkHandler getNetworkHandler(){return networkHandler;}

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

        super(ownerColor, new CLIUpdateHandler(new CLIInfo(info.getPlayers(),ownerColor,info.getActivePlayerColor(),info.getBoard(),info.getTurnNumber(),info.getKills(),
                info.getOverkills())));

        mCLIInfo = ((CLIUpdateHandler) mUpdateHandler).getCLIInfo();
    }


    @Override
    public void reinitialize(InitializationInfo initInfo){
        mOwnerColor = initInfo.getOwnerColor();

        mCLIInfo = new CLIInfo(initInfo.getPlayers(),initInfo.getOwnerColor(), initInfo.getActivePlayerColor(), initInfo.getBoard(), initInfo.getTurnNumber(), initInfo.getKills(),
                initInfo.getOverkills());

        ((CLIUpdateHandler)mUpdateHandler).setUpdateHandlerCLIInfo(mCLIInfo);
        printLineToConsole("reinitialize CLI");

    }

    @Override
    public void registerAll(Observer<Request> observer) {
        register(observer);

    }

    public void actionCommand(){
        printLineToConsole("These are the possible commands:");
        printLineToConsole("\t" + MOVE_ACTION);
        printLineToConsole("\t" + GRAB_ACTION);
        printLineToConsole("\t" + SHOOT_ACTION);
        printLineToConsole("\t" + RELOAD_ACTION);
        printLineToConsole("\t" + RELOADSHOOT_ACTION);
        printLineToConsole("\t" + POWER_UPS);
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
                    if(pos == null){
                        availableCommands();
                        return;
                    }
                    action = new MoveAction(mCLIInfo.getOwnerColor(), pos,true);
                    logger.log(Level.INFO, "Action: MOVE  Pos: {0}", pos);
                    new Thread(() ->  availableCommands()).start();
                    break;
                case "grab":
                    pos = parseDestination(otherCommandPart);
                    if(pos == null){
                        availableCommands();
                        return;
                    }
                    action = new MoveGrabAction(mCLIInfo.getOwnerColor(), pos);
                    logger.log(Level.INFO, "Action: GRAB  Pos: {0}", pos);
                    break;
                case "shoot":
                    pos = parseDestination(otherCommandPart);
                    if(pos == null){
                        availableCommands();
                        return;
                    }
                    index = parseWeaponToAct(true);
                    action = new MoveShootAction(mCLIInfo.getOwnerColor(), pos, index);
                    logger.log(Level.INFO, "Action: SHOOT  Pos: {0} ", pos);
                    break;
                case "use":
                    printLineToConsole(mCLIInfo.getOwner().getPlayerPowerUps());
                    index = parseInteger();
                    notify(new UsePowerUpRequest(index,mCLIInfo.getOwnerColor()));
                   // availableCommands();
                    break;
                case "reloadshoot":
                    pos = parseDestination(otherCommandPart);
                    if(pos == null){
                        availableCommands();
                        return;
                    }
                    int indexReload = parseWeaponToAct(false);
                    index = parseWeaponToAct(true);
                    action = new MoveReloadShootAction(mCLIInfo.getOwnerColor(), pos, indexReload, index);
                    logger.log(Level.INFO, "Action: RELOADSHOOT  Pos: {0}  ", pos);

                    break;
                case "reload":
                    index = parseWeaponToAct(false);
                    action = new ReloadAction(index);
                    logger.log(Level.INFO, "Action: RELOAD  index: {0}", index);
                    break;
                case "turn":
                    notify(new TurnEndRequest(mCLIInfo.getOwnerColor()));
                    availableCommands();
                    break;
                default:
                    availableCommands();
                    break;
            }

        if(!command.equalsIgnoreCase("use")){
            notify(new ActionRequest(action, getOwnerColor()));
        }
   //     }else printLineToConsole("Is not your turn!\n");

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
                if(destination.equalsIgnoreCase("undo")){
                    return null;
                }

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
        for(String weapon: mCLIInfo.getOwner().getWeaponsInfo().keySet())
            printToConsole(weapon + " "+ mCLIInfo.getOwner().getWeaponsInfo().get(weapon));

        return parseInteger();
    }


    public int parseWeaponInformation(TileColor tileColor){
        int index =5;
        printToConsole("Type the index of the weapon you want between 0 and 2\n" +
                mCLIInfo.getSpawnTiles().get(tileColor)+"\n");

        while(index<0 || index>2){
            index = parseInteger();
        }
        return index;
    }

    public int parseWeaponInformation(){
        int index = 5;
        printToConsole("Type the index of the weapon you want select between 0 and 2:\n");
        System.out.println(mCLIInfo.getOwner().getPlayerWeapons());

        while(index<0 || index>2){
            index = parseInteger();
        }
        return index;
    }

    @Override
    public void showPowerUpsDiscardView() {
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
        new Thread(() -> {
            if(spawnColor!=null) {
                int weaponInfo = parseWeaponInformation(spawnColor);
                notify(new WeaponSelectedRequest(weaponInfo, mOwnerColor));
            }
            else
                notify(new WeaponSelectedRequest(parseWeaponInformation(), mOwnerColor));
            availableCommands();
        }).start();
    }

    @Override
    public void showValidPositions(List<Position> positions) {
        printLineToConsole("These are possible positions: ");
        for (Position pos : positions)
            printLineToConsole(pos.toString());
    }

    @Override
    public void showPowerUpSelectionView(List<Integer> indexes) {
       new Thread(() -> {
            List<Integer> powerUps= new ArrayList<>();
            powerUps.add(parseInteger());
            notify(new PowerUpsSelectedRequest(powerUps,mOwnerColor));
       }).start();
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
        try {
            t1.join();

        } catch(InterruptedException e){
            System.out.println("interrupted");
        }
        new Thread(() -> {
            Position pos = selectPosition(possiblePositions);
            if(pos == null)
                return;
            notify(new PositionSelectedRequest(pos, getOwnerColor()));
        }).start();
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
        availableCommands();
    }

    @Override
    public void showRespawnPowerUpDiscardView() {
        try {
            t1.join();

        } catch(InterruptedException e){
            System.out.println("interrupted");
        }
       new Thread(() -> {
            printLineToConsole(mCLIInfo.getOwner().getPlayerPowerUps());
            notify(new RespawnPowerUpRequest(parseInteger(), mOwnerColor));
            availableCommands();
       }).start();

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
            case "kill"       : for(String s : mCLIInfo.getKills())
                                    printToConsole(s);
                                printToConsole("\n");
                                for(String s : mCLIInfo.getOverkills())
                                    printToConsole(s);
                                break;
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
                "Damage: ");
                for(String s: mCLIInfo.getOwner().getPlayerDamage())
                    System.out.print(s);
                System.out.print(SPACE);
                System.out.print("Weapons :   ");
                for(String weapon: mCLIInfo.getOwner().getWeaponsInfo().keySet())
                    printToConsole(weapon + " "+ mCLIInfo.getOwner().getWeaponsInfo().get(weapon));
        System.out.println();
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
                            "Damage: ");
                            for(String s: player.getPlayerDamage())
                                System.out.print(s);
                            System.out.println();
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
        availableCommands();
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
