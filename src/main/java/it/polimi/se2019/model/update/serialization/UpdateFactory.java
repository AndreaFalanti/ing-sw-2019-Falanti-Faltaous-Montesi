package it.polimi.se2019.model.update.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.se2019.model.update.*;
import it.polimi.se2019.util.AnnotationExclusionStrategy;
import it.polimi.se2019.util.CustomFieldNamingStrategy;
import it.polimi.se2019.util.gson.extras.typeadapters.RuntimeTypeAdapterFactory;

public final class UpdateFactory {
    private UpdateFactory () {
    }

    // GSON used to deal with serialization/deserialization
    static Gson makeGsonSerializerDeserializer() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Update.class, "type")
                        .registerSubtype(ActivePlayerUpdate.class)
                        .registerSubtype(BoardTileUpdate.class)
                        .registerSubtype(KillScoredUpdate.class)
                        .registerSubtype(PlayerAmmoUpdate.class)
                        .registerSubtype(PlayerBoardFlipUpdate.class)
                        .registerSubtype(PlayerDamageUpdate.class)
                        .registerSubtype(PlayerMarksUpdate.class)
                        .registerSubtype(PlayerPositionUpdate.class)
                        .registerSubtype(PlayerPowerUpsUpdate.class)
                        .registerSubtype(PlayerRespawnUpdate.class)
                        .registerSubtype(PlayerWeaponsUpdate.class)
                        .registerSubtype(RemainingActionsUpdate.class)
                )
                //.setPrettyPrinting()
                .setFieldNamingStrategy(new CustomFieldNamingStrategy())
                .addSerializationExclusionStrategy(new AnnotationExclusionStrategy())
                .create();
    }

    public static JsonElement toJsonTree(Update toSerialize) {
        return makeGsonSerializerDeserializer().toJsonTree(toSerialize, Update.class);
    }

    public static String toJson(Update toSerialize) {
        return makeGsonSerializerDeserializer().toJson(toSerialize, Update.class);
    }

    public static Update fromJson(JsonElement toDeserialize) {
        return makeGsonSerializerDeserializer().fromJson(toDeserialize, Update.class);
    }

    public static Update fromJson(String toDeserialize) {
        return makeGsonSerializerDeserializer().fromJson(toDeserialize, Update.class);
    }
}


