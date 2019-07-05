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
import java.util.concurrent.TimeUnit;

public class CLIView extends View {


    private static final String[] COMMAND_ACTION         =  {"move","grab","turn","shoot","reload","reloadshoot","use","back"};
    private static final String[] COMMAND_SIMPLE_REQUEST =  {"myinfo","players","board","undo","showg","back"} ;
    private static final String POSITION_REQUEST_COMMAND =  " and the position where you want " ;
    private static final String PLAYER_TARGET_REQUEST    =  "  the name of target player" ;
    private static final String SIX_DAMAGE               =  " (if you have taken at least 6 damage points) ," ;
    private static final String SHOW                     =  " to see";
    private static final String USE                      =  " to use";
    private static final String SPACE                    =  "\n\t\t\t";
    private static final String NOWEAPON                 =  "not have weapon";
    private static final String MOVE_ACTION              =  "MOVE" + POSITION_REQUEST_COMMAND  + "to move";
    private static final String GRAB_ACTION              =  "GRAB " + POSITION_REQUEST_COMMAND + "to grab";
    private static final String SHOOT_ACTION             =  "SHOOT" + POSITION_REQUEST_COMMAND + SIX_DAMAGE + PLAYER_TARGET_REQUEST + "to shoot";
    private static final String RELOAD_ACTION            =  "RELOAD" +  " and choose the index (from zero to two) a weapon to reload ";
    private static final String RELOADSHOOT_ACTION       =  "RELOADSHOOT" + "(in frenzy mode and if you are before first player) to move, reload and shoot ";
    private static final String POWER_UPS                =  "USE" + " and choose the index of the power up" + USE;
    private static final String TURN                     =  "TURN" + "to pass your turn";
    private static final String SHOWG                    =  "SHOWG" + SHOW + " what you can grab";
    private static final String PLAYERS                  =  "PLAYERS" + SHOW + " players";
    private static final String MYINFO                   =  "MYINFO" + SHOW + " your information";
    private static final String BOARD                    =  "BOARD" + SHOW + " the board";
    private static final String KILL                     =  "KILL" + SHOW + " kills and overkills";
    private static final String UNDO                     =  "UNDO" + " to undo the current action" ;
    private static final String BACK                     =  "BACK" + " to go back" ;
    private static final int DAMAGE_FOR_MOVING = 6;


    private CLIInfo mCLIInfo = null;
    private ClientNetworkHandler networkHandler;


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
        availableCommands();
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
        printLineToConsole("\t" + TURN);
        printLineToConsole("\t" + BACK);
        printLineToConsole("What do you want to do ?");

        interact();
    }

    public void simpleCommand(){
        printLineToConsole("These are the possible commands:");
        printLineToConsole("\t" + PLAYERS);
        printLineToConsole("\t" + SHOWG);
        printLineToConsole("\t" + MYINFO);
        printLineToConsole("\t" + BOARD);
        printLineToConsole("\t" + KILL);
        printLineToConsole("\t" + UNDO);
        printLineToConsole("\t" + BACK);
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
        if((mCLIInfo.getActivePlayer()==mCLIInfo.getOwnerColor())){
            switch (command) {
                case "move":
                    pos = parseDestination(otherCommandPart);
                    if (onUndoPos(pos)) {
                        return;
                    }
                    action = new MoveAction(mCLIInfo.getOwnerColor(), pos, true);
                    break;
                case "grab":
                    pos = parseDestination(otherCommandPart);
                    if (onUndoPos(pos)) {
                        return;
                    }
                    action = new MoveGrabAction(mCLIInfo.getOwnerColor(), pos);
                    break;
                case "shoot":
                    commandActionShoot(command, otherCommandPart);
                    break;
                case "use":
                    for (String playerPowerUp : mCLIInfo.getOwner().getPlayerPowerUps()) {
                        printToConsole(playerPowerUp);
                    }
                    String powerUp = parseIndex(false);
                    if (powerUp.equalsIgnoreCase("undo"))
                        availableCommands();
                    else {
                        index = Integer.parseInt(powerUp);
                        notify(new UsePowerUpRequest(index, mCLIInfo.getOwnerColor()));
                    }
                    break;
                case "reloadshoot":
                    commandActionShoot(command, otherCommandPart);
                    break;
                case "reload":
                    String chosen = parseIndex(false);
                    if (chosen.equalsIgnoreCase("undo"))
                        availableCommands();
                    else {
                        index = Integer.parseInt(chosen);
                        action = new ReloadAction(index);
                    }
                    break;
                case "turn":
                    notify(new TurnEndRequest(mCLIInfo.getOwnerColor()));
                    availableCommands();
                    break;
                default:
                    availableCommands();
                    break;
            }

            if (action != null) {
                notify(new ActionRequest(action, mCLIInfo.getOwnerColor()));
            }
        } else availableCommands();

    }

    public void commandActionShoot(String command,String otherCommandPart){
        int index;
        String chosen = "";
        Position pos;
        Action action;

        if(command.equalsIgnoreCase("shoot")){
            chosen = parseWeaponToAct(true);
            if(chosen.equalsIgnoreCase("undo")){
                availableCommands();
                return;
            }
            if (canMoveBeforeShooting()) {
                pos = parseDestination(otherCommandPart);
                if (onUndoPos(pos)) {
                    return;
                }
                action = new MoveShootAction(mCLIInfo.getOwnerColor(), pos, Integer.parseInt(chosen));
            } else
                action = new ShootAction(Integer.parseInt(chosen));
        }else{
            if(mCLIInfo.getOwner().getBoardFlipped().equals("true")){
                pos = parseDestination(otherCommandPart);
                if(onUndoPos(pos)){
                    return;
                }
                String weaponToReload = parseWeaponToAct(false);
                if(weaponToReload.equalsIgnoreCase("undo")){
                    availableCommands();
                    return;
                }
                String weaponShoot = parseWeaponToAct(true);
                if(weaponShoot.equalsIgnoreCase("undo")){
                    availableCommands();
                    return;
                }
                action = new MoveReloadShootAction(mCLIInfo.getOwnerColor(), pos, Integer.parseInt(weaponToReload), Integer.parseInt(weaponShoot));
            }else{
                printToConsole("Not in frenzy \n");
                availableCommands();
                return;
            }
        }
            notify(new ActionRequest(action,mCLIInfo.getOwnerColor()));//vedere aggiungere ava
    }

    public boolean canMoveBeforeShooting(){
        return mCLIInfo.getOwner().getValueOfDamage()>= DAMAGE_FOR_MOVING ||
                mCLIInfo.getOwner().getBoardFlipped().equalsIgnoreCase("true");
    }

    public boolean onUndoPos(Position pos){
        if(pos == null){
            availableCommands();
            return true;
        }
        return false;
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
                printLineToConsole("Uncorrect insertion. Please insert correctly: ");

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

    public String parseWeaponToAct(boolean shoot){

        if(mCLIInfo.getOwner().getPlayerWeapons().isEmpty()){
            printLineToConsole("You don't have weapon");
            availableCommands();
            return "5";
        }

        printToConsole("Type the index of the weapon you want" );
            if(shoot)
                printToConsole(" use: ");
            else
                printToConsole(" reload: ");
        for(String weapon: mCLIInfo.getOwner().getWeaponsInfo().keySet())
            printToConsole(weapon + " "+ mCLIInfo.getOwner().getWeaponsInfo().get(weapon));

        return parseIndex(false);
    }


    public String parseWeaponInformation(TileColor tileColor){
        int index = 5;
        String chosen = "";
        printToConsole("Type the index of the weapon you want starting from 0:\n" +
                mCLIInfo.getSpawnTiles().get(tileColor)+"\n");
        chosen = chooseWeapon();
        return chosen;
    }

    public String parseWeaponInformation(){
        int index = 5;
        String chosen = "";
        printToConsole("Type the index of the weapon you want select starting from 0:\n");
        System.out.println(mCLIInfo.getOwner().getPlayerWeapons());

        chosen = chooseWeapon();

        return chosen;
    }

    public String chooseWeapon(){
        int index = 5;
        String chosen = "";
        chosen = parseIndex(false);
        if(chosen.equalsIgnoreCase("undo")){
            new Thread(() -> availableCommands()).start();
            return "no_execution";
        }
        else
            index = Integer.parseInt(chosen);

        while(index<0 || index>2){
            printToConsole("Not valid.Try again:  ");
            chosen = parseIndex(false);
            if(chosen.equalsIgnoreCase("undo")){
                new Thread(() -> availableCommands()).start();
                return "no_execution";
            }
            else
                index = Integer.parseInt(chosen);
        }
        return chosen;
    }



    public String parseIndex(boolean canBeEmpty){
        String chosen = null;
        Integer index = null;
        boolean isValid = false;
        if(canBeEmpty){
            do{
                try{
                    chosen = requestAdditionalInfo(canBeEmpty);
                    if(chosen.equals(""))
                        return chosen;
                    Integer.parseInt(chosen);
                    isValid = true;
                }catch(NumberFormatException e){
                    printLineToConsole("Is not a number. Please type correctly:");
                }
            }while(!isValid);
        } else{
            do{
                try{
                    chosen = requestAdditionalInfo(canBeEmpty);
                    if(chosen.equals("undo"))
                        return chosen;
                    Integer.parseInt(chosen);
                    isValid = true;
                }catch(NumberFormatException e){
                    printLineToConsole("Is not a number. Please type correctly:");
                }
            }while(!isValid);
        }
        return chosen;
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

            int maxIndex;
            if(index.length>4)
                maxIndex=4;
            else
                maxIndex=index.length;

            for (int i = 0; i < maxIndex; i++){
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
                    String chosen = parseWeaponInformation(spawnColor);
                    if(chosen.equals("no_execution"))
                        return;
                    notify(new WeaponSelectedRequest(Integer.parseInt(chosen), mOwnerColor));
                }
                else{
                    String chosen = parseWeaponInformation();
                    if(chosen.equals("no_execution"))
                        return;
                    notify(new WeaponSelectedRequest(Integer.parseInt(chosen), mOwnerColor));
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
                        String powerUp = parseIndex(true);
                        if(powerUp.equals(""))
                            notify(new PowerUpsSelectedRequest(powerUps,mCLIInfo.getOwnerColor()));
                        else{
                            powerUps.add(Integer.parseInt(powerUp));
                            notify(new PowerUpsSelectedRequest(powerUps,mCLIInfo.getOwnerColor()));
                        }
                   });


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
                availableCommands();
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
        int i =0;
        do{
            try{
                String chosen = requestAdditionalInfo(false);
                    if(chosen ==null )
                       for(String s :mCLIInfo.getOwner().getPlayerPowerUps()){
                           if(!s.contains("Not")){
                               index = i;
                               break;
                           }
                           i++;
                       }

                    else
                        index = Integer.parseInt(chosen);
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
            case "myinfo"     : ownerInfo();break;
            case "board"      : mCLIInfo.getBoard().addPlayers(mCLIInfo.getBoard().getBoardCLI(),mCLIInfo.getPlayersInfo()); break;
            case "undo"       : break;

            case "back"       : break;
            default           : break;//to change
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

        if(command == null){

            return;
        }
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
    //    if(!mCLIInfo.getOwner().getPlayerPosition().equals("not respawned")
      //      && mCLIInfo.getOwnerColor() == mCLIInfo.getActivePlayer())
            availableCommands();

    }


    public String requestAdditionalInfo(boolean isForShoot){

       try{
           printLineToConsole("write:");
            return new Reader(
                    90,
                    3000,
                    TimeUnit.MILLISECONDS
            ).readLine();

        }catch (InterruptedException e){}

        return "";
    }

    public void interact(){

            new Thread(() -> {
                String command = requestAdditionalInfo(false);
                parseCommand(command);

            }).start();
    }


}
