package it.polimi.se2019.util;

/**
 * Singleton containing all json resources
 */
public class Jsons {
    public static final String PATH_TO_JSON_RESOURCES_FOLDER = "/json/";
    public static final String JSON_SUFFIX = ".json";

    private Jsons() {

    }

    public static String get(String path) {
        String realPath = PATH_TO_JSON_RESOURCES_FOLDER + path + JSON_SUFFIX;

        if (Jsons.class.getResource(realPath) == null)
            throw new IllegalArgumentException("Cannot load resource at path " + realPath + "...");

        return ResourceUtils.loadResource(realPath);
    }
}

