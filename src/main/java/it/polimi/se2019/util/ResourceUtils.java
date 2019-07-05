package it.polimi.se2019.util;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class ResourceUtils {
    private ResourceUtils() {

    }

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
                .map(path -> rootPath + path)
                .map(ResourceUtils::loadResource);
    }
}
