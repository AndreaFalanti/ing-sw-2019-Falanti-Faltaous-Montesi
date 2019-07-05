package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.model.weapon.serialization.WeaponFactory;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.util.ResourceUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Singleton which can load all weapons
 * @author Stefano Montesi
 */
public class Weapons {
    private static final String PATH_TO_WEAPONS_RESOURCES_FOLDER = Jsons.PATH_TO_JSON_RESOURCES_FOLDER + "weapons/real/";
    private static final String JSON_SUFFIX = ".json";

    private static final Set<String> WEAPON_NAMES = new HashSet<>(Arrays.asList(
            "lock_rifle",
            "vortex_cannon",
            "furnace",
            "plasma_gun",
            "heatseeker",
            "whisper",
            "hellion",
            "zx-2",
            "grenade_launcher",
            "shotgun",
            "rocket_launcher",
            "shockwave",
            "cyberblade",
            "electroscythe",
            "flamethrower",
            "tractor_beam",
            "thor",
            "power_glove",
            "railgun",
            "sledgehammer",
            "machine_gun"
    ));

    private Weapons() {
    }

    public static Weapon get(String id) {
        return WeaponFactory.fromJson(
                ResourceUtils.loadResource(
                        PATH_TO_WEAPONS_RESOURCES_FOLDER + id + JSON_SUFFIX
                )
        );
    }

    public static Set<String> getNames() {
        return WEAPON_NAMES;
    }

    public static List<Weapon> getAll() {
        return WEAPON_NAMES.stream()
                .map(name -> PATH_TO_WEAPONS_RESOURCES_FOLDER + name + JSON_SUFFIX)
                .map(ResourceUtils::loadResource)
                .map(WeaponFactory::fromJson)
                .collect(Collectors.toList());
    }
}



