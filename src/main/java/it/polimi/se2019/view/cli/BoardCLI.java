package it.polimi.se2019.view.cli;

import java.util.ArrayList;
import java.util.List;

public class BoardCLI {
    public static final int LENGTH = 4;
    public static final int SIZECELL = 29;
    public static final int HIGHCELL = 8;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND ="\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_BRED_BACKGROUND = "\u001B[41;1m";
    public static final String ANSI_BGREEN_BACKGROUND = "\u001B[42;1m";
    public static final String ANSI_BYELLOW_BACKGROUND = "\u001B[43;1m";
    public static final String ANSI_BBLUE_BACKGROUND = "\u001B[44;1m";
    public static final String ANSI_BPURPLE_BACKGROUND = "\u001B[45;1m";
    public static final String ANSI_BCYAN_BACKGROUND = "\u001B[46;1m";
    public static final String color1 =  "RED";
    public static final String color4 = "YELLOW";
    public static final String color2 = "GREEN";
    public static final String color3 = "BLUE";
    public static final String color5 = "WHITE";
    public static final testTile tile0 = new testTile(color3, false, true, true, true);
    public static final testTile tile1 = new testTile(color3, false, false, false, true);
    public static final testTile tile2 = new testTile(color3, false, true, true, true);
    public static final testTile tile3 =null;
    public static final testTile tile4 = new testTile(color1, false, false, false, true);
    public static final testTile tile5 = new testTile(color1, false, true, false, true);
    public static final testTile tile6 = new testTile(color1, false, false, true, true);
    public static final testTile tile7 = new testTile(color4, false, true, true, true);
    public static final testTile tile8 =  null;
    public static final testTile tile9 = new testTile(color5, false, false, false, true);
    public static final testTile tile10 = new testTile(color5, false, false, true, true);
    public static final testTile tile11 = new testTile(color4, false, false, false, true);

    private static final List<testTile> board = new ArrayList<>();
    private static final testTile[] board1 = new testTile[12];


    public static void crea() {

        board.add(tile0);
        board.add(tile1);
        board.add(tile2);
        board.add(tile3);
        board.add(tile4);
        board.add(tile5);
        board.add(tile6);
        board.add(tile7);
        board.add(tile8);
        board.add(tile9);
        board.add(tile10);
        board.add(tile11);
        board.toArray(board1);
    }

    public static void boardCreation(){
        StringBuilder line0 = new StringBuilder();
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();
        StringBuilder line4 = new StringBuilder();

        line0.append(firstline());
        line1.append(creaprima(0));
        line2.append(creaprima(LENGTH));
        line3.append(ultimaLinea(LENGTH *2));


        line4.append(line0);
        line4.append(line1);
        line4.append(line2);
        line4.append(line3);

        System.out.println(line1);

    }

    public static StringBuilder colleague(boolean door, int i){
        StringBuilder line = new StringBuilder();
        if(door) {
            if((i<5 || i> SIZECELL -5) && (i< SIZECELL +5 ||i> SIZECELL *2-5)  )
                line.append("_");
            else
                line.append(" ");
        }

        else{

            if(i== SIZECELL -1 || i== SIZECELL *2-1 || i== SIZECELL *3-1|| i==1 || i== SIZECELL +1 || i== SIZECELL *2+1 || i== SIZECELL *3+1 ||i== SIZECELL *4-1 )
                line.append("_");
            else
                line.append(" ");


        }


        return line;
    }

    public static StringBuilder addSouthernDoors(int i,int indexTile){

        StringBuilder line = new StringBuilder();

        if(indexTile == LENGTH *2) {
            line.append("_");
            return line;
        }


        if(board1[i/ SIZECELL +indexTile]!=null && board1[i/ SIZECELL + LENGTH +indexTile]!=null ) {

            if (board1[i/ SIZECELL +indexTile].getColor() == board1[i/ SIZECELL + LENGTH +indexTile].getColor()) {
                line.append(colleague(false,i));
            } else {
                if (board1[i/ SIZECELL].getDoors()[1])
                    line.append(colleague(true,i));
                else line.append("_");
            }

        }else line.append("_");

        return line;
    }

    public static String getColorTile(String color){

        if(color.equals(color1) )
            return ANSI_BRED_BACKGROUND;
        if(color.equals(color2))
            return ANSI_BGREEN_BACKGROUND;
        if(color.equals(color3))
            return ANSI_BBLUE_BACKGROUND;
        if(color.equals(color4))
            return ANSI_BYELLOW_BACKGROUND;
        else
            return ANSI_WHITE_BACKGROUND;

    }

    public static StringBuilder firstline(){
        StringBuilder line = new StringBuilder();
        int indexLenght = LENGTH;
        if(board1[LENGTH -1] == null )
            indexLenght = LENGTH -1;

        for (int i = 0; i <= SIZECELL * indexLenght; i++) {
            line.append("_");
        }
        line.append("\n");

        System.out.print(line);
        return line;
    }

    public static StringBuilder creaprima(int indexTile) {
        StringBuilder line = new StringBuilder();

        for (int j = 0; j <= HIGHCELL; j++) {
            for (int i = 0; i <= SIZECELL * LENGTH; i++) {
                if (  board1[ i/ SIZECELL + indexTile] != null) {
                    if (i < SIZECELL * LENGTH) {
                        line.append(valutation(i,j,indexTile));
                    }
                    else line.append(makeLastWall());
                }else if(indexTile == LENGTH)
                    line.append(makeLastWall());
            }
        }

        System.out.print(line);
        return line;
    }

    public static StringBuilder ultimaLinea(int indexTile){
        StringBuilder line = new StringBuilder();
        int indexValutation;
        for (int j = 0; j <= HIGHCELL; j++) {
            for (int i = 0; i <= SIZECELL * LENGTH; i++) {
                indexValutation =i/ SIZECELL +indexTile;
                if(board1[indexTile] != null || indexValutation!= LENGTH *2 && indexValutation != LENGTH *2 +1){
                    line.append(valutation2(i,j,indexTile));
                } else {
                    if (i/ SIZECELL + indexTile == LENGTH *2)
                        line.append(" ");
                    else{
                         line.append(valutation3(i,j,indexTile));
                    }
                }
            }
        }

        System.out.print(line);
        return line;
    }


    public static StringBuilder valutation2(int i,int j,int indexTile){
        StringBuilder line = new StringBuilder();
        if (  i/ SIZECELL + indexTile != board1.length && board1[ i/ SIZECELL + indexTile] != null) {
            line.append(valutation(i,j,indexTile));
        } else {
            if ( i/ SIZECELL + indexTile == board1.length)
                line.append(makeLastWall());
        }
        return line;
    }

    public static StringBuilder valutation(int i,int j,int indexTile ){
        StringBuilder line = new StringBuilder();
        if (board1[(i-1)/ SIZECELL +indexTile].getColor().equals((board1[i/ SIZECELL +indexTile].getColor())))
            line.append(sameRoom(i, j, board1[i/ SIZECELL +indexTile].getColor(),indexTile));
        else {
            if (board1[(i-1)/ SIZECELL +indexTile].getDoors()[2])
                line.append(makeDoor(i, j, board1[i/ SIZECELL +indexTile].getColor()));
            else
                line.append(makeWall(i, board1[i/ SIZECELL +indexTile].getColor()));
        }

        return line;
    }

    public static StringBuilder valutation3(int i,int j,int indexTile ){
        StringBuilder line = new StringBuilder();


        if(j!=0 &&j% HIGHCELL ==0){
            line.append(getColorTile(board1[i/ SIZECELL +indexTile].getColor()));
            if(i% SIZECELL ==0)
                line.append("|");
            else
                line.append("_");
            line.append(ANSI_RESET);
        }
        else line.append(makeWall(i, board1[i/ SIZECELL +indexTile].getColor()));
        return line;
    }


    public static StringBuilder sameRoom(int i,int j,String color,int indexTile){
        StringBuilder line = new StringBuilder();
        line.append(getColorTile(color));
        if(j% HIGHCELL ==0){
            if(i% SIZECELL == 0 )
                line.append("|");
            else{
                if(j!=0){

                    line.append(addSouthernDoors(i,indexTile));
                }
                else
                    line.append(" ");
            }
        }else{
            if(i==0)
                line.append("|");
            else
                line.append(" ");
        }
        line.append(ANSI_RESET);
        return line;
    }

    public static StringBuilder makeDoor(int i,int j, String color){
        StringBuilder line = new StringBuilder();
        line.append(getColorTile(color));
        if(j!= HIGHCELL /2 && j!= HIGHCELL /2+1 && i% SIZECELL ==0)
            line.append("|");
        else
            line.append(" ");
        line.append(ANSI_RESET);

        return line;
    }

    public static StringBuilder makeWall(int i, String color){
        StringBuilder line = new StringBuilder();
        line.append(getColorTile(color));
        if(i== SIZECELL * LENGTH)
            line.append("|\n");
        else{
            if(i% SIZECELL ==0)
                line.append("|");
            else
                line.append(" ");
        }
        line.append(ANSI_RESET);
        return line;
    }


    public static StringBuilder makeLastWall(){
        StringBuilder line = new StringBuilder();

        line.append("|\n");
        line.append(ANSI_RESET);

        return line;
    }



    public static void main(String[] arg) {
        System.out.print("\n\n\n\n\n\n\n\n\n");
        crea();
        boardCreation();



    }
}
