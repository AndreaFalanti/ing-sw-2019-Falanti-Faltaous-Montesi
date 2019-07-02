package it.polimi.se2019.network.server.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.controller.response.serialization.ResponseFactory;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.serialization.UpdateFactory;
import it.polimi.se2019.network.server.ServerMessage;
import it.polimi.se2019.network.server.ServerMessageType;
import it.polimi.se2019.util.AnnotationExclusionStrategy;
import it.polimi.se2019.util.CustomFieldNamingStrategy;
import it.polimi.se2019.util.gson.extras.typeadapters.RuntimeTypeAdapterFactory;

public final class ServerMessageFactory {
    private ServerMessageFactory () {
    }

    // GSON used to deal with serialization/deserialization
    private static Gson makeGsonSerializerDeserializer() {
        return new GsonBuilder()
                .setFieldNamingStrategy(new CustomFieldNamingStrategy())
                .addSerializationExclusionStrategy(new AnnotationExclusionStrategy())
                .create();
    }

    public static ServerMessage fromJson(String toDeserialize) {
        return makeGsonSerializerDeserializer().fromJson(toDeserialize, ServerMessage.class);
    }

    public static String toJson(Update update) {
        return makeGsonSerializerDeserializer().toJson(
                new ServerMessage(ServerMessageType.UPDATE, UpdateFactory.toJson(update)),
                ServerMessage.class
        );
    }

    public static String toJson(Response response) {
        return makeGsonSerializerDeserializer().toJson(
                new ServerMessage(ServerMessageType.RESPONSE, ResponseFactory.toJson(response)),
                ServerMessage.class
        );
    }

    public static String makeRawPing() {
        return makeGsonSerializerDeserializer().toJson(
                new ServerMessage(ServerMessageType.PING, ""),
                ServerMessage.class
        );
    }
}


