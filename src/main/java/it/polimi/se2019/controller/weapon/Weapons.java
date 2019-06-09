package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.model.weapon.serialization.WeaponFactory;
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
    private Weapons() {

    }

    public static Weapon get(String weaponName) {
        return WeaponFactory.fromJson(Jsons.get("weapons/real/" + weaponName));
    }
}


