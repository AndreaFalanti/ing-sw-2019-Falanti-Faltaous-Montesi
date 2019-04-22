package it.polimi.se2019.resource_handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
