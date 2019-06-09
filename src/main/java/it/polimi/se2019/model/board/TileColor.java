package it.polimi.se2019.model.board;

public enum TileColor {
    GREEN("Green"),
    RED("Red"),
    YELLOW("Yellow"),
    BLUE("Blue"),
    WHITE("White"),
    PURPLE("Purple");


    private String mPascalName;

    TileColor (String pascalName) {
        mPascalName = pascalName;
    }

    public String getPascalName() {
        return mPascalName;
    }
}
