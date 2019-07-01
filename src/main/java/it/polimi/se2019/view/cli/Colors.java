package it.polimi.se2019.view.cli;

public class Colors {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RED =  "\u001B[31m";




    public static String findColor(String color){

        if(color.equalsIgnoreCase("yellow") )
            return ANSI_YELLOW;
        if(color.equalsIgnoreCase("purple"))
            return ANSI_PURPLE;
        if(color.equalsIgnoreCase("blue"))
            return ANSI_BLUE;
        if(color.equalsIgnoreCase("green"))
            return ANSI_GREEN;
        if(color.equalsIgnoreCase("red"))
            return ANSI_RED;
        else
            return ANSI_WHITE;

    }

}
