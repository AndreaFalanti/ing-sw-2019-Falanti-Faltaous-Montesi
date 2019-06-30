package it.polimi.se2019.network.server.serialization;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.controller.response.serialization.ResponseFactory;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.serialization.UpdateFactory;

public final class ServerMessageFactory {
    public static final String MESSAGE_TYPE_JSON_FIELD = "messageType";
    public static final String CONTENTS_JSON_FIELD = "contents";

    private ServerMessageFactory() {
    }

    public static JsonElement toJsonTree(Response toSerialize) {
        JsonObject jMessage = new JsonObject();
        jMessage.addProperty(MESSAGE_TYPE_JSON_FIELD, ServerMessageType.Response.toString());
        jMessage.add(CONTENTS_JSON_FIELD, ResponseFactory.toJsonTree(toSerialize));

        return jMessage;
    }

    public static JsonElement toJsonTree(Update toSerialize) {
        JsonObject jMessage = new JsonObject();
        jMessage.addProperty(MESSAGE_TYPE_JSON_FIELD, ServerMessageType.Update.toString());
        jMessage.add(CONTENTS_JSON_FIELD, UpdateFactory.toJsonTree(toSerialize));

        return jMessage;
    }

    public static String toJson(Response toSerialize) {
        return new Gson().toJson(toJsonTree(toSerialize));
    }

    public static String toJson(Update toSerialize) {
        return new Gson().toJson(toJsonTree(toSerialize));
    }

    public static ServerMessageType getMessageType(JsonElement jMessage) {
        return ServerMessageType.valueOf(jMessage.getAsJsonObject().get(MESSAGE_TYPE_JSON_FIELD).getAsString());
    }

    public static ServerMessageType getMessageType(String rawMessage) {
        return getMessageType(new Gson().fromJson(rawMessage, JsonElement.class));
    }

    public static JsonElement unrwrapMessage(JsonElement element) {
        return element.getAsJsonObject().get(CONTENTS_JSON_FIELD);
    }

    public static Response getAsResponse(String rawMessage) {
        JsonElement jMessage = new Gson().fromJson(rawMessage, JsonElement.class);

        if (!getMessageType(jMessage).equals(ServerMessageType.Response))
            throw new IllegalArgumentException("Not a response!");

        return ResponseFactory.fromJson(unrwrapMessage(jMessage));
    }

    public static Update getAsUpdate(String rawMessage) {
        JsonElement jMessage = new Gson().toJsonTree(rawMessage);

        if (!getMessageType(jMessage).equals(ServerMessageType.Update))
            throw new IllegalArgumentException("Not an update!");

        return UpdateFactory.fromJson(unrwrapMessage(jMessage));
    }
}


