package it.polimi.se2019.util;

import com.google.gson.FieldNamingPolicy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * This class is meant as a static wrapper for utility methods, and so cannot produce instances
     */
    private StringUtils() {}

    public static String removeLastChar(String toModify) {
        return toModify.substring(0, toModify.length() - 1);
    }

    public static String toSnakeCase(String toModify) {
        if (toModify == null)
            throw new NullPointerException();

        Matcher matcher = Pattern.compile("\\w+").matcher(toModify);

        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            builder.append(matcher.group().toLowerCase()).append("_");
        }
        return removeLastChar(builder.toString());
    }

    public static boolean equalIgnoringWhitespace(String lhs, String rhs) {
        return lhs.replaceAll("\\s+","").equals(rhs.replaceAll("\\s+",""));
    }
}
