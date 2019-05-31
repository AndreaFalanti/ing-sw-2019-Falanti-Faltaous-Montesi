package it.polimi.se2019.model.weapon.behaviour;

import com.google.gson.annotations.SerializedName;
import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.serialization.ExpressionFactory;
import it.polimi.se2019.view.request.Request;

import java.util.*;

public abstract class Expression {

    /******************/
    /* private fields */
    /******************/

    // subexpressions evaluated before their parent expression and used in its evaluation
    @SerializedName("subs")
    private Map<String, Expression> mSubexpressions = new HashMap<>();

    // cost needed to pay to activate expression
    private AmmoValue mCost;
    // TODO: add doc
    private int mPriority;
    // TODO: add doc
    private boolean mOptional;

    // default constructor to fill in default values during deserialization
    public Expression() {
        // TODO: try and see if it works
    }

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

        Expression casted = (Expression) o;
        return toJson().equals(casted.toJson());
    }

    // TODO: add doc
    @Override
    public final int hashCode() {
        return Objects.hash(mSubexpressions);
    }

    // TODO: add doc
    public Expression getSub(String name) {
        if (mSubexpressions == null)
            throw new IllegalStateException("FATAL: " + getClass().getSimpleName() + " contains no subexpressions!");

        Expression result = mSubexpressions.get(name);

        if (result == null)
            throw new IllegalArgumentException(
                    getClass().getSimpleName() + " has no subexpression named " + name
            );

        return mSubexpressions.get(name);
    }

    // TODO: add doc
    public Expression putSub(String name, Expression subexpr) {
        return mSubexpressions.put(name, subexpr);
    }

    // TODO: add doc
    public Map<String, Expression> getSubexpressions() {
        return mSubexpressions;
    }

    // TODO: add doc
    private boolean evalSubexpressions(ShootContext shootContext) {
        // there must be a subexpression list
        if (mSubexpressions == null)
            throw new IllegalStateException("FATAL: subexpression map is missing from expression!");

        // get subexpressions mask

        // evaluate subexpressions
        for (Map.Entry<String, Expression> entry : mSubexpressions.entrySet()) {
            String subexprName = entry.getKey();
            Expression subexpr = entry.getValue();

            if (subexpr == null)
                throw new IllegalStateException("FATAL: subexpression was found null during evaluation!");

            EvalResult evalResult = subexpr.evalToEvalResult(shootContext);

            mSubexpressions.put(subexprName, evalResult.evaluatedExpression);

            if (evalResult.shouldStopEval || (evalResult.evaluatedExpression instanceof WaitForInfo)) {
                System.out.println(getClass().getSimpleName() + "'s subexpression (" + subexprName + ") is stopping!");
                return true;
            }
        }

        return false;
    }

    /**
     * Simple utility class to contain the result of eval
     */
    // TODO: make eval interface better
    private static class EvalResult {
        EvalResult(boolean shouldStopEval, Expression evaluatedExpression) {
            this.shouldStopEval = shouldStopEval;
            this.evaluatedExpression = evaluatedExpression;
        }

        boolean shouldStopEval = false;
        Expression evaluatedExpression = null;
    }
    // TODO: add doc
    private EvalResult evalToEvalResult(ShootContext shootContext) {
        // evaluate all registered subexpressions
        boolean shouldStopEval = evalSubexpressions(shootContext);

        // if evaluation should stop, freeze expression tree by stopping evaluation of parent expression
        // along with all its parents and ancestors
        if (shouldStopEval)
            return new EvalResult(true, this);
        // else continue evaluation of expression tree (for now)
        else
            return new EvalResult(false, continueEval(shootContext));
    }

    // TODO: add doc
    protected Expression eval(ShootContext shootContext) {
        return evalToEvalResult(shootContext).evaluatedExpression;
    }

    // TODO: add doc
    public final ShootResult evalToShootResult(ShootContext context) {
        Expression exprResult = eval(context);

        return exprResult.isDone() ?
                ShootResult.from(context.getResultingAction()) :
                ShootResult.from(context.popRequestedInfo().asResponse());
    }

    // TODO: add doc
    protected final Expression requestInfoFromPlayer(ShootContext context, Expression infoToRequest) {
        // if info is available, consume it
        if (context.peekProvidedInfo().isPresent())
            return context.popProvidedInfo();

        // else, request it...
        context.requestInfo(infoToRequest);

        // ...and start waiting for it
        return new WaitForInfo(infoToRequest);
    }

    // TODO: add doc
    public boolean isDone() {
        return false;
    }

    // TODO: add doc
    protected  abstract Expression continueEval(ShootContext shootContext);

    // TODO: add doc and refine error messages
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
