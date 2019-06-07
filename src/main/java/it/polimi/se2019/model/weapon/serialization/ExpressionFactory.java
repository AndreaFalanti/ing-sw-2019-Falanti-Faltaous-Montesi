package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.*;
import it.polimi.se2019.model.weapon.Expression;
import it.polimi.se2019.model.weapon.behaviour.*;
import it.polimi.se2019.util.AnnotationExclusionStrategy;
import it.polimi.se2019.util.CustomFieldNamingStrategy;
import it.polimi.se2019.util.gson.extras.typeadapters.RuntimeTypeAdapterFactory;

public class ExpressionFactory {
    // GSON used to deal with serialization
    // TODO: register all subtypes (maybe use generics to automate)
    static RuntimeTypeAdapterFactory<Expression> makeRuntimeTypeAdapterFactory() {
        return RuntimeTypeAdapterFactory.of(Expression.class, "expr")
                .registerSubtype(Behaviour.class, "Behaviour")
                .registerSubtype(InflictDamage.class, "InflictDamage")
                .registerSubtype(TargetsLiteral.class, "TargetsLiteral")
                .registerSubtype(DamageLiteral.class, "DamageLiteral")
                .registerSubtype(SelectTargets.class, "SelectTargets")
                .registerSubtype(CanSee.class, "CanSee")
                .registerSubtype(GetTargets.class, "GetTargets")
                .registerSubtype(Do.class, "Do")
                .registerSubtype(You.class, "You")
                .registerSubtype(IntLiteral.class, "IntLiteral")
                .registerSubtype(NegateSelection.class, "NegateSelection")
                .registerSubtype(Store.class, "Store")
                .registerSubtype(StringLiteral.class, "StringLiteral")
                .registerSubtype(Done.class, "Done")
                .registerSubtype(SelectOneTarget.class, "SelectOneTarget");
    }

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
                .registerTypeAdapterFactory(makeRuntimeTypeAdapterFactory())
                .setPrettyPrinting()
                .setFieldNamingStrategy(new CustomFieldNamingStrategy())
                .addSerializationExclusionStrategy(new AnnotationExclusionStrategy())
                .create();
    }

    // TODO: add doc
    public static Expression fromJson(JsonElement toDeserialize) {
        return makeGsonDeserializer().fromJson(toDeserialize, Expression.class);
    }

    // TODO: add doc
    public static Expression fromJson(String toDeserialize) {
        return makeGsonDeserializer().fromJson(new Gson().fromJson(toDeserialize, JsonElement.class), Expression.class);
    }

    // TODO: add doc
    public static String toJson(Expression toSerialize) {
        return makeGsonSerializer().toJson(toSerialize, Expression.class);
    }

    public static JsonElement toJsonTree(Expression toSerialize) {
        return makeGsonSerializer().toJsonTree(toSerialize, Expression.class);
    }
}
