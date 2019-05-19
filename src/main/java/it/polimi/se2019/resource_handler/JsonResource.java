package it.polimi.se2019.resource_handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class JsonResource extends Resource {
    private static final Logger logger = Logger.getLogger(JsonResource.class.getName());
    String mContents;

    private JsonResource() {
        super();
    }

    public static Resource loadFromPath(String path) {
        JsonResource result = new JsonResource();

        try {
            result.mContents = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }

        return result;
    }

    @Override
    public String get() {
        return mContents;
    }

    public void set(String contents) { mContents = contents; }
}
