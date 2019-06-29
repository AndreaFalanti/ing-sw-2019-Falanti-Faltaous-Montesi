package it.polimi.se2019.controller.response.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.se2019.controller.response.*;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.serialization.CustomBoardAdapter;
import it.polimi.se2019.util.AnnotationExclusionStrategy;
import it.polimi.se2019.util.CustomFieldNamingStrategy;
import it.polimi.se2019.util.gson.extras.typeadapters.RuntimeTypeAdapterFactory;

public final class ResponseFactory {
    private ResponseFactory() {
    }

    // GSON used to deal with serialization/deserialization
    static Gson makeGsonSerializerDeserializer() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Response.class, "type")
                        .registerSubtype(DiscardPowerUpResponse.class)
                        .registerSubtype(InitializationInfoResponse.class)
                        .registerSubtype(MessageResponse.class)
                        .registerSubtype(PickDirectionResponse.class)
                        .registerSubtype(PickPositionResponse.class)
                        .registerSubtype(PickPowerUpsResponse.class)
                        .registerSubtype(PickRespawnPowerUpResponse.class)
                        .registerSubtype(PickRoomColorResponse.class)
                        .registerSubtype(PickTargetsResponse.class)
                        .registerSubtype(PickWeaponModeResponse.class)
                        .registerSubtype(PickWeaponResponse.class)
                        .registerSubtype(ValidMoveResponse.class)
                )
                //.setPrettyPrinting()
                .registerTypeAdapter(Board.class, new CustomBoardAdapter())
                .setFieldNamingStrategy(new CustomFieldNamingStrategy())
                .addSerializationExclusionStrategy(new AnnotationExclusionStrategy())
                .addDeserializationExclusionStrategy(new AnnotationExclusionStrategy())
                .create();
    }

    public static JsonElement toJsonTree(Response toSerialize) {
        return makeGsonSerializerDeserializer().toJsonTree(toSerialize, Response.class);
    }

    public static String toJson(Response toSerialize) {
        return makeGsonSerializerDeserializer().toJson(toSerialize, Response.class);
    }

    public static Response fromJson(JsonElement toDeserialize) {
        return makeGsonSerializerDeserializer().fromJson(toDeserialize, Response.class);
    }

    public static Response fromJson(String toDeserialize) {
        return makeGsonSerializerDeserializer().fromJson(toDeserialize, Response.class);
    }
}


