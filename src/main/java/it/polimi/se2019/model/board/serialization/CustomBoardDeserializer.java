package it.polimi.se2019.model.board.serialization;

import com.google.gson.*;
import it.polimi.se2019.model.board.Board;

import java.lang.reflect.Type;

public class CustomBoardDeserializer implements JsonSerializer<Board>, JsonDeserializer<Board> {
    @Override
    public Board deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        return Board.fromJson(jsonElement);
    }

    @Override
    public JsonElement serialize(Board board, Type type, JsonSerializationContext jsonSerializationContext) {
        return board.toJsonTree();
    }
}
