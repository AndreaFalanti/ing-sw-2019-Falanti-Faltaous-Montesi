package it.polimi.se2019.model.board;

import com.google.gson.annotations.JsonAdapter;
import it.polimi.se2019.model.Player;


public abstract class Tile {
    private TileColor color;

    @JsonAdapter(DoorsDeserializer.class)
    private boolean[] doors;


    public abstract void grabObjects(Player player);

    public TileColor getColor() {
        return null;
    }

    public boolean[] getDoors() {
        return null;
    }

    public abstract String getTileType();
}
