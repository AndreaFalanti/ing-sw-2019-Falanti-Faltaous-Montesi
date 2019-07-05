package it.polimi.se2019.model;

/**
 * Represent all possible colors assigned to players, used to identify them in games
 *
 * @author Andrea Falanti
 */
public enum PlayerColor {
    YELLOW("Yellow"),
    GREEN("Green"),
    PURPLE("Purple"),
    GREY("Grey"),
    BLUE("Blue");

    private String mPascalName;

    PlayerColor (String pascalName) {
        mPascalName = pascalName;
    }

    public String getPascalName() {
        return mPascalName;
    }
}
