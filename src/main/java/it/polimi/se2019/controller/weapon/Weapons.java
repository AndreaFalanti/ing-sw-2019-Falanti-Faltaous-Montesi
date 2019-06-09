package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.model.weapon.serialization.WeaponFactory;
import it.polimi.se2019.util.Jsons;

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


