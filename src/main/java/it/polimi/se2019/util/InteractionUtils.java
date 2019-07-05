package it.polimi.se2019.util;

import java.util.Scanner;

public class InteractionUtils {
    private static Scanner mStdIn = new Scanner(System.in);

    private InteractionUtils() {
    }

    public static String input(String message) {
        print(message);

        return input();
    }

    public static String input() {
        return mStdIn.nextLine();
    }

    public static void println(String line) {
        System.out.println(line);
    }

    public static void print(String toPrint) {
        System.out.print(toPrint);
    }
}
