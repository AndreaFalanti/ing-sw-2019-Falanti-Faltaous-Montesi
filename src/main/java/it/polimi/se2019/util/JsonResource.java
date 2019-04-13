package it.polimi.se2019.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JsonResource extends Resource {
    String mContents;

    private JsonResource() {
        super();
    }

    public static Resource loadFromPath(String path) {
        JsonResource result = new JsonResource();

        try {
            result.mContents = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public String get() {
        return mContents;
    }
}
