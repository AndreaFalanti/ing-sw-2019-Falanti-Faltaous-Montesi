package it.polimi.se2019.util;

import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.function.Function;

public class ResourceHandler {
    // path to
    static final String PATH_TO_RESOURCES_FOLDER = "./resources/";

    // fields
    private final HashMap<String, Resource> mResources = new HashMap<>();

    // methods
    public void registerResource(Function<String, Resource> loader, String path, String customName) {
        mResources.put(customName, loader.apply(PATH_TO_RESOURCES_FOLDER + path));
    }
    public void registerResource(Function<String, Resource> loader, String path) {
        if (path.isEmpty())
            throw new RuntimeException("Cannot load resource from empty path!");

        registerResource(loader, path,
                         path.substring(0, path.indexOf('.')));
    }

    public Object get(String resourceKey) throws NonexistentResourceException {
        Resource resource =  mResources.get(resourceKey);

        if (resource == null)
            throw new NonexistentResourceException(resourceKey);

        return resource.get();
    };
}

