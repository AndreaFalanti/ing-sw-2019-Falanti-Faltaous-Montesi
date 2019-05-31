package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.serialization.ExpressionFactory;
import it.polimi.se2019.view.request.Request;

import java.util.Objects;
import java.util.Set;

// TODO: refine doc
public abstract class Expression {
    // FIELDS
    // cost needed to pay to activate expression
    private AmmoValue mCost;
    // TODO: add doc
    private int mPriority;
    // TODO: add doc
    private boolean mOptional;

    /**
     * Evaluates expressions depending on its type
     * @param context context used for evaluation
     * @return evaluated expression
     */
    public abstract Expression eval(ShootContext context);

    // TODO: add doc
    public static Expression fromJson(String jsonString) {
        return ExpressionFactory.fromJson(jsonString);
    }

    // TODO: add doc
    public String toJson() {
        return ExpressionFactory.toJson(this);
    }

    // TODO: add doc
    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass()) return false;

        AtomicExpression casted = (AtomicExpression) o;
        return toJson().equals(casted.toJson());
    }

    // TODO: add doc
    @Override
    public final int hashCode() {
        return Objects.hash(toJson());
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
    public Action asAction() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Action");
    }
    public Request asRequest() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Request");
    }
    public Response asResponse() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Response");
    }
}

