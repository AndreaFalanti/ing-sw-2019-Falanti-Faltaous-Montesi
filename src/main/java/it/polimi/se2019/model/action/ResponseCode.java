package it.polimi.se2019.model.action;

public enum ResponseCode {
    OK,
    INVALID_MOVE,
    MAX_POWERUPS,
    MAX_WEAPON,
    NO_ACTION_LEFT,
    ALREADY_PICKED,
    NOT_AMMO_TILE,//to see if it is necessary
    PERFORMABLE_BY_ACTIVE_PLAYER,
    TILE_NOT_REACHABLE,
    CANT_MOVE,
    SHIFTING_MAX_THREE,
    YOU_ARENT_MOVING
}
