package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.se2019.controller.weapon.Expression;
import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.util.AnnotationExclusionStrategy;
import it.polimi.se2019.util.CustomFieldNamingStrategy;


public class WeaponFactory {
    // GSON used to deal with serialization/deserialization
    static Gson makeGsonDeserializer() {
        return new GsonBuilder()
                .registerTypeAdapter(Expression.class, new ExpressionParser())
                .setPrettyPrinting()
                .setFieldNamingStrategy(new CustomFieldNamingStrategy())
                .addSerializationExclusionStrategy(new AnnotationExclusionStrategy())
                .create();
    }

    static Gson makeGsonSerializer() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(ExpressionFactory.makeRuntimeTypeAdapterFactory())
                .setPrettyPrinting()
                .setFieldNamingStrategy(new CustomFieldNamingStrategy())
                .addSerializationExclusionStrategy(new AnnotationExclusionStrategy())
                .create();
    }

    // TODO: add doc
    // TODO: WIP refine implementation
    public static Weapon fromJson(String toDeserialize) {
        return makeGsonDeserializer().fromJson(toDeserialize, Weapon.class);
    }

    // TODO: add doc
    public static String toJson(Weapon toSerialize) {
        return makeGsonSerializer().toJson(toSerialize, Weapon.class);
    }

    // TODO: add doc
    public static JsonElement toJsonTree(Weapon toSerialize) {
        return makeGsonSerializer().toJsonTree(toSerialize, Weapon.class);
    }
}
