package it.polimi.se2019.util;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceUtils {
    private ResourceUtils() {

    }

    private static final String PATH_DELIMITER = "/";

    public static String loadResource(String path) {
        Scanner scanner = new Scanner(
                ResourceUtils.class.getResourceAsStream(path)
        )
                .useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    public static Stream<String> flatWalk(String rootPath) {
        return Arrays.stream(
                loadResource(rootPath).split("\\n")
        )
                .map(paht -> rootPath + paht)
                .map(ResourceUtils::loadResource);
    }
}
