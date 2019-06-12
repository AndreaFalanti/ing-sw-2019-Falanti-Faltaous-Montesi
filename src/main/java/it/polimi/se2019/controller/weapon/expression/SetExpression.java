package it.polimi.se2019.controller.weapon.expression;

import com.google.gson.annotations.SerializedName;
import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetExpression extends Expression {
    @SerializedName("subs")
    private Set<Expression> mSubexpressions;

    /**
     * Constructs SetExpression from list of subexpressions
     * @param subs subexpressions of constructed SetExpression
     */
    public SetExpression(Expression... subs) {
        mSubexpressions = Arrays.stream(subs).collect(Collectors.toSet());
    }

    /**
     * Trivial constructor
     * @param subexpressions subexpressions of {@code this}
     */
    public static SetExpression from(Set<Expression> subexpressions) {
        SetExpression result = new SetExpression();

        result.mSubexpressions = subexpressions;

        return result;
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
    public Expression eval(ShootContext context) {
        // return SetExpression of all evaluated expressions
        return from(stream()
                        .map(expr -> expr.eval(context))
                        .collect(Collectors.toSet())
        );
    }

    // utility conversion to primitives
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
}
