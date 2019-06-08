package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.resource_handler.*;
import it.polimi.se2019.util.Jsons;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Singleton containing all weapons
 */
public class Weapons {
    public static final String PATH_TO_WEAPONS_RESOURCES_FOLDER = Jsons.PATH_TO_JSON_RESOURCES_FOLDER + "weapons/real/";

    private static boolean resourcesLoaded = false;
    private static ResourceHandler resourceHandler = new ResourceHandler();

    /**
     * Private constructor to satisfy the singleton pattern
     */
    private Weapons() {}


    /**
     * Utility function to load all weapon json files residing at a given path
     */
    private static void loadWeaponsAt(Path path) {
        resourceHandler.setBasePath(path.toString());

        try (Stream<Path> walk = Files.walk(path)) {
            walk
                    .filter(Files::isRegularFile)
                    .forEach(jsonFile ->
                            resourceHandler.registerResource(WeaponResource::loadFromPath, jsonFile.toString())
                    );
        } catch (IOException e) {
            throw new BadLoadException("could not load json resources from designed folder path: [" +
                    PATH_TO_WEAPONS_RESOURCES_FOLDER + "]");
        }
    }

    /**
     * Helper function to load all resources
     */
    private static void loadResources() {
        if (!resourcesLoaded) {
            resourcesLoaded = true;
            loadWeaponsAt(Paths.get(PATH_TO_WEAPONS_RESOURCES_FOLDER));
        }
    }

    /**
     * Also lazily loads all json files from disk
     * @param resourceKey the key of the wanted resource
     * @return the json string corresponding the the given resource key
     * @throws NonExistentResourceException when the requested resource key is nonexistent
     */
    public static Weapon get(String resourceKey) {
        loadResources();

        return (Weapon) resourceHandler.get(resourceKey);
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


