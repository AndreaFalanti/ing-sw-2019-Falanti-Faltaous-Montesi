package it.polimi.se2019.view.cli;


import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.*;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.network.client.ClientNetworkHandler;
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
    private static final String BOARD                    =  "BOARD" + SHOW + " the board";
    private static final String KILL                     =  "KILL" + SHOW + " kills and overkills";
    private static final String UNDO                     =  "UNDO" + " to undo the current action" ;
    private static final String BACK                     =  "BACK" + " to go back" ;
    private static final int damageForMovingBeforeShooting = 6;

    private static final Logger logger = Logger.getLogger(CLIView.class.getName());

    private CLIInfo mCLIInfo = null;
    private ClientNetworkHandler networkHandler;
    private Thread t1;

    public void setNetworkHandler(ClientNetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
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

        ((CLIUpdateHandler)mUpdateHandler).setUpdateHandlerCLIInfo(this,mCLIInfo);
    }

    @Override
    public void confirmEndOfInteraction() {
        new Thread(() -> availableCommands()).start();
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

        new Thread(() -> interact()).start();
    }

    public void simpleCommand(){
        printLineToConsole("These are the possible commands:");
        printLineToConsole("\t" + PLAYERS);
        printLineToConsole("\t" + SHOWG);
        printLineToConsole("\t" + MYINFO);
        printLineToConsole("\t" + BOARD);
        printLineToConsole("\t" + UNDO);
        printLineToConsole("\t" + BACK);
        printLineToConsole("What do you want to do ?");
        new Thread(() -> interact()).start();
    }

    public void availableCommands(){
        printLineToConsole("Choose between ACTION and INFO (or directly the command):");
        new Thread(() -> interact()).start();
    }

    public void commandAction (String command,String otherCommandPart) {
        Action action = null;
        int index;
        Position pos;
            switch (command) {
                case "move":
                    pos = parseDestination(otherCommandPart);
                    if(pos == null){
                        new Thread(() -> availableCommands()).start();
                        return;
                    }
                    action = new MoveAction(mCLIInfo.getOwnerColor(), pos,true);
                    break;
                case "grab":
                    pos = parseDestination(otherCommandPart);
                    if(pos == null){
                        new Thread(() -> availableCommands()).start();
                        return;
                    }
                    action = new MoveGrabAction(mCLIInfo.getOwnerColor(), pos);
                    break;
                case "shoot":
                    index = parseWeaponToAct(true);
                    if(mCLIInfo.getOwner().getValueOfDamage()>=damageForMovingBeforeShooting ||
                        mCLIInfo.getOwner().getBoardFlipped().equalsIgnoreCase("true")){
                        pos = parseDestination(otherCommandPart);
                        if(pos == null){
                            new Thread(() -> availableCommands()).start();
                            return;
                        }
                        action = new MoveShootAction(mCLIInfo.getOwnerColor(), pos, index);
                    }
                    else
                        action = new ShootAction(index);
                    break;
                case "use":
                    for (String playerPowerUp : mCLIInfo.getOwner().getPlayerPowerUps()){
                        printToConsole(playerPowerUp);
                    }
                    index = parseInteger();
                    notify(new UsePowerUpRequest(index,mCLIInfo.getOwnerColor()));
                    break;
                case "reloadshoot":
                    pos = parseDestination(otherCommandPart);
                    if(pos == null){
                        new Thread(() -> availableCommands()).start();
                        return;
                    }
                    int indexReload = parseWeaponToAct(false);
                    index = parseWeaponToAct(true);
                    action = new MoveReloadShootAction(mCLIInfo.getOwnerColor(), pos, indexReload, index);
                    break;
                case "reload":
                    index = parseWeaponToAct(false);
                    action = new ReloadAction(index);
                    break;
                default:
                    new Thread(() -> availableCommands()).start();
                    break;
            }

        if(action!=null&&!command.equalsIgnoreCase("use")&&!command.equalsIgnoreCase("turn")){
            notify(new ActionRequest(action,mCLIInfo.getOwnerColor()));
        }


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
                logger.log(Level.SEVERE,"Uncorrect insertion. Please insert correctly: ");

                destination = requestAdditionalInfo(false);
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
        index = parseInteger();
        while(index<0 || index>2){
            printLineToConsole("Invalid index");
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
       new Thread(() -> {
            boolean[] discarded={false,false,false,false};
            printLineToConsole("Choose one or more power ups to discard:\n" +
                    "(example 1 0 1 to discard first and third.\n" +
                    " Pay attention every number different from 1 is considered as 0) \n" +
                    mCLIInfo.getOwner().getPlayerPowerUps());
            String indexes = requestAdditionalInfo(false);
            String chosen = indexes.replaceAll("\\D","");
            String[] index = chosen.split("");

            for (int i = 0; i < index.length; i++){
                if (index[i] != null && index[i].equals("1")) {
                    discarded[i]=true;
                }
            }
            notify(new PowerUpDiscardedRequest(discarded, mOwnerColor));
       }).start();
    }

    @Override
    public void showWeaponSelectionView(TileColor spawnColor) {
    new Thread(() -> {
                if(spawnColor!=null) {
                    int weaponInfo = parseWeaponInformation(spawnColor);
                    notify(new WeaponSelectedRequest(weaponInfo, mOwnerColor));
                }
                else{

                    notify(new WeaponSelectedRequest(parseWeaponInformation(), mOwnerColor));
                }

        }).start();
    }

    @Override
    public void showValidPositions(List<Position> positions) {
       new Thread(() -> {
            printLineToConsole("These are possible positions: ");
            for (Position pos : positions)
                printLineToConsole(pos.toString());
       }).start();
    }

    @Override
    public void showPowerUpSelectionView(List<Integer> indexes) {

           new Thread(() -> {
                List<Integer> powerUps= new ArrayList<>();
                printToConsole("Indexes: ");
                for (Integer powerUp : indexes) {
                        printToConsole(powerUp+" ");
                }
                printToConsole("\n");
                powerUps.add(parseInteger());
                notify(new PowerUpsSelectedRequest(powerUps,mOwnerColor));
           }).start();
    }

    @Override
    public void showRoomColorSelectionView(Set<TileColor> possibleColors) {
        new Thread(() -> {

            TileColor tileColor = selectTileColor(possibleColors);
            if(tileColor == null)
                return;
            notify(new RoomSelectedRequest(tileColor,mOwnerColor));

        }).start();
    }

    @Override
    public void showDirectionSelectionView() {
       new Thread(() -> {
            Direction direction = pickDirection();
            if(direction == null)
                return;
            notify(new DirectionSelectedRequest(direction, getOwnerColor()));
       }).start();
    }

    @Override
    public void showPositionSelectionView(Set<Position> possiblePositions) {

        new Thread(() -> {
                Position pos = selectPosition(possiblePositions);
                if(pos == null)
                    return;
                notify(new PositionSelectedRequest(pos, getOwnerColor()));
        }).start();

    }

    @Override
    public void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets) {
       new Thread(() -> {
            Set<PlayerColor> targets = selectTargets(minToSelect,maxToSelect,possibleTargets);
            if (targets == null)
                return;
            notify(new TargetsSelectedRequest(targets, getOwnerColor()) );
       }).start();
    }

    @Override
    public void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {
        new Thread(() -> {
            List<String> effects =new ArrayList<>();
            printToConsole("Choose an effect: ");
            for(Effect effect: possibleEffects)
                printToConsole(effect.getId()+" ");
            String effectChoosen = requestAdditionalInfo(true);
            if(effectChoosen.equalsIgnoreCase("undo")){
                deleteRequest();
                return;
            }
            if(effectChoosen.equalsIgnoreCase("")){
                notify(new EffectsSelectedRequest(effects,mCLIInfo.getOwnerColor()));
                return;
            }
            effects.add(effectChoosen);
            notify(new EffectsSelectedRequest(effects,mCLIInfo.getOwnerColor()));
        }).start();
    }

    @Override
    public void showWeaponModeSelectionView(Effect effect1, Effect effect2) {

        new Thread(() -> {
            printLineToConsole("Choose one of these effects: "+effect1.getId()+" "+effect2.getId());
            String effect = requestAdditionalInfo(false);
            if(effect.equalsIgnoreCase("undo")){
                deleteRequest();
                return;
            }
            notify(new WeaponModeSelectedRequest(effect, getOwnerColor()));
        }).start();

    }

    @Override
    public void showRespawnPowerUpDiscardView() {

       new Thread(() -> {
           for (String playerPowerUp : mCLIInfo.getOwner().getPlayerPowerUps()){
               printToConsole(playerPowerUp);
           }
            int index = parseInteger();
            if(!mCLIInfo.getOwner().getPlayerPowerUps().get(index).contains(")Not a card")
                    && mCLIInfo.getActivePlayer() == mCLIInfo.getOwnerColor())
                new Thread(() -> availableCommands()).start();
           notify(new RespawnPowerUpRequest(index, mOwnerColor));
       }).start();

    }

    @Override
    public void showAmmoColorSelectionView(Set<TileColor> possibleColors) {
        new Thread(() -> {
            TileColor chosen = selectTileColor(possibleColors);
            if(chosen ==null)
                return;
            notify(new AmmoColorSelectedRequest(chosen,mCLIInfo.getOwnerColor()));
        }).start();
    }

    public TileColor selectTileColor(Set<TileColor> possibleColors){
        TileColor chosen = null;

        while(chosen == null){
            printToConsole("Choose one of these colors (or undo to go back): ");
            for (TileColor possibleColor : possibleColors) {
                printToConsole(possibleColor +" ");
            }
            String color = requestAdditionalInfo(false);
            if(color.equalsIgnoreCase("undo")){
                deleteRequest();
                return null;
            }
            for (TileColor possibleColor : mCLIInfo.getTilesColor().keySet()) {
                if(color.equalsIgnoreCase(mCLIInfo.getTilesColor().get(possibleColor)))
                    chosen = possibleColor;
            }
        }
        return chosen;
    }

    public int parseInteger(){
        Integer index = null;
        boolean isValid = false;

        do{
            try{
                index = Integer.parseInt(requestAdditionalInfo(false));
                isValid = true;
            }catch(NumberFormatException e){
                System.err.println("Is not a number. Please type correctly:");
            }
        }while(!isValid);

        return index;
    }

    /*
    public int parseIntegerForPowerUp(){
        Integer index = null;
        boolean isValid = false;

        do{
            try{
                index = Integer.parseInt(requestAdditionalInfo());
                isValid = index >= 0 && index < 4;
            }catch(NumberFormatException e){
                System.err.println("Is not a number. Please type correctly:");
            }
        }while(!isValid);

        return index;
    }*/


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
            case "myinfo"     : ownerInfo();break;
            case "board"      : mCLIInfo.getBoard().addPlayers(mCLIInfo.getBoard().getBoardCLI(),mCLIInfo.getPlayersInfo()); break;
            case "help"       : break;
            case "back"       : break;
            default           : ;break;//to change
        }
        new Thread(() -> availableCommands()).start();
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
                "Power Up: "+ mCLIInfo.getOwner().getPlayerPowerUps() + SPACE +
                "Number of deaths: " + mCLIInfo.getOwner().getPlayerDeaths() + SPACE +
                "Is overkilled ? " + mCLIInfo.getOwner().playerIsOverkilled()+SPACE +
                "Damage: ");
                for(String s: mCLIInfo.getOwner().getPlayerDamage())
                    printToConsole(s);
                printToConsole(SPACE);
                printToConsole("Weapons :   ");
                for(String weapon: mCLIInfo.getOwner().getPlayerWeapons())
                    printToConsole(weapon + " "+ mCLIInfo.getOwner().getWeaponsInfo().get(weapon)+" ");
        printToConsole("\n");
        printToConsole("\n");
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
                                printToConsole(s);
                            printToConsole("\n");
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
        printLineToConsole("Choose a direction: north, south, east, west.");
        String direction= "";

        while(!direction.equalsIgnoreCase("north")&&
                !direction.equalsIgnoreCase("south")&&
                !direction.equalsIgnoreCase("west")&&
                !direction.equalsIgnoreCase("east")){

            direction = requestAdditionalInfo(false);
            if(direction.equalsIgnoreCase("undo")){
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

        String destination = requestAdditionalInfo(false);
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
        String target = requestAdditionalInfo(false);
        if(target.equalsIgnoreCase("undo")){
            deleteRequest();
            return new HashSet<>();
        }

        String[] targets = target.split("\\s+");
        while(targets.length<minToSelect ){
            target = requestAdditionalInfo(false);
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
    }

    @Override
    public void showMessage(String message){
        printLineToConsole(message);

    }

    @Override
    public void reportError(String error){
        printLineToConsole(error);
        if(!mCLIInfo.getOwner().getPlayerPosition().equals("not respawned")
            && mCLIInfo.getOwnerColor() == mCLIInfo.getActivePlayer())
            new Thread(() ->  availableCommands()).start();

    }

    public String requestAdditionalInfo(boolean isForShoot){
        Scanner scanner = new Scanner(System.in);

            String command = "" ;
            if(!isForShoot){
                while (command.equals("")) {
                    command = scanner.nextLine();
                }
            }else{
                command=scanner.nextLine();
            }
            return command;

    }

    public void interact(){
        new Thread(() -> {
            String command = requestAdditionalInfo(false);
            parseCommand(command);
        }).start();
    }


}
