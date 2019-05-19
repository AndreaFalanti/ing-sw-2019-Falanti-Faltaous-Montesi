package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.weapon.serialization.WeaponFactory;
import it.polimi.se2019.util.Jsons;

// TODO: implement
public class Weapons {
    private static final String WEAPONS_FOLDER_PATH = "weapons";

    // TODO: add doc
    public static Weapon get(String weaponKey) {
        return WeaponFactory.fromJson(Jsons.get("weapons/" + weaponKey));
    }
}
