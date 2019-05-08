package it.polimi.se2019.model.weapon.behaviour;

import com.google.gson.annotations.JsonAdapter;
import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.request.Request;
import it.polimi.se2019.model.weapon.serialization.CustomExpressionDeserializer;
import it.polimi.se2019.util.Exclude;
import it.polimi.se2019.util.FieldUtils;

import java.util.*;
import java.util.stream.Collectors;

@JsonAdapter(CustomExpressionDeserializer.class)
public abstract class Expression {
    @Exclude
    List<Expression> mSubexpressions = new ArrayList<>();

    // TODO: add doc
    protected Expression() {
        updateSubExpressions();
    }
    // TODO: add doc
    public static Expression fromJson(String jsonString) {
        return ExpressionFactory.fromJson(jsonString);
    }

    // trivial getters
    public List<Expression> getSubExpressions() {
        return mSubexpressions;
    }

    // TODO: add doc
    public String toJson() {
        return ExpressionFactory.toJson(this);
    }

    // TODO: add doc
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        Expression casted = (Expression) obj;

        return toJson().equals(casted.toJson());
    }

    // TODO: add doc
    @Override
    public int hashCode() {
        return Objects.hash(mSubexpressions);
    }

    // TODO: add doc
    public final Expression eval(ShootContext shootContext) {
        // first evaluate all subexpressions
        boolean infoIsMissing = false;
        for (Expression subexpr : mSubexpressions) {
            subexpr = subexpr.eval(shootContext);
            if (subexpr instanceof MissingInfo)
                infoIsMissing = true;
        }

        // if info is missing, return the expression partially evaluated
        if (infoIsMissing)
            return this;
        // else use info acquired from complete subexpressions to finish evaluation of
        // this expression
        else
            return continueEval(shootContext);
    }

    // TODO: add doc
    protected abstract Expression continueEval(ShootContext shootContext);

    // TODO: add doc and refine error messages
    int asInt() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    Set<PlayerColor> asTargets() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    Set<Position> asRange() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    Damage asDamage() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    Action asAction() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }
    Request asRequest() {
        throw new UnsupportedOperationException("This expression cannot be converted to an int!");
    }

    // TODO: add doc
    public void updateSubExpressions() {
        mSubexpressions = Arrays.stream(FieldUtils.getFieldsWithAnnotation(getClass(), SubExpression.class))
                .map(field -> {
                    if (!field.getClass().isAssignableFrom(Expression.class)) {
                        throw new IllegalStateException("Field " + field.getName() + " of class " + getClass().getName() +
                                "is annotated with a SubExpression annotation without being an Expression!");
                    }

                    return field;
                })
                .map(field -> {
                    try {
                        return field.get(this);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException("Cannot get field " + field.getName() + " of class " +
                                getClass().getName() + " annotated with subexpression annotation");
                    }
                })
                .map(obj -> (Expression) obj)
                .collect(Collectors.toList());
    }
}
