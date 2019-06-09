package it.polimi.se2019.resource_handler;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.weapon.serialization.WeaponFactory;

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

        try {
            result.mContents = WeaponFactory.fromJson(
                    new String(
                        Files.readAllBytes(Paths.get(path)),
                        StandardCharsets.UTF_8
                    )
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load weapon resource at file path " + path);
        }

        return result;
    }

    @Override
    public Weapon get() {
        return mContents;
    }
}
