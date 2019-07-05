package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.model.weapon.serialization.WeaponFactory;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.util.ResourceUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton containing all weapons
 */
public class Weapons {
    private static final String PATH_TO_WEAPONS_RESOURCES_FOLDER = Jsons.PATH_TO_JSON_RESOURCES_FOLDER + "weapons/real/";
    private static final String JSON_SUFFIX = ".json";

    private Weapons() {
    }

    public static Weapon get(String id) {
        return WeaponFactory.fromJson(
                ResourceUtils.loadResource(
                        PATH_TO_WEAPONS_RESOURCES_FOLDER + id + JSON_SUFFIX
                )
        );
    }

    public static List<String> getNames() {
        return Arrays.stream(ResourceUtils.loadResource(PATH_TO_WEAPONS_RESOURCES_FOLDER)
                .split("\\n"))
                .collect(Collectors.toList());
    }

    public static List<Weapon> getAll() {
        return Arrays.stream(ResourceUtils.loadResource(PATH_TO_WEAPONS_RESOURCES_FOLDER)
                .split("\\n"))
                .map(path -> PATH_TO_WEAPONS_RESOURCES_FOLDER + path)
                .map(ResourceUtils::loadResource)
                .map(WeaponFactory::fromJson)
                .collect(Collectors.toList());
    }
}



