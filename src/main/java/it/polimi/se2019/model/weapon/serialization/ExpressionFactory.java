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
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Expression.class, "expr")
                    .registerSubtype(InflictDamage.class, "InflictDamage")
                    .registerSubtype(TargetsLiteral.class, "TargetsLiteral")
                    .registerSubtype(DamageLiteral.class, "DamageLiteral")
                    .registerSubtype(ActionLiteral.class, "ActionLiteral")
                    .registerSubtype(SelectTargets.class, "SelectTargets")
                    .registerSubtype(CanSee.class, "CanSee")
                    .registerSubtype(IntLiteral.class, "IntLiteral")
                    .registerSubtype(NegateSelection.class, "NegateSelection")
                    .registerSubtype(SelectOneTarget.class, "SelectOneTarget"))
            .setFieldNamingStrategy(new CustomFieldNamingStrategy())
            .addSerializationExclusionStrategy(new AnnotationExclusionStrategy())
            .create();

    // TODO: add doc
    // TODO: WIP refine implementation
    public static Expression fromJson(String toDeserialize) {
        return GSON.fromJson(toDeserialize, Expression.class);
    }

    // TODO: add doc
    public static String toJson(Expression toSerialize) {
        return GSON.toJson(toSerialize, Expression.class);
    }
}
