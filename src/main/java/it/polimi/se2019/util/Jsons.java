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
    public static final String PATH_TO_RESOURCES_FOLDER = "resources/";
    public static final String PATH_TO_JSON_RESOURCES_FOLDER = PATH_TO_RESOURCES_FOLDER + "json/";

    private static boolean resourcesLoaded = false;
    private static ResourceHandler resourceHandler = new ResourceHandler();

    /**
     * Private constructor to satisfy the singleton pattern
     */
    private Jsons() {}

    /**
     * Utility function to load all json files residing at a given path
     */
    private static void loadJsonsAt(Path path) throws BadLoadException {
        resourceHandler.setBasePath(path.toString());

        try (Stream<Path> walk = Files.walk(path)) {
            walk
                    .filter(Files::isRegularFile)
                    .forEach(jsonFile ->
                            resourceHandler.registerResource(JsonResource::loadFromPath, jsonFile.toString())
                    );
        } catch (IOException e) {
            throw new BadLoadException("could not load json resources from designed folder path: [" +
                    PATH_TO_JSON_RESOURCES_FOLDER + "]");
        }
    }

    /**
     * Helper function to load all resources
     */
    private static void loadResources() {
        if (!resourcesLoaded) {
            resourcesLoaded = true;
            loadJsonsAt(Paths.get(PATH_TO_JSON_RESOURCES_FOLDER));
        }
    }

    /**
     * Fetches the desired json string
     * Also lazily loads all json files from disk
     * @param resourceKey the key of the wanted resource
     * @return the json string corresponding the the given resource key
     * @throws NonExistentResourceException when the requested resource key is nonexistent
     */
    public static String get(String resourceKey) throws NonExistentResourceException {
        loadResources();

        return (String) resourceHandler.get(resourceKey);
    }

    /**
     * Lists all the resources keys - used for debugging purposes
     * @return a list containing all the keys to the resources stored inside this class
     */
    public static Set<String> listResourceNames() {
        loadResources();

        return resourceHandler.listResourceNames();
    }
}

