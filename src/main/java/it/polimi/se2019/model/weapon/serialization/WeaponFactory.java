package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.model.weapon.behaviour.Expression;
import it.polimi.se2019.util.AnnotationExclusionStrategy;
import it.polimi.se2019.util.CustomFieldNamingStrategy;

public class WeaponFactory {
    private WeaponFactory() {}

    // GSON used to deal with serialization/deserialization
    public static final Gson GSON = new GsonBuilder()
            // .registerTypeAdapterFactory(ExpressionFactory.RUN_TYPE_ADAPTER_FACTORY)
            .registerTypeAdapter(Expression.class, new CustomExpressionAdapter())
            .setPrettyPrinting()
            .setFieldNamingStrategy(new CustomFieldNamingStrategy())
            .addSerializationExclusionStrategy(new AnnotationExclusionStrategy())
            .create();

    // TODO: add doc
    // TODO: WIP refine implementation
    public static Weapon fromJson(String toDeserialize) {
        return GSON.fromJson(toDeserialize, Weapon.class);
    }

    // TODO: add doc
    public static String toJson(Weapon toSerialize) {
        return GSON.toJson(toSerialize, Weapon.class);
    }

    // TODO: add doc
    public static JsonElement toJsonTree(Weapon toSerialize) {
        return GSON.toJsonTree(toSerialize, Weapon.class);
    }
}
