package it.polimi.se2019.util;

public abstract class Resource {
    private String mPath;

    protected Resource() {}

    public String generateName() {
        return mPath.substring(0, mPath.indexOf('.'));
    }

    public abstract Object get();
}
