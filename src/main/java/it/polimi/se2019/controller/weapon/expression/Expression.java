package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.weapon.serialization.ExpressionFactory;

import java.util.Objects;
import java.util.Set;

// TODO: refine doc
public abstract class Expression {
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

    // inflict damage
    protected static void inflictDamage(ShootContext context, PlayerColor inflicter, Set<PlayerColor> inflicted, Damage amount) {
        Game game = context.getGame();

        System.out.println(inflicter + " inflicting " + amount + " damage to " + inflicted);

        inflicted.forEach(
                singularInflicted -> game.handleDamageInteraction(inflicter, singularInflicted, amount)
        );
    }

    // move player around
    protected static void move(ShootContext context, Set<PlayerColor> who, Position where) {
        Game game = context.getGame();

        who
                .forEach(pl -> game.getPlayerFromColor(pl).move(where));
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

