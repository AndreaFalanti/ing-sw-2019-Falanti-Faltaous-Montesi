package it.polimi.se2019.util;

public class StringUtils {
    /**
     * This class is meant as a static wrapper for utility methods, and so cannot produce instances
     */
    private StringUtils() {}

    public static String removeLastChar(String toModify) {
        return toModify.substring(0, toModify.length() - 1);
    }

    public static boolean equalIgnoringWhitespace(String lhs, String rhs) {
        return lhs.replaceAll("\\s+","").equals(rhs.replaceAll("\\s+",""));
    }
}
