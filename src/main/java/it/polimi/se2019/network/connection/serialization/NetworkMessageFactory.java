package it.polimi.se2019.network.connection.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.controller.response.serialization.ResponseFactory;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.serialization.UpdateFactory;
import it.polimi.se2019.network.connection.NetworkMessage;
import it.polimi.se2019.network.connection.NetworkMessageType;
import it.polimi.se2019.util.AnnotationExclusionStrategy;
import it.polimi.se2019.util.CustomFieldNamingStrategy;
import it.polimi.se2019.view.request.Request;
import it.polimi.se2019.view.request.serialization.RequestFactory;

public class NetworkMessageFactory {
    private NetworkMessageFactory() {
    }

    // GSON used to deal with serialization/deserialization
    private static Gson makeGsonSerializerDeserializer() {
        return new GsonBuilder()
                .setFieldNamingStrategy(new CustomFieldNamingStrategy())
                .addSerializationExclusionStrategy(new AnnotationExclusionStrategy())
                .create();
    }

    public static NetworkMessage fromJson(String toDeserialize) {
        if (toDeserialize == null)
            throw new NullPointerException();

        return makeGsonSerializerDeserializer().fromJson(toDeserialize, NetworkMessage.class);
    }

    public static String toJson(Request request) {
        return makeGsonSerializerDeserializer().toJson(
                new NetworkMessage(NetworkMessageType.REQUEST, RequestFactory.toJson(request)),
                NetworkMessage.class
        );
    }

    public static String toJson(Response response) {
        return makeGsonSerializerDeserializer().toJson(
                new NetworkMessage(NetworkMessageType.RESPONSE, ResponseFactory.toJson(response)),
                NetworkMessage.class
        );
    }

    public static String toJson(Update update) {
        return makeGsonSerializerDeserializer().toJson(
                new NetworkMessage(NetworkMessageType.UPDATE, UpdateFactory.toJson(update)),
                NetworkMessage.class
        );
    }

    public static String makeRawPong() {
        return makeGsonSerializerDeserializer().toJson(
                new NetworkMessage(NetworkMessageType.PONG, ""),
                NetworkMessage.class
        );
    }

    public static String makeRawPing() {
        return makeGsonSerializerDeserializer().toJson(
                new NetworkMessage(NetworkMessageType.PING, ""),
                NetworkMessage.class
        );
    }
}



