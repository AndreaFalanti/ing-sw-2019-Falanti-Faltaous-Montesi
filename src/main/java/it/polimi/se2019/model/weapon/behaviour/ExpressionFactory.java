package it.polimi.se2019.model.weapon.behaviour;

import com.google.gson.*;
import it.polimi.se2019.util.AnnotationExclusionStrategy;
import it.polimi.se2019.util.CustomFieldNamingStrategy;
import it.polimi.se2019.util.gson.extras.typeadapters.RuntimeTypeAdapterFactory;

import java.lang.reflect.Type;

public class ExpressionFactory {
    // GSON used to deal with serialization/deserialization
    // TODO: register all subtypes (maybe use generics to automate)
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Expression.class, "expr")
                    .registerSubtype(InflictDamage.class, "InflictDamage")
                    .registerSubtype(TargetsLiteral.class, "TargetsLiteral")
                    .registerSubtype(DamageLiteral.class, "DamageLiteral"))
            .setFieldNamingStrategy(new CustomFieldNamingStrategy())
            .addSerializationExclusionStrategy(new AnnotationExclusionStrategy())
            .create();

    // TODO: add doc
    // TODO: WIP refine implementation
    private static Expression fromJsonIgnoringSubExpressions(String toDeserialize) {
        return GSON.fromJson(toDeserialize, Expression.class);
    }

    // TODO: add doc
    // TODO: WIP refine implementation
    public static Expression fromJson(String toDeserialize) {
        // base case
        // if (toSerialize.getSubExpressions().isEmpty())
            // TODO: WIP: return fromJsonIgnoringSubExpressions();

        // recurse among subexpressions while registering them
        // toSerialize.getSubExpressions().stream()
                // .map(subexpr -> {
                    // return fromJson(subexpr);
                // });

        return null;
    }

    // TODO: add doc
    public static String toJson(Expression toSerialize) {
        return GSON.toJson(toSerialize, Expression.class);
    }
}
