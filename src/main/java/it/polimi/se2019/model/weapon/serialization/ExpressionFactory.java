package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.se2019.controller.weapon.expression.*;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.serialization.AmmoValueDeserializer;
import it.polimi.se2019.util.AnnotationExclusionStrategy;
import it.polimi.se2019.util.CustomFieldNamingStrategy;
import it.polimi.se2019.util.gson.extras.typeadapters.RuntimeTypeAdapterFactory;

public class ExpressionFactory {
    private ExpressionFactory() {
    }

    // GSON used to deal with serialization
    // TODO: register all subtypes (maybe use generics to automate)
    static RuntimeTypeAdapterFactory<Expression> makeRuntimeTypeAdapterFactory() {
        return RuntimeTypeAdapterFactory.of(Expression.class, "expr")
                .registerSubtype(Behaviour.class, "Behaviour")
                .registerSubtype(InflictDamage.class, "InflictDamage")
                .registerSubtype(DamageLiteral.class, "DamageLiteral")
                .registerSubtype(SelectOnePosition.class, "SelectOnePosition")
                .registerSubtype(SelectTargets.class, "SelectTargets")
                .registerSubtype(CanSee.class, "CanSee")
                .registerSubtype(TargetLiteral.class, "TargetLiteral")
                .registerSubtype(PositionLiteral.class, "PositionLiteral")
                .registerSubtype(PickEffect.class, "PickEffect")
                .registerSubtype(Move.class, "Move")
                .registerSubtype(ColorLiteral.class, "ColorLiteral")
                .registerSubtype(GetColors.class, "GetColors")
                .registerSubtype(DistanceRange.class, "DistanceRange")
                .registerSubtype(SetExpression.class, "SetExpression")
                .registerSubtype(Load.class, "Load")
                .registerSubtype(Pos.class, "Pos")
                .registerSubtype(XorEffect.class, "XorEffect")
                .registerSubtype(Difference.class, "Difference")
                .registerSubtype(Distance.class, "Distance")
                .registerSubtype(Union.class, "Union")
                .registerSubtype(All.class, "All")
                .registerSubtype(LastSelected.class, "LastSelected")
                .registerSubtype(AllInRoom.class, "AllInRoom")
                .registerSubtype(Others.class, "Others")
                .registerSubtype(Chain.class, "Chain")
                .registerSubtype(Do.class, "Do")
                .registerSubtype(SelectOneColor.class, "SelectOneColor")
                .registerSubtype(GetVisibleRange.class, "GetVisibleRange")
                .registerSubtype(Line.class, "Line")
                .registerSubtype(Intersect.class, "Intersect")
                .registerSubtype(Neighbours.class, "Neighbours")
                .registerSubtype(SelectOneDirection.class, "SelectOneDirection")
                .registerSubtype(InfLiteral.class, "InfLiteral")
                .registerSubtype(You.class, "You")
                .registerSubtype(IntLiteral.class, "IntLiteral")
                .registerSubtype(NegateTargets.class, "NegateSelection")
                .registerSubtype(Store.class, "Store")
                .registerSubtype(StringLiteral.class, "StringLiteral")
                .registerSubtype(Done.class, "Done")
                .registerSubtype(SelectOneTarget.class, "SelectOneTarget");
    }

    static Gson makeGsonDeserializer() {
        return new GsonBuilder()
                .registerTypeAdapter(Expression.class, new ExpressionParser())
                .registerTypeAdapter(AmmoValue.class, new AmmoValueDeserializer())
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
    public static Expression fromRawJson(JsonElement toDeserialize) {
        return makeGsonSerializer().fromJson(toDeserialize, Expression.class);
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
