package it.polimi.se2019.util;

public class StringUtils {
    public static boolean equalIgnoringWhitespace(String lhs, String rhs) {
        return lhs.replaceAll("\\s+","").equals(rhs.replaceAll("\\s+",""));
    }
}
