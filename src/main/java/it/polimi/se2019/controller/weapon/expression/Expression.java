package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.controller.weapon.EvaluationInterruptedException;
import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.controller.weapon.*;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.weapon.serialization.ExpressionFactory;
import it.polimi.se2019.util.Exclude;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: refine doc
public abstract class Expression {
    // logger
    protected static final Logger LOGGER = Logger.getLogger(Expression.class.getName());

    // trivial constructor
    public Expression() {

    }

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
        return ExpressionFactory.fromRawJson(ExpressionFactory.toJsonTree(this));
    }

    // safely discard result of evaluated expression by issuing a warning
    protected static Expression discardEvalResult(Expression result) {
        if (!result.isDone())
            LOGGER.log(Level.WARNING,
                    "{0} was discarded after evaluation!",
                    result.getClass().getSimpleName()
            );

        return result;
    }

    // evaluation is done (it's only done in Done expression)
    public boolean isDone() {
        return false;
    }

    // true if the expression represents infinity
    public boolean isInf() {
        return false;
    }

    // conversions into primitives
    public int asInt() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "int");
    }
    public Set<PlayerColor> asTargets() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Targets");
    }
    public PlayerColor asTarget() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Target");
    }
    public Set<Position> asRange() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Range");
    }
    public Position asPosition() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Position");
    }
    public Damage asDamage() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Damage");
    }
    public String asString() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "String");
    }
    public Direction asDirection() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Direction");
    }
    public SetExpression asSetExpr() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "SetExpression");
    }
}

