package it.polimi.se2019.resource_handler;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.weapon.serialization.WeaponFactory;
import it.polimi.se2019.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WeaponResource extends Resource {
    private Weapon mContents;

    private WeaponResource() {

    }

    public static Resource loadFromPath(String path) {
        WeaponResource result = new WeaponResource();

        result.mContents = WeaponFactory.fromJson(
                ResourceUtils.loadResource(path)
        );

        return result;
    }

    @Override
    public Weapon get() {
        return mContents;
    }
}
