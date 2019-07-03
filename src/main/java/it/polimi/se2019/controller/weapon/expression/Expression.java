package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.model.weapon.serialization.ExpressionFactory;

import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: refine doc
public abstract class Expression {
    // logger
    protected static final Logger LOGGER = Logger.getLogger(Expression.class.getName());

    /**
     * Construct empty expression
     */
    public Expression() {

    }

    /**
     * Evaluates expressions depending on its type
     * @param context context used for evaluation
     * @return result of evaluation
     */
    public abstract Expression eval(ShootContext context);

    /**
     * Checks if two expressions are the same
     * @param o the other expression
     * @return true if the two expressions are the same
     */
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

    /**
     * Returns the hashcode corresponding to {@code this}
     * @return the hashcode corresponding to {@code this}
     */
    @Override
    public final int hashCode() {
        return Objects.hash(ExpressionFactory.toJsonTree(this));
    }

    /**
     * Returns a deep copy of the expression
     * @return a deep copy of the expression
     */
    public Expression deepCopy() {
        return ExpressionFactory.fromRawJson(ExpressionFactory.toJsonTree(this));
    }

    /**
     * safely discard result of evaluated expression by issuing a warning
     */
    protected static Expression discardEvalResult(Expression result) {
        if (!result.isDone())
            LOGGER.log(Level.WARNING,
                    "{0} was discarded after evaluation!",
                    result.getClass().getSimpleName()
            );

        return result;
    }

    /**
     * True if evaluation is considered done
     */
    public boolean isDone() {
        return false;
    }

    /**
     * True if the expression represents infinity
     */
    public boolean isInf() {
        return false;
    }

    /**
     * Convert the expression to an int (if possible)
     * @return an int representation of the expression
     */
    public int asInt() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "int");
    }

    /**
     * Convert the expression to targets (if possible)
     * @return a target set representation of the expression
     */
    public Set<PlayerColor> asTargets() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Targets");
    }

    /**
     * Convert the expression to a target (if possible)
     * @return a target representation of the expression
     */
    public PlayerColor asTarget() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Target");
    }

    /**
     * Convert the expression to an range (if possible)
     * @return a range representation of the expression
     */
    public Set<Position> asRange() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Range");
    }

    /**
     * Convert the expression to a position (if possible)
     * @return a position representation of the expression
     */
    public Position asPosition() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Position");
    }

    /**
     * Convert the expression to a damage value (if possible)
     * @return a damage value representation of the expression
     */
    public Damage asDamage() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Damage");
    }

    /**
     * Convert the expression to a string (if possible)
     * @return a string representation of the expression
     */
    public String asString() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "String");
    }

    /**
     * Convert the expression to a direction (if possible)
     * @return a direction representation of the expression
     */
    public Direction asDirection() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Direction");
    }

    /**
     * Convert the expression to a set expression (if possible)
     * @return a set expression representation of the expression
     */
    public SetExpression asSetExpr() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "SetExpression");
    }

    /**
     * Convert the expression to a color value (if possible)
     * @return a color value representation of the expression
     */
    public TileColor asColor() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Color");
    }

    /**
     * Convert the expression to a set of color values (if possible)
     * @return a set of color values representation of the expression
     */
    public Set<TileColor> asColors() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Colors");
    }
}

