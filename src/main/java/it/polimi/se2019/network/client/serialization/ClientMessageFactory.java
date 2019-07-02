package it.polimi.se2019.network.client.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.se2019.network.client.ClientMessage;
import it.polimi.se2019.network.client.ClientMessageType;
import it.polimi.se2019.util.AnnotationExclusionStrategy;
import it.polimi.se2019.util.CustomFieldNamingStrategy;
import it.polimi.se2019.view.request.Request;
import it.polimi.se2019.view.request.serialization.RequestFactory;

public class ClientMessageFactory {
    private ClientMessageFactory () {
    }

    // GSON used to deal with serialization/deserialization
    private static Gson makeGsonSerializerDeserializer() {
        return new GsonBuilder()
                .setFieldNamingStrategy(new CustomFieldNamingStrategy())
                .addSerializationExclusionStrategy(new AnnotationExclusionStrategy())
                .create();
    }

    public static String toJson(ClientMessage toSerialize) {
        return makeGsonSerializerDeserializer().toJson(toSerialize, ClientMessage.class);
    }

    public static ClientMessage fromJson(String toDeserialize) {
        return makeGsonSerializerDeserializer().fromJson(toDeserialize, ClientMessage.class);
    }

    public static String toJson(Request request) {
        return makeGsonSerializerDeserializer().toJson(
                new ClientMessage(ClientMessageType.REQUEST, RequestFactory.toJson(request)),
                ClientMessage.class
        );
    }

    public static String makeRawPong() {
        return makeGsonSerializerDeserializer().toJson(
                new ClientMessage(ClientMessageType.PONG, ""),
                ClientMessage.class
        );
    }
}



