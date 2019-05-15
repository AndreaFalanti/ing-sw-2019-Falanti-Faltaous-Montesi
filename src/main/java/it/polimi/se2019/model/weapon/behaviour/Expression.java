package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.Selection;
import it.polimi.se2019.model.weapon.request.Request;
import it.polimi.se2019.model.weapon.serialization.ExpressionFactory;
import it.polimi.se2019.util.Exclude;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Expression {
    @Exclude
    List<Expression> mSubexpressions;

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
    public final Expression eval(ShootContext shootContext) {
        // evaluate all tagged subexpression lists
        Arrays.stream(getClass().getDeclaredFields())
                .filter(field -> Arrays.stream(field.getDeclaredAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().equals(SubExpressionList.class)))
                .forEachOrdered(field -> {
                    // evaluate subexpression
                    field.setAccessible(true);
                    try {
                        List<Expression> subexprList = (List<Expression>) field.get(this);

                        // stop eval if any info is missing
                        // TODO: eliminate instanceof
                        final boolean shouldStopEval = subexprList.stream()
                                .anyMatch(subexpr -> subexpr instanceof WaitForInfo);

                        // evaluate all subexpressions in tagged subexpression list
                        if (!shouldStopEval) {
                            field.set(this, subexprList.stream()
                                    .map(subexpr -> subexpr.eval(shootContext))
                                    .collect(Collectors.toList()));
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("LOGIC ERROR: could not access accessible field");
                    } catch (ClassCastException e) {
                        throw new IllegalStateException("field " + field.getName() + " of class " +
                                getClass().getSimpleName() + " is tagged as a SubExpressionList but is not a subtype " +
                                "of List<Expression>");
                    }

                    // exit if info is missing
                    // TODO: remove isAssignableFrom
                    if (field.getClass().isAssignableFrom(WaitForInfo.class)) {
                        return;
                    }
                });

        // evaluate all tagged subexpression lists
        Arrays.stream(getClass().getDeclaredFields())
                .filter(field -> Arrays.stream(field.getDeclaredAnnotations())
                        .anyMatch(annotation -> annotation.annotationType() == SubExpression.class))
                .forEachOrdered(field -> {
                    field.setAccessible(true);
                    try {
                        // get subexpressions
                        Expression subexpr = (Expression) field.get(this);

                        // TODO: eliminate instanceof
                        if (!(subexpr instanceof WaitForInfo))
                            field.set(this, ((Expression) field.get(this)).eval(shootContext));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("LOGIC ERROR: could not access accessible field");
                    } catch (ClassCastException e) {
                        throw new IllegalStateException("field " + field.getName() + " of class " +
                                getClass().getSimpleName() + " is tagged as a SubExpression but is not a subtype " +
                                "of Expression");
                    }
                });

        // proceed with custom evaluation of current expression
        return continueEval(shootContext);
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
