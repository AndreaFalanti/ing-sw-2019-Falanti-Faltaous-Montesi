package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.Selection;
import it.polimi.se2019.model.weapon.request.Request;
import it.polimi.se2019.model.weapon.serialization.ExpressionFactory;
import it.polimi.se2019.util.Exclude;
import it.polimi.se2019.util.FieldUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Expression {
    @Exclude
    private List<Expression> mSubexpressions;

    // TODO: add doc
    protected Expression() {
        mSubexpressions = new ArrayList();
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
    public final boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;
        
        Expression casted = (Expression) o;
        return toJson().equals(casted.toJson());
    }

    // TODO: add doc
    @Override
    public final int hashCode() {
        return Objects.hash(mSubexpressions);
    }

    public <AnnotationType, AnnotatedThing>
    void evalAnnotatedFields(Class<AnnotationType> annotationClass,
                             Function<AnnotatedThing, AnnotatedThing> evaluator,
                             Predicate<AnnotatedThing> evalStopper) {
        // evaluate all tagged subexpressions
        Arrays.stream(getClass().getDeclaredFields())
                .filter(field -> Arrays.stream(field.getDeclaredAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().equals(annotationClass)))
                .forEachOrdered(field -> {
                    // evaluate subexpression
                    field.setAccessible(true);
                    try {
                        // get annotated thing
                        AnnotatedThing annotatedThing = (AnnotatedThing) field.get(this);

                        // stop eval if any info is missing
                        final boolean shouldStopEval = evalStopper.test(annotatedThing);

                        // evaluate all subexpressions in tagged subexpression list
                        if (!shouldStopEval) {
                            field.set(this, evaluator.apply(annotatedThing));
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("LOGIC ERROR: could not access accessible field");
                    } catch (ClassCastException e) {
                        throw new IllegalStateException("field " + field.getName() + " of class " +
                                getClass().getSimpleName() + " is tagged as a SubExpressionList but is not a subtype " +
                                "of List<Expression>");
                    }
                });
    }

    // TODO: add doc
    private boolean evalRegisteredSubexpressionLists(ShootContext shootContext) {
        // evaluate all tagged subexpression lists
        boolean shouldStopEval = false;
        for (Field field : FieldUtils.getFieldsWithAnnotation(getClass(), SubExpressionList.class)) {

            // evaluate subexpression
            field.setAccessible(true);
            try {
                // get subexpression list
                List<Expression> subexprList = (List<Expression>) field.get(this);

                // get evaluation results of all subexpressions
                Supplier<Stream<EvalResult>> evalResults = () -> subexprList.stream()
                        .map(subexpr -> subexpr.evalToEvalResult(shootContext));

                // update class members containing subexpressions
                field.set(this, evalResults.get().map(res -> res.evaluatedExpression)
                        .collect(Collectors.toList()));

                // find out if evaluation should be continued
                shouldStopEval |= evalResults.get()
                        .anyMatch(res -> res.shouldStopEval || res.evaluatedExpression instanceof WaitForInfo);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("LOGIC ERROR: could not access accessible field");
            } catch (ClassCastException e) {
                throw new IllegalStateException("field " + field.getName() + " of class " +
                        getClass().getSimpleName() + " is tagged as a SubExpressionList but is not a subtype " +
                        "of List<Expression>");
            }
        }

        return shouldStopEval;
    }

    // TODO: add doc
    // TODO NEXT: implement
    private boolean evalRegisteredSubexpressions(ShootContext shootContext) {
        boolean shouldStopEval = false;

        // evaluate all tagged subexpression lists
        for (Field field : FieldUtils.getFieldsWithAnnotation(getClass(), SubExpression.class)) {
            field.setAccessible(true);
            try {
                // get subexpressions
                Expression subexpr = (Expression) field.get(this);

                // evaluate it
                EvalResult evalResult = subexpr.evalToEvalResult(shootContext);

                // determine if evaluation should be stopped
                shouldStopEval |= evalResult.shouldStopEval || evalResult.evaluatedExpression instanceof WaitForInfo;

                // set class member corresponding to evaluated subexpression
                field.set(this, evalResult.evaluatedExpression);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("LOGIC ERROR: could not access accessible field");
            } catch (ClassCastException e) {
                throw new IllegalStateException("field " + field.getName() + " of class " +
                        getClass().getSimpleName() + " is tagged as a SubExpression but is not a subtype " +
                        "of Expression");
            }
        }

        return shouldStopEval;
    }

    /**
     * Simple utility class to contain the result of eval
     */
    private static class EvalResult {
        EvalResult(boolean shouldStopEval, Expression evaluatedExpression) {
            this.shouldStopEval = shouldStopEval;
            this.evaluatedExpression = evaluatedExpression;
        }

        boolean shouldStopEval = false;
        Expression evaluatedExpression = null;
    }
    // TODO: add doc
    private final EvalResult evalToEvalResult(ShootContext shootContext) {
        // evaluate all registered subexpressions
        boolean shouldStopEval = false;
        shouldStopEval |= evalRegisteredSubexpressions(shootContext);
        shouldStopEval |= evalRegisteredSubexpressionLists(shootContext);

        // if evaluation should stop, freeze expression tree by stopping evaluation of parent expression
        // along with all its parents and ancestors
        if (shouldStopEval)
            return new EvalResult(true, this);
        // else continue evaluation of expression tree (for now)
        else
            return new EvalResult(false, continueEval(shootContext));
    }

    // TODO: add doc
    public final Expression eval(ShootContext shootContext) {
        return evalToEvalResult(shootContext).evaluatedExpression;
    }

    // TODO: add doc
    protected abstract Expression continueEval(ShootContext shootContext);

    // TODO: add doc and refine error messages
    int asInt() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "int");
    }
    Selection<PlayerColor> asTargetSelection() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "TargetSelection");
    }
    Selection<Position> asRange() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Range");
    }
    Selection<?> asSelection() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Selection<?>");
    }
    Damage asDamage() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Damage");
    }
    Action asAction() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Action");
    }
    Request asRequest() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Request");
    }
}
