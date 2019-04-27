package it.polimi.se2019.resource_handler;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

public class ResourceHandler {
    // fields
    private final HashMap<String, Resource> mResources = new HashMap<>();
    private String mBasePath;

    // methods
    public void setBasePath(String basePath) {
        mBasePath = basePath;
    }

    public void registerResource(Function<String, Resource> loader, String path, String customName) {
        if (path.isEmpty())
            throw new IllegalArgumentException("Cannot load resource from empty path!");
        mResources.put(customName, loader.apply(path));
    }
    public void registerResource(Function<String, Resource> loader, String path) {
        // check if base paths exists, if so save it for later (to remove it)
        int baseStartIndex = path.indexOf(mBasePath);
        int baseEndIndex = (baseStartIndex == -1) ?
                0 :
                baseStartIndex + mBasePath.length() + 1; // the +1 is for removing the "/"

        registerResource(loader, path,
                path.substring(baseEndIndex, path.indexOf('.')));
    }

    public Object get(String resourceKey) {
        Resource resource =  mResources.get(resourceKey);

        if (resource == null)
            throw new NonExistentResourceException(resourceKey);

        return resource.get();
    }

    public Set<String> listResourceNames() {
        return mResources.keySet();
    }
}

