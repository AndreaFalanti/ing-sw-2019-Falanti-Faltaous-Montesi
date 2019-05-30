package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.weapon.behaviour.DamageLiteral;
import it.polimi.se2019.model.weapon.behaviour.IntLiteral;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Exists to parse pretty (more readable) definition of expression in json into that can be more
 * easily serialized by the Gson library
 */
public class ExpressionParser {
    protected static final Set<String> EXCLUSIVE_KEYWORDS = new HashSet<>(Arrays.asList(
            "cost",
            "subs",
            "expr",
            "contents",
            "priority",
            "optional"
    ));

    private ExpressionParser() {}

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

    // parses a cost string literal
    private static JsonElement parseCostStringLiteral(JsonPrimitive rawPrimitive) {
        throw new UnsupportedOperationException("WIP");
    }

    // parses a cost string literal
    private static JsonElement parseDamageStringLiteral(JsonPrimitive rawPrimitive) {
        return ExpressionFactory.toJsonTree(new DamageLiteral(
                Damage.from(rawPrimitive.getAsString())
                        .orElseThrow(() -> new IllegalArgumentException(
                                rawPrimitive.getAsString() + " could not be parsed"))
        ));
    }

    // parses a primitive
    private static JsonElement parsePrimitive(JsonElement raw) {
        JsonPrimitive rawPrimitive = raw.getAsJsonPrimitive();

        // parse numbers
        if (rawPrimitive.isNumber()) {
            return parseNumber(rawPrimitive);
        }
        // parse strings
        if (rawPrimitive.isString()) {
            if (isCostStringLiteral(rawPrimitive))
                return parseCostStringLiteral(rawPrimitive);
            else if(isDamageStringLiteral(rawPrimitive))
                return parseDamageStringLiteral(rawPrimitive);
        }

        // if this point is reached, then a malformed primitive has been encountered
        throw new IllegalArgumentException(
                raw.toString() + " is a malformed primitive and cannot be parsed."
        );
    }

    // parse a number into a number literal expression
    private static JsonElement parseNumber(JsonPrimitive rawPrimitive) {
        return ExpressionFactory.toJsonTree(
                new IntLiteral(rawPrimitive.getAsInt())
        );
    }

    // parse a complex expression
    private static JsonElement parseComplexExpression(JsonElement rawPrimitive) {
        JsonObject jExpression = rawPrimitive.getAsJsonObject();

        JsonObject result = new JsonObject();
        result.add("subs", new JsonObject());

        for (Map.Entry<String, JsonElement> entry : jExpression.entrySet()) {
            String subName = entry.getKey();
            JsonElement jSub = entry.getValue();

            if (!EXCLUSIVE_KEYWORDS.contains(subName))
                result.get("subs").getAsJsonObject().add(subName, parse(jSub));
            else
                result.add(subName, jSub);
        }

        return result;
    }

    /**
     * Do what this class is supposed to do
     */
    public static JsonElement parse(JsonElement raw) {
        if (isPrimitive(raw))
            return parsePrimitive(raw);

        // else treat it as a complex expression
        return parseComplexExpression(raw);
    }
}
