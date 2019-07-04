package it.polimi.se2019.util;

import it.polimi.se2019.resource_handler.BadLoadException;
import it.polimi.se2019.resource_handler.JsonResource;
import it.polimi.se2019.resource_handler.NonExistentResourceException;
import it.polimi.se2019.resource_handler.ResourceHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Stream;

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

