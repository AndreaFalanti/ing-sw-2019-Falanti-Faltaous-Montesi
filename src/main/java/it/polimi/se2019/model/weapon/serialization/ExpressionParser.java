package it.polimi.se2019.model.weapon.serialization;

import com.google.gson.*;
import it.polimi.se2019.model.weapon.behaviour.Expression;

import java.lang.reflect.Type;
import java.util.Map;

public class ExpressionParser implements JsonDeserializer<Expression>, JsonSerializer<Expression> {
    private static String EXPRESSION_PACKAGE_NAME = "it.polimi.se2019.model.weapon.behaviour";

    @Override
    public Expression deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        // get class from class name
        String typeString = jsonElement.getAsJsonObject().get("expr").toString();
        Class<Expression> expressionType = null;
        try {
            System.out.println("Instatiating " + typeString + "\n");
            expressionType = (Class<Expression>) Class.forName(EXPRESSION_PACKAGE_NAME + "." + typeString);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find class");
        }

        // instantiate empty expression from class name
        Expression result = null;
        try {
            result = expressionType.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate object from class");
        }

        // add subexpressions
        JsonObject jSubDict = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> jEntry : jSubDict.entrySet()) {
            if (!jEntry.getKey().equals("expr")) {
                String subName = jEntry.getKey();
                Expression parsedSubexpr = deserialize(jEntry.getValue(), Expression.class, context);

                result.putSub(jEntry.getKey(), parsedSubexpr);
            }
        }

        // return result
        return result;
    }

    @Override
    public JsonElement serialize(Expression expression, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        // determine type
        result.add("expr", new JsonPrimitive(expression.getClass().getSimpleName()));

        // add subs
        for (Map.Entry<String, Expression> entry : expression.getSubexpressions().entrySet()) {
            JsonElement serializedSub = serialize(entry.getValue(), Expression.class, context);

            result.add(entry.getKey(), serializedSub);
        }

        // result
        return result;
    }
}
