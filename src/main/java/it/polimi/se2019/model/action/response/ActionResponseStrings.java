package it.polimi.se2019.model.action.response;

/**
 * Static class to provide common strings used in action error messages
 *
 * @author Andrea Falanti
 */
public final class ActionResponseStrings {
    public static final String NO_ACTIONS_REMAINING = "Max number of action reached";
    public static final String ILLEGAL_TILE_DISTANCE = "You can't reach that tile";
    public static final String HACKED_MOVE = "Hack or developer fault? Illegal action detected";
    public static final String DISCARD_MESSAGE = "Discard more power up cards to cover the cost";
    public static final String NOT_ENOUGH_AMMO = "You can't pay the ammo cost";

    private ActionResponseStrings() {
    }
}
