package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.controller.weapon.expression.*;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Damage;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static it.polimi.se2019.controller.weapon.ShootContext.SPECIAL_VAR_PREVIOUSLY_SELECTED;

/**
 * Exists to parse pretty (more readable) definition of expression in json into that can be more
 * easily serialized by the Gson library
 */
public class ExpressionParser implements JsonDeserializer<Expression> {
    private static final String EXPRESSION_PACKAGE_NAME = "it.polimi.se2019.controller.weapon.expression";
    private static final String STORE_KEYWORD = "store";
    private static final String EXCLUDE_PREVIOUS_SELECTIONS_KEYWORD = "exclude_previous_selections";
    private static final String EXPR_KEYWORD = "expr";
    private static final Set<String> PROHIBITED_KEYWORDS = new HashSet<>(Arrays.asList(
            "subs",
            "contents"
    ));
    private static final Set<String> RESERVED_KEYWORDS = new HashSet<>(Arrays.asList(
            EXPR_KEYWORD,
            STORE_KEYWORD,
            EXCLUDE_PREVIOUS_SELECTIONS_KEYWORD
    ));
    private static final Set<String> TRIVIALLY_DESERIALIZABLE_EXPRESSION_TYPES = new HashSet<>(Arrays.asList(
            "XorEffect",
            "Do"
    ));

    // identifies if expression is a primitive
    private static boolean isPrimitive(JsonElement raw) {
        return raw.isJsonPrimitive();
    }

    // identifies strings identifying a damage value
    private static boolean isDamageStringLiteral(JsonPrimitive rawPrimitive) {
        return rawPrimitive.isString() &&
                Damage.from(rawPrimitive.getAsString()).isPresent();
    }

    // identifies strings identifying an cost value
    private static boolean isCostStringLiteral(JsonPrimitive rawPrimitive) {
        return rawPrimitive.isString() &&
                AmmoValue.from(rawPrimitive.getAsString()).isPresent();
    }

    // identifies a pure string
    private static boolean isPureStringLiteral(JsonPrimitive rawPrimitive) {
        return rawPrimitive.isString();
    }

    // identifies a pick effect expression
    private static boolean isPickEffectExpression(JsonElement raw) {
        return raw.isJsonObject() &&
                raw.getAsJsonObject().has(EXPR_KEYWORD) &&
                raw.getAsJsonObject().get(EXPR_KEYWORD).equals(
                        new JsonPrimitive("PickEffect")
                );
    }

    // identifies a trivially deserializable expression
    private static boolean isTriviallyDeserializableExpression(JsonElement raw) {
        return raw.isJsonObject() &&
                raw.getAsJsonObject().has(EXPR_KEYWORD) &&
                TRIVIALLY_DESERIALIZABLE_EXPRESSION_TYPES.contains(
                        raw.getAsJsonObject().get(EXPR_KEYWORD).getAsString()
                );
    }

    // identifies a behavioural expression
    private static boolean isBehaviour(JsonElement raw) {
        return raw.isJsonObject() &&
                raw.getAsJsonObject().has(EXPR_KEYWORD);
    }

    // parses a cost string literal
    private static Expression parseCostStringLiteral(JsonPrimitive rawPrimitive) {
        throw new UnsupportedOperationException("WIP");
    }

    // parses a cost string literal
    private static Expression parseDamageStringLiteral(JsonPrimitive rawPrimitive) {
        return new DamageLiteral(
                Damage.from(rawPrimitive.getAsString())
                        .orElseThrow(() -> new IllegalArgumentException(
                                rawPrimitive.getAsString() + " could not be parsed"))
        );
    }

    // parses a pure string literal
    private static Expression parsePureStringLiteral(JsonPrimitive rawPrimitive) {
        return new StringLiteral(rawPrimitive.getAsString());
    }

    // parses a primitive
    private static Expression parsePrimitive(JsonElement raw) {
        JsonPrimitive rawPrimitive = raw.getAsJsonPrimitive();

        // parse numbers
        if (rawPrimitive.isNumber()) {
            return parseNumber(rawPrimitive);
        }
        // parse strings
        if (rawPrimitive.isString()) {
            if (isCostStringLiteral(rawPrimitive))
                return parseCostStringLiteral(rawPrimitive);
            else if (isDamageStringLiteral(rawPrimitive))
                return parseDamageStringLiteral(rawPrimitive);
            // this has to be last
            else if (isPureStringLiteral(rawPrimitive))
                return parsePureStringLiteral(rawPrimitive);
        }

        // if this point is reached, then a malformed primitive has been encountered
        throw new IllegalArgumentException(
                raw.toString() + " is a malformed primitive and cannot be parsed."
        );
    }

    // parse a number into a number literal expression
    private static Expression parseNumber(JsonPrimitive rawPrimitive) {
        return new IntLiteral(rawPrimitive.getAsInt());
    }

    // parses a parsePickEffectExpression
    private static Expression parsePickEffectExpression(JsonElement raw, JsonDeserializationContext context) {
        PickEffect result = new PickEffect();

        raw.getAsJsonObject().get("effects").getAsJsonArray().iterator().forEachRemaining(ele -> {
            JsonObject jEff = ele.getAsJsonObject();

            result.addEffect(context.deserialize(jEff, Effect.class));
        });

        return result;
    }

    // get the type of an expression from its string representation
    private static Class<? extends Expression> getExpressionTypeFromString(String strType) {
        try {
            return Class.forName(EXPRESSION_PACKAGE_NAME + "." + strType).asSubclass(Expression.class);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Something went wrong during default initialization of " + strType + " expression: " + e
            );
        }
    }

    // create a default constructed expression from a string representation of its type
    private static Behaviour createDefaultConstructedBehaviour(String strType) {
        try {
            Class type = Class.forName(EXPRESSION_PACKAGE_NAME + "." + strType);
            return (Behaviour) (type.newInstance());
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Something went wrong during default initialization of " + strType + " expression: " + e
            );
        }
    }

    // parse a complex expression
    private static Expression parseBehaviour(JsonElement raw, JsonDeserializationContext context) {
        JsonObject jExpression = raw.getAsJsonObject();

        // find expression class and call its default constructor
        //   NB: this is important for setting default subexpression values
        Behaviour result = createDefaultConstructedBehaviour(
                jExpression.get(EXPR_KEYWORD).getAsString()
        );

        // add subexpressions
        for (Map.Entry<String, JsonElement> entry : jExpression.entrySet()) {
            String subName = entry.getKey();
            JsonElement jSub = entry.getValue();

            if (PROHIBITED_KEYWORDS.contains(subName))
                throw new IllegalArgumentException(
                        subName + " is a reserved keyword and cannot be used as a subexpression name"
                );
            else if (!RESERVED_KEYWORDS.contains(subName))
                result.putSub(subName, parse(jSub, context));
        }

        // decorate with expression excluding previous selections if specified
        if (jExpression.has(EXCLUDE_PREVIOUS_SELECTIONS_KEYWORD)) {
            result = new Difference(
                    result,
                    new Load(
                            new StringLiteral(SPECIAL_VAR_PREVIOUSLY_SELECTED)
                    )
            );
        }

        // decorate with store expression if specified
        if (jExpression.has(STORE_KEYWORD)) {
            result = new Store(
                    new StringLiteral(jExpression.get(STORE_KEYWORD).getAsString()),
                    result
            );
        }

        return result;
    }

    public static Expression parse(JsonReader reader) {
        return parse(
                new Gson().toJsonTree(reader.toString()),
                new JsonDeserializationContext() {
                    @Override
                    public <T> T deserialize(JsonElement jsonElement, Type type) {
                        throw new UnsupportedOperationException("This should not happen...");
                    }
                }
        );
    }

    /**
     * Do what this class is supposed to do
     */
    public static Expression parse(JsonElement raw, JsonDeserializationContext context) {
        if (isPrimitive(raw))
            return parsePrimitive(raw);

        else if (isTriviallyDeserializableExpression(raw)) {
            return ExpressionFactory.makeGsonDeserializer()
                    .fromJson(
                            raw,
                            getExpressionTypeFromString(raw.getAsJsonObject().get(EXPR_KEYWORD).getAsString())
                    );
        }

        else if (isPickEffectExpression(raw))
            return parsePickEffectExpression(raw, context);

        // this needs to remain last
        else if (isBehaviour(raw))
            return parseBehaviour(raw, context);

        else
            throw new IllegalArgumentException(
                    "Cannot identify type of expression while parsing! Malformed expression: " + raw
            );
    }

    @Override
    public Expression deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        return parse(jsonElement, context);
    }
}
