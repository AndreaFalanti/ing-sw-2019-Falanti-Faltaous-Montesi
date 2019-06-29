package it.polimi.se2019.model.board.serialization;

import com.google.gson.*;
import it.polimi.se2019.model.board.Board;

import java.lang.reflect.Type;

public class CustomBoardAdapter implements JsonSerializer<Board>, JsonDeserializer<Board> {
    @Override
    public Board deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        return Board.fromJson(jsonElement);
    }

    @Override
    public JsonElement serialize(Board board, Type type, JsonSerializationContext context) {
        return board.toJsonTree();
    }
}
