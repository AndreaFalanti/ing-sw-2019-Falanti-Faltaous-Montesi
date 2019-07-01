package it.polimi.se2019.model.board.serialization;

import com.google.gson.*;
import it.polimi.se2019.model.board.SpawnTile;
import it.polimi.se2019.model.board.Tile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomTilesDeserializer implements JsonDeserializer<List<Tile>>, JsonSerializer<List<Tile>> {

    @Override
    public List<Tile> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        List<Tile> result = new ArrayList<>();

        JsonArray jTiles = jsonElement.getAsJsonArray();

        for (JsonElement jeTile : jTiles) {
            if (jeTile.getAsJsonObject().get("type").getAsString().equals("empty"))
                result.add(null);
            else
                result.add(jsonDeserializationContext.deserialize(jeTile, Tile.class));
        }

        return result;
    }

    @Override
    public JsonElement serialize(List<Tile> tiles, Type type, JsonSerializationContext jsonSerializationContext) {
        return tiles.stream()
                .map(tile -> {
                    if (tile == null) {
                        JsonObject jTile = new JsonObject();

                        jTile.add("type", new JsonPrimitive("empty"));

                        return jTile;
                    }
                    else
                        return jsonSerializationContext.serialize(tile, Tile.class);
                })
                .reduce(new JsonArray(), (jTiles, jTile) -> {
                    ((JsonArray) jTiles).add(jTile);
                    return jTiles;
                });
    }
}
