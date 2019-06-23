package it.polimi.se2019.controller.weapon.expression;

import com.google.gson.annotations.SerializedName;
import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetExpression extends Expression {
    @SerializedName("subs")
    private final Set<Expression> mSubexpressions;

    /**
     * Constructs an empty set expression
     */
    public SetExpression() {
        mSubexpressions = new HashSet<>();
    }

    /**
     * Constructs SetExpression from set of subexpressions
     * @param subs subexpressions of constructed SetExpression
     */
    public SetExpression(Set<Expression> subs) {
        mSubexpressions = subs;
    }

    /**
     * Converts {@code this} into a set
     * @return a set containing {@code this}'s subexpressions
     */
    public Set<Expression> asSet() {
        return mSubexpressions;
    }

    /**
     * Turn {@code this} into a stream
     * @return a stream consisting of {@code this}'s subexpressions
     */
    public Stream<Expression> stream() {
        return mSubexpressions.stream();
    }

    @Override
    public final Expression eval(ShootContext context) {
        // return SetExpression of all evaluated expressions
        return new SetExpression(stream()
                        .map(expr -> expr.eval(context))
                        .collect(Collectors.toSet())
        );
    }

    // utility conversion to primitives
    @Override
    public PlayerColor asTarget() {
        if (mSubexpressions.size() == 1)
            return mSubexpressions.iterator().next().asTarget();

        if (mSubexpressions.isEmpty())
            throw new UnsupportedConversionException("SetExpression (empty)", "Target");

        else
            throw new UnsupportedConversionException("SetExpression (more than one element)", "Target");
    }

    @Override
    public Position asPosition() {
        if (mSubexpressions.size() == 1)
            return mSubexpressions.iterator().next().asPosition();

        if (mSubexpressions.isEmpty())
            throw new UnsupportedConversionException("SetExpression (empty)", "Position");

        else
            throw new UnsupportedConversionException("SetExpression (more than one element)", "Position");
    }

    @Override
    public Set<PlayerColor> asTargets() {
        return stream()
                .map(Expression::asTarget)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Position> asRange() {
        return stream()
                .map(Expression::asPosition)
                .collect(Collectors.toSet());
    }

    @Override
    public SetExpression asSetExpr() {
        return this;
    }
}
