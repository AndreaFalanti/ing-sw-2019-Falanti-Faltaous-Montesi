package it.polimi.se2019.model.weapon;

import com.google.gson.annotations.JsonAdapter;
import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.behaviour.UnsupportedConversionException;
import it.polimi.se2019.model.weapon.serialization.ExpressionFactory;
import it.polimi.se2019.model.weapon.serialization.ExpressionParser;
import it.polimi.se2019.view.request.Request;

import java.util.Objects;
import java.util.Set;

// TODO: refine doc
public abstract class Expression {
    /**
     * Evaluates expressions depending on its type
     * @param context context used for evaluation
     * @return evaluated expression
     */
    public abstract Expression eval(ShootContext context);

    // TODO: add doc
    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass()) return false;

        Expression casted = (Expression) o;
        return ExpressionFactory.toJsonTree(this).equals(
                ExpressionFactory.toJsonTree(casted)
        );
    }

    // TODO: add doc
    @Override
    public final int hashCode() {
        return Objects.hash(ExpressionFactory.toJsonTree(this));
    }

    // TODO: add doc
    // TODO: maybe make faster
    public Expression deepCopy() {
        return ExpressionFactory.fromJson(ExpressionFactory.toJsonTree(this));
    }

    // inflict damage
    protected void inflictDamage(PlayerColor inflicterColor, Set<PlayerColor> to, Damage amount) {

    }

    // move player around
    protected void move(PlayerColor who, Position where) {

    }

    // safely discard result of evaluated expression by issuing a warning
    protected static Expression discardEvalResult(Expression result) {
        if (!result.isDone())
            // TODO: use logger (todof)
            System.out.println(
                    "WARNING:" + result.getClass().getSimpleName() + "was discarded after evaluation!"
            );

        return result;
    }

    // evaluation is done (it's only done in Done expression)
    public boolean isDone() {
        return false;
    }

    // conversions into primitives
    public int asInt() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "int");
    }
    public Set<PlayerColor> asTargets() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Targets");
    }
    public Set<Position> asRange() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Range");
    }
    public Damage asDamage() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Damage");
    }
    public String asString() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "String");
    }
}

