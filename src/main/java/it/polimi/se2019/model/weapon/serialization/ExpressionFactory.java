package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.*;
import it.polimi.se2019.model.weapon.behaviour.*;
import it.polimi.se2019.util.AnnotationExclusionStrategy;
import it.polimi.se2019.util.CustomFieldNamingStrategy;
import it.polimi.se2019.util.gson.extras.typeadapters.RuntimeTypeAdapterFactory;

public class ExpressionFactory {
    private ExpressionFactory() {}

    // GSON used to deal with serialization/deserialization
    // TODO: register all subtypes (maybe use generics to automate)
    public static final RuntimeTypeAdapterFactory<Expression> RUN_TYPE_ADAPTER_FACTORY =
            RuntimeTypeAdapterFactory.of(Expression.class, "expr")
                    .registerSubtype(InflictDamage.class, "InflictDamage")
                    .registerSubtype(TargetsLiteral.class, "TargetsLiteral")
                    .registerSubtype(DamageLiteral.class, "DamageLiteral")
                    .registerSubtype(ActionLiteral.class, "ActionLiteral")
                    .registerSubtype(SelectTargets.class, "SelectTargets")
                    .registerSubtype(CanSee.class, "CanSee")
                    .registerSubtype(GetTargets.class, "GetTargets")
                    .registerSubtype(IntLiteral.class, "IntLiteral")
                    .registerSubtype(NegateSelection.class, "NegateSelection")
                    .registerSubtype(Done.class, "Done")
                    .registerSubtype(SelectOneTarget.class, "SelectOneTarget");

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(RUN_TYPE_ADAPTER_FACTORY)
            .setPrettyPrinting()
            .setFieldNamingStrategy(new CustomFieldNamingStrategy())
            .addSerializationExclusionStrategy(new AnnotationExclusionStrategy())
            .create();

    // TODO: add doc
    public static Expression fromJson(JsonElement toDeserialize) {
        return GSON.fromJson(toDeserialize, Expression.class);
    }

    // TODO: add doc
    public static Expression fromJson(String toDeserialize) {
        return GSON.fromJson(toDeserialize, Expression.class);
    }

    // TODO: add doc
    public static String toJson(Expression toSerialize) {
        return GSON.toJson(toSerialize, Expression.class);
    }

    public static JsonElement toJsonTree(Expression toSerialize) {
        return GSON.toJsonTree(toSerialize, Expression.class);
    }
}
