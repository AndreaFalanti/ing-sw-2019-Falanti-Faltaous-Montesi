package it.polimi.se2019.view.cli;

import java.util.ArrayList;
import java.util.List;

public class BoardCLI {
    public static final int lung = 4;
    public static final int grandcella = 29;
    public static final int altezzacella = 8;
    public static final int grandcellaltezza = 8;
    private StringBuilder line1 = new StringBuilder();
    private StringBuilder line2;
    private StringBuilder line3;
    public static final StringBuilder ANSI_RESET = new StringBuilder("\u001B[0m");
    public static final StringBuilder ANSI_BLACK = new StringBuilder("\u001B[30m");
    public static final StringBuilder ANSI_RED = new StringBuilder("\u001B[31m");
    public static final StringBuilder ANSI_GREEN = new StringBuilder("\u001B[32m");
    public static final StringBuilder ANSI_YELLOW = new StringBuilder("\u001B[33m");
    public static final StringBuilder ANSI_BLUE = new StringBuilder("\u001B[34m");
    public static final StringBuilder ANSI_PURPLE = new StringBuilder("\u001B[35m");
    public static final StringBuilder ANSI_CYAN = new StringBuilder("\u001B[36m");
    public static final StringBuilder ANSI_WHITE = new StringBuilder("\u001B[37m");
    public static final StringBuilder ANSI_BLACK_BACKGROUND = new StringBuilder("\u001B[40m");
    public static final StringBuilder ANSI_RED_BACKGROUND = new StringBuilder("\u001B[41m");
    public static final StringBuilder ANSI_GREEN_BACKGROUND = new StringBuilder("\u001B[42m");
    public static final StringBuilder ANSI_YELLOW_BACKGROUND = new StringBuilder("\u001B[43m");
    public static final StringBuilder ANSI_BLUE_BACKGROUND = new StringBuilder("\u001B[44m");
    public static final StringBuilder ANSI_PURPLE_BACKGROUND = new StringBuilder("\u001B[45m");
    public static final StringBuilder ANSI_CYAN_BACKGROUND = new StringBuilder("\u001B[46m");
    public static final StringBuilder ANSI_WHITE_BACKGROUND = new StringBuilder("\u001B[47m");
    public static final StringBuilder ANSI_BRED_BACKGROUND = new StringBuilder("\u001B[41;1m");
    public static final StringBuilder ANSI_BGREEN_BACKGROUND = new StringBuilder("\u001B[42;1m");
    public static final StringBuilder ANSI_BYELLOW_BACKGROUND = new StringBuilder("\u001B[43;1m");
    public static final StringBuilder ANSI_BBLUE_BACKGROUND = new StringBuilder("\u001B[44;1m");
    public static final StringBuilder ANSI_BPURPLE_BACKGROUND = new StringBuilder("\u001B[45;1m");
    public static final StringBuilder ANSI_BCYAN_BACKGROUND = new StringBuilder("\u001B[46;1m");
    public static final StringBuilder color1 = new StringBuilder("RED");
    public static final StringBuilder color4 = new StringBuilder("YELLOW");
    public static final StringBuilder color2 = new StringBuilder("GREEN");
    public static final StringBuilder color3 = new StringBuilder("BLUE");
    public static final StringBuilder color5 = new StringBuilder("WHITE");
    public static final StringBuilder sud = new StringBuilder("sud");
    public static final StringBuilder est = new StringBuilder("est");
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

    public static final List<testTile> board = new ArrayList<>();
    public static final testTile[] board1 = new testTile[12];


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

    public static void board(){
        StringBuilder line0 = new StringBuilder();
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();
        StringBuilder line4 = new StringBuilder();
        StringBuilder line5 = new StringBuilder();

        line0.append(firstline());
        line1.append(creaprima(0));
        line2.append(creaprima(lung));
        line3.append(ultimaLinea(lung*2));


        line4.append(line0);
        line4.append(line1);
        line4.append(line2);
        line4.append(line3);
        String boarder= line4.substring(0);

        System.out.println(line1);

    }

    public static StringBuilder colleague(boolean door, int i){
        StringBuilder line = new StringBuilder();
        if(door) {
            if((i<5 || i>grandcella-5) && (i<grandcella+5 ||i>grandcella*2-5)  )
                line.append("_");
            else
                line.append(" ");
        }

        else{

            if(i==grandcella-1 || i==grandcella*2-1 || i==grandcella*3-1|| i==1 || i==grandcella+1 || i==grandcella*2+1 || i==grandcella*3+1 ||i==grandcella*4-1 )
                line.append("_");
            else
                line.append(" ");


        }


        return line;
    }

    public static StringBuilder addSouthernDoors(int i,int indexTile){
        long start;
        int end;
        StringBuilder line = new StringBuilder();

        if(indexTile ==lung*2) {
            line.append("_");
            return line;
        }


        if(board1[i/grandcella+indexTile]!=null && board1[i/grandcella + lung +indexTile]!=null ) {

            if (board1[i/grandcella +indexTile].getColor() == board1[i/grandcella + lung+indexTile].getColor()) {
                line.append(colleague(false,i));
            } else {
                if (board1[i/grandcella].getDoors()[1])
                    line.append(colleague(true,i));
                else line.append("_");
            }

        }else line.append("_");

        return line;
    }

    public static StringBuilder getColorTile(StringBuilder color){

        if(color == color1 )
            return ANSI_BRED_BACKGROUND;
        if(color == color2)
            return ANSI_BGREEN_BACKGROUND;
        if(color == color3)
            return ANSI_BBLUE_BACKGROUND;
        if(color == color4)
            return ANSI_BYELLOW_BACKGROUND;
        else
            return ANSI_WHITE_BACKGROUND;

    }

    public static StringBuilder firstline(){
        StringBuilder line = new StringBuilder();
        int indexLenght = lung;
        if(board1[lung-1] == null )
            indexLenght = lung -1;

        for (int i = 0; i <= grandcella * indexLenght; i++) {
            line.append("_");
        }
        line.append("\n");

        System.out.print(line);
        return line;
    }

    public static StringBuilder creaprima(int indexTile) {
        StringBuilder line = new StringBuilder();

        for (int j = 0; j <= altezzacella; j++) {
            for (int i = 0; i <= grandcella * lung; i++) {
                if (  board1[ i/grandcella + indexTile] != null) {
                    if (i < grandcella * lung ) {
                        line.append(valutation(i,j,indexTile));
                    }
                    else line.append(makeLastWall());
                }else if(indexTile ==lung)
                    line.append(makeLastWall());
            }
        }

        System.out.print(line);
        return line;
    }

    public static StringBuilder ultimaLinea(int indexTile){
        StringBuilder line = new StringBuilder();

        for (int j = 0; j <= altezzacella; j++) {
            for (int i = 0; i <= grandcella * lung; i++) {
                if(board1[indexTile] != null){
                    line.append(valutation2(i,j,indexTile));
                } else {
                    if (i/grandcella + indexTile == lung*2)
                        line.append(" ");
                    else{
                        if (i/grandcella + indexTile == lung*2+1 )
                            line.append(valutation3(i,j,indexTile));
                        else
                            line.append(valutation2(i,j,indexTile));
                    }
                }
            }
        }

        System.out.print(line);
        return line;
    }


    public static StringBuilder valutation2(int i,int j,int indexTile){
        StringBuilder line = new StringBuilder();
        if (  i/grandcella + indexTile != board1.length && board1[ i/grandcella + indexTile] != null) {
            line.append(valutation(i,j,indexTile));
        } else {
            if ( i/grandcella + indexTile == board1.length)
                line.append(makeLastWall());
        }
        return line;
    }

    public static StringBuilder valutation(int i,int j,int indexTile ){
        StringBuilder line = new StringBuilder();
        if (board1[(i-1)/grandcella +indexTile].getColor() == (board1[i/grandcella+indexTile].getColor()))
            line.append(sameRoom(i, j, board1[i/grandcella+indexTile].getColor(),indexTile));
        else {
            if (board1[(i-1)/grandcella+indexTile].getDoors()[2])
                line.append(makeDoor(i, j, board1[i/grandcella+indexTile].getColor()));
            else
                line.append(makeWall(i, board1[i/grandcella+indexTile].getColor()));
        }

        return line;
    }

    public static StringBuilder valutation3(int i,int j,int indexTile ){
        StringBuilder line = new StringBuilder();


        if(j!=0 &&j%altezzacella==0){
            line.append(getColorTile(board1[i/grandcella+indexTile].getColor()));
            if(i%grandcella==0)
                line.append("|");
            else
                line.append("_");
            line.append(ANSI_RESET);
        }
        else line.append(makeWall(i, board1[i/grandcella+indexTile].getColor()));
        return line;
    }


    public static StringBuilder sameRoom(int i,int j,StringBuilder color,int indexTile){
        StringBuilder line = new StringBuilder();
        line.append(getColorTile(color));
        if(j%altezzacella==0){
            if(i%grandcella == 0 )
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

    public static StringBuilder makeDoor(int i,int j, StringBuilder color){
        StringBuilder line = new StringBuilder();
        line.append(getColorTile(color));
        if(j!=altezzacella/2 && j!=altezzacella/2+1 && i%grandcella==0)
            line.append("|");
        else
            line.append(" ");
        line.append(ANSI_RESET);

        return line;
    }

    public static StringBuilder makeWall(int i, StringBuilder color){
        StringBuilder line = new StringBuilder();
        line.append(getColorTile(color));
        if(i==grandcella*lung)
            line.append("|\n");
        else{
            if(i%grandcella==0)
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
        int mono =1;
        System.out.print("\n\n\n\n\n\n\n\n\n");
        crea();
        board();



    }
}
