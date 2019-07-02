package it.polimi.se2019.view.request.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.se2019.model.action.*;
import it.polimi.se2019.util.AnnotationExclusionStrategy;
import it.polimi.se2019.util.CustomFieldNamingStrategy;
import it.polimi.se2019.util.gson.extras.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.se2019.view.request.*;

public final class RequestFactory {
    private RequestFactory() {
    }

    // GSON used to deal with serialization/deserialization
    static Gson makeGsonSerializerDeserializer() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Request.class, "type")
                        .registerSubtype(ActionRequest.class)
                        .registerSubtype(DirectionSelectedRequest.class)
                        .registerSubtype(EffectsSelectedRequest.class)
                        .registerSubtype(PositionSelectedRequest.class)
                        .registerSubtype(PowerUpDiscardedRequest.class)
                        .registerSubtype(PowerUpsSelectedRequest.class)
                        .registerSubtype(RespawnPowerUpRequest.class)
                        .registerSubtype(RoomSelectedRequest.class)
                        .registerSubtype(TargetsSelectedRequest.class)
                        .registerSubtype(TurnEndRequest.class)
                        .registerSubtype(UndoWeaponInteractionRequest.class)
                        .registerSubtype(ValidPositionRequest.class)
                        .registerSubtype(WeaponModeSelectedRequest.class)
                        .registerSubtype(WeaponSelectedRequest.class)
                        .registerSubtype(UsePowerUpRequest.class)
                )
                .registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Action.class, "type")
                        .registerSubtype(GrabAmmoAction.class)
                        .registerSubtype(GrabWeaponAction.class)
                        .registerSubtype(MoveAction.class)
                        .registerSubtype(MoveGrabAction.class)
                        .registerSubtype(MoveReloadShootAction.class)
                        .registerSubtype(MoveShootAction.class)
                        .registerSubtype(NewtonAction.class)
                        .registerSubtype(ReloadAction.class)
                        .registerSubtype(ShootAction.class)
                        .registerSubtype(TeleportAction.class)
                )
                .registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(GrabAction.class, "type")
                        .registerSubtype(GrabAmmoAction.class)
                        .registerSubtype(GrabWeaponAction.class)
                )
                //.setPrettyPrinting()
                .setFieldNamingStrategy(new CustomFieldNamingStrategy())
                .addSerializationExclusionStrategy(new AnnotationExclusionStrategy())
                .create();
    }

    public static JsonElement toJsonTree(Request toSerialize) {
        return makeGsonSerializerDeserializer().toJsonTree(toSerialize, Request.class);
    }

    public static String toJson(Request toSerialize) {
        return makeGsonSerializerDeserializer().toJson(toSerialize, Request.class);
    }

    public static Request fromJson(JsonElement toDeserialize) {
        return makeGsonSerializerDeserializer().fromJson(toDeserialize, Request.class);
    }

    public static Request fromJson(String toDeserialize) {
        return makeGsonSerializerDeserializer().fromJson(toDeserialize, Request.class);
    }
}

