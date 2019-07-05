package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardCLI {
    public static final int LENGTH = 4;
    public static final int SIZECELL = 29;
    public static final int HIGHCELL = 8;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_BLACK_BACKGROUND ="\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String RED = "RED";
    public static final String YELLOW = "YELLOW";
    public static final String GREEN = "GREEN";
    public static final String BLUE = "BLUE";
    public static final String PURPLE = "PURPLE";
    public static final String WHITE = "WHITE";

    public static final int BOARD_COLUMNS=4;
    public static final int BOARD_ROWS=3;
    private List<StringBuilder> board = new ArrayList<>();
    private boolean thirdNull= false;

    private static void printToConsole(StringBuilder message) {
        System.out.print(message);
    }

    //Constructor
    public BoardCLI(Board realBoard) {
        Tile tile;
        List<Tile> tBoard = new ArrayList<>();
        Tile[] tiles = new Tile[12];
        StringBuilder line0 = new StringBuilder();
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();

        for (int y = 0; y < BOARD_ROWS; y++) {
            for (int x = 0; x < BOARD_COLUMNS; x++) {
                if (realBoard.getTileAt(new Position(x, y)) != null) {
                    tile = realBoard.getTileAt(new Position(x, y));
                    tBoard.add(tile);
                }else tBoard.add(null);
            }
        }

        tBoard.toArray(tiles);


        line0.append(firstLine(tiles));
        line1.append(createFirstAndSecond(0,tiles));
        if(tiles[LENGTH-1] == null)
            line1.replace(line1.lastIndexOf("|\n"),line1.lastIndexOf("\n"),addLastLine());
        line2.append(createFirstAndSecond(LENGTH,tiles));
        line3.append(lastLine(LENGTH *2,tiles));
        if(tiles[LENGTH*2]==null)
            thirdNull=true;

        board.add(line0);
        board.add(line1);
        board.add(line2);
        board.add(line3);

    }


    public String addLastLine(){
        StringBuilder line = new StringBuilder();
        line.append(appendLines(false));
        for(int i = 0 ;i< SIZECELL;i++) {
            line.append(appendLines(true));
        }

        return line.toString();
    }

    /**
     * to colleague two near tiles
     * @param door boolean that indicates if there is a door on east,south
     * @param i an int that indicates actual index on a line
     * @return line created
     */
    public StringBuilder colleague(boolean door, int i){
        StringBuilder line = new StringBuilder();
        boolean spaceDoor = false;
        if(door) {
            for (int j=0 ; j<LENGTH ; j++) {
                if((i>SIZECELL*j+7 && i<SIZECELL*(j+1) -7))
                    spaceDoor = true;
            }

            if(spaceDoor){
                line.append(" ");
                return line;
            }else{
                line.append(appendLines(true));
                return line;
            }
        }
        else{
            if(i== SIZECELL -1 || i== SIZECELL *2-1 || i== SIZECELL *3-1|| i==1 || i== SIZECELL +1 || i== SIZECELL *2+1 || i== SIZECELL *3+1 ||i== SIZECELL *4-1 )
                line.append(appendLines(true));
            else
                line.append(" ");
        }
        return line;
    }

    /**
     *
     * @param horizontal boolean indicates if it is an horizontal line
     * @return a line
     */
    public StringBuilder appendLines(boolean horizontal){
        StringBuilder line= new StringBuilder();
        line.append(ANSI_BLACK);
        if(horizontal)
            line.append("_");
        else
            line.append("|");

        line.append(ANSI_RESET);

        return line;
    }

    /**
     *
     * @param i an int that indicates actual index on a line
     * @param indexTile indicates the first tile on a line
     * @param tiles tiles of the board
     * @return line
     */
    public StringBuilder addSouthernDoors(int i,int indexTile,Tile[] tiles){

        StringBuilder line = new StringBuilder();

        if(indexTile == LENGTH *2) {
            line.append(appendLines(true));
            return line;
        }

        if(tiles[i/ SIZECELL +indexTile]!=null && tiles[i/ SIZECELL + LENGTH +indexTile]!=null ) {

            if (tiles[i/ SIZECELL +indexTile].getColor().equals(tiles[i/ SIZECELL + LENGTH +indexTile].getColor())) {
                line.append(colleague(false,i));
            } else {
                if (tiles[i/ SIZECELL+indexTile].getDoors()[2])
                    line.append(colleague(true,i));
                else
                    line.append(appendLines(true));
            }

        }else {
            line.append(appendLines(true));
        }

        return line;
    }

    public String getColorTile(String color){

        if(color.equalsIgnoreCase(RED) )
            return ANSI_RED_BACKGROUND;
        if(color.equalsIgnoreCase(GREEN))
            return ANSI_GREEN_BACKGROUND;
        if(color.equalsIgnoreCase(BLUE))
            return ANSI_BLUE_BACKGROUND;
        if(color.equalsIgnoreCase(YELLOW))
            return ANSI_YELLOW_BACKGROUND;
        if(color.equalsIgnoreCase(PURPLE))
            return ANSI_PURPLE_BACKGROUND;
        else
            return ANSI_WHITE_BACKGROUND;

    }

    /**
     *
     * @param tiles tile of the real board
     * @return first line of the board
     */
    public StringBuilder firstLine(Tile[] tiles){
        StringBuilder line = new StringBuilder();
        int indexLenght = LENGTH;
        if(tiles[LENGTH -1] == null )
            indexLenght = LENGTH -1;

        for (int i = 0; i <= SIZECELL * indexLenght; i++) {
            line.append(appendLines(true));
        }
        line.append("\n");

        return line;
    }

    /**
     * create first and second row
     * @param indexTile indicates the first tile on a line
     * @param tiles tile of the real board
     * @return first or second row of tile
     */
    public StringBuilder createFirstAndSecond(int indexTile, Tile[] tiles) {
        StringBuilder line = new StringBuilder();

        for (int j = 0; j <= HIGHCELL; j++) {
            for (int i = 0; i <= SIZECELL * LENGTH; i++) {
                if (  tiles[ i/ SIZECELL + indexTile] != null) {
                    if (i < SIZECELL * LENGTH) {
                        line.append(valutation(i,j,indexTile,tiles));
                    }
                    else line.append(makeLastWall());
                }else if(indexTile == LENGTH)
                    line.append(makeLastWall());
            }
        }

        return line;
    }

    /**
     * create last row of tile
     * @param indicates the first tile on a line
     * @param tiles tile of the real board
     * @return last ROW
     */
    public StringBuilder lastLine(int indexTile, Tile[] tiles){
        StringBuilder line = new StringBuilder();
        int indexValutation;
        for (int j = 0; j <= HIGHCELL; j++) {
            for (int i = 0; i <= SIZECELL * LENGTH; i++) {
                indexValutation =i/ SIZECELL +indexTile;
                if(tiles[indexTile] != null || indexValutation!= LENGTH *2 && indexValutation != LENGTH *2 +1){
                    line.append(valutation2(i,j,indexTile,tiles));
                } else {
                    if (i/ SIZECELL + indexTile == LENGTH *2)
                        line.append(" ");
                    else{
                         line.append(valutation3(i,j,indexTile,tiles));
                    }
                }
            }
        }

        return line;
    }


    /**
     *
     * @param i an int that indicates actual index on a line horizontally
     * @param j an int that indicates actual index on a line vertically
     * @param indexTile the first tile on a line
     * @param tiles tile of the real board
     * @return line of row
     */
    public StringBuilder valutation(int i,int j,int indexTile,Tile[] tiles){
        StringBuilder line = new StringBuilder();
        if (tiles[(i-1)/ SIZECELL +indexTile].getColor().getPascalName()
                .equals((tiles[i/ SIZECELL +indexTile].getColor().getPascalName())))
            line.append(sameRoom(i, j, tiles[i/ SIZECELL +indexTile].getColor().getPascalName(),indexTile,tiles));
        else {
            if (tiles[(i-1)/ SIZECELL +indexTile].getDoors()[1])
                line.append(makeDoor(i, j, tiles[i/ SIZECELL +indexTile].getColor().getPascalName()));
            else
                line.append(makeWall(i, tiles[i/ SIZECELL +indexTile].getColor().getPascalName()));
        }

        return line;
    }

    /**
     *valutation for first and second row
     * @param i an int that indicates actual index on a line horizontally
     * @param j an int that indicates actual index on a line vertically
     * @param indexTile the first tile on a line
     * @param tiles tile of the real board
     * @return line of row
     */
    public StringBuilder valutation2(int i,int j,int indexTile,Tile[] tiles){
        StringBuilder line = new StringBuilder();
        if (  i/ SIZECELL + indexTile != tiles.length && tiles[ i/ SIZECELL + indexTile] != null) {
            line.append(valutation(i,j,indexTile,tiles));
        } else {
            if ( i/ SIZECELL + indexTile == tiles.length)
                line.append(makeLastWall());
        }
        return line;
    }

    public StringBuilder valutation3(int i,int j,int indexTile,Tile[] tiles ){
        StringBuilder line = new StringBuilder();

        if(j!=0 &&j% HIGHCELL ==0){
            line.append(getColorTile(tiles[i/ SIZECELL +indexTile].getColor().getPascalName()));
            if(i% SIZECELL ==0)
                line.append(appendLines(false));
            else
                line.append(appendLines(true));

            line.append(ANSI_RESET);
        }
        else line.append(makeWall(i, tiles[i/ SIZECELL +indexTile].getColor().getPascalName()));
        return line;
    }


    public StringBuilder sameRoom(int i,int j,String color,int indexTile,Tile[] tiles){
        StringBuilder line = new StringBuilder();
        line.append(getColorTile(color));
        if(j% HIGHCELL ==0){
            if(i% SIZECELL == 0 )
                line.append(appendLines(false));
            else{
                if(j!=0){

                    line.append(addSouthernDoors(i,indexTile,tiles));
                }
                else
                    line.append(" ");
            }
        }else{
            if(i==0)
                line.append(appendLines(false));
            else
                line.append(" ");
        }
        line.append(ANSI_RESET);
        return line;
    }

    public StringBuilder makeDoor(int i,int j, String color){
        StringBuilder line = new StringBuilder();
        line.append(getColorTile(color));
        if(j!= HIGHCELL /2 && j!= HIGHCELL /2+1 && i% SIZECELL ==0)
            line.append(appendLines(false));
        else
            line.append(" ");
        line.append(ANSI_RESET);

        return line;
    }

    public StringBuilder makeWall(int i, String color){
        StringBuilder line = new StringBuilder();
        line.append(getColorTile(color));
        if(i== SIZECELL * LENGTH)
            line.append("|\n");
        else{
            if(i% SIZECELL ==0)
                line.append(appendLines(false));
            else
                line.append(" ");
        }
        line.append(ANSI_RESET);
        return line;
    }


    public StringBuilder makeLastWall(){
        StringBuilder line = new StringBuilder();

        line.append("|\n");
        line.append(ANSI_RESET);

        return line;
    }


    public void addPlayers(List<StringBuilder> lines, Map<PlayerColor,CLIPlayer> players){
    int numberPlayer = 0;
    StringBuilder firstLineSostitution = new StringBuilder(lines.get(1));
    StringBuilder secondLineSostitution = new StringBuilder(lines.get(2));
    StringBuilder thirdLineSostitution = new StringBuilder(lines.get(3));
        for(CLIPlayer player: players.values()){
            if(!player.getPlayerPosition().equalsIgnoreCase("not respawned")){
                numberPlayer +=2;
                String[] coord = player.getPlayerPosition()
                                .replaceAll("\\D","")
                                .split("");
               if(coord[1].equals("0"))
                        addPlayer(firstLineSostitution,coord[0],player.getPlayerColor(),numberPlayer);
                     else{
                       if(coord[1].equals("1"))
                            addPlayer(secondLineSostitution,coord[0],player.getPlayerColor(),numberPlayer);
                            else{
                                if(!thirdNull)
                                    addPlayer(thirdLineSostitution,coord[0],player.getPlayerColor(),numberPlayer);
                                else
                                    addPlayerLastLine(thirdLineSostitution,coord[0],player.getPlayerColor(),numberPlayer);
                            }
               }
            }
        }
        printToConsole(board.get(0));
        printToConsole(firstLineSostitution);
        printToConsole(secondLineSostitution);
        printToConsole(thirdLineSostitution);
    }

    public static void addPlayer(StringBuilder line , String horizontal,String color,int numberPlayer){
        int index=0;
        int coordX=Integer.parseInt(horizontal);
        for(int i=0;i<HIGHCELL/2 ;i++){
            index = line.indexOf("\n",index);
            index+=1;
        }
        index+=1;

       for(int i=0;i<(SIZECELL*coordX) + numberPlayer ;i++){
            index = line.indexOf(" ",index);
            index +=1;
        }
        index-=1;
        line.replace(index-5,index+1,ANSI_BLACK_BACKGROUND+Colors.findColor(color)+"X"+ANSI_RESET);

    }

    public static void addPlayerLastLine(StringBuilder line , String horizontal,String color,int numberPlayer){
        int index=0;
        int coordX=Integer.parseInt(horizontal);
        for(int i=0;i<HIGHCELL/2 ;i++){
            index = line.indexOf("|\n",index);
            index+=1;
        }

        index+=2;
        index = line.indexOf("|", index);

        for(int i=0;i<(SIZECELL*(coordX-1)) + numberPlayer+5 ;i++){

            index = line.indexOf(" ", index) ;
            index+=1;

        }
        index-=1;
        line.replace(index-5,index+1,ANSI_BLACK_BACKGROUND+Colors.findColor(color)+"X"+ANSI_RESET);


    }
    public List<StringBuilder> getBoardCLI(){
            return board;
        }
}
