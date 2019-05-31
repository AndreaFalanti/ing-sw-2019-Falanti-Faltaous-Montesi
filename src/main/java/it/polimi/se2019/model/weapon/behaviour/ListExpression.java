package it.polimi.se2019.model.weapon.behaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListExpression extends Expression {
    // subexpressions
    private Map<Integer, List<Expression>> mSubexpressions;

    // maximum priority encountered
    private int mMaximumPriority = 0;

    /**
     * Add a subexpression
     * @param value subexpression to add
     */
    public void addSub(Expression value) {
        // update maximum priority
        if (value.getPriority() > mMaximumPriority)
            mMaximumPriority = value.getPriority();

        // get subexpression list corresponding to the added subexpression's priority
        List<Expression> correspondingPriorityList = mSubexpressions.get(
                value.getPriority(),
                mSubexpressions.getOrDefault(value.getPriority(), new ArrayList<>())
        );

        // add subexpression
        correspondingPriorityList.add(value);
    }

    @Override
    EvalResult evalToEvalResult(ShootContext context) {
        // get list of expressions corresponding to current priority
        List<Expression> currentSubs = mPriorityMap.getOrDefault(
                context.getCurrentPriority(),
                new ArrayList<>()
        );

        // if no subs present in this priority, keep evaluating with higher priority until maximum is reached
        if (currentSubs.isEmpty()) {
            // exit if maximum is reached
            if (context.getCurrentPriority() >= mMaxPriority)
                return new EvalResult(false, new Done());

            // go on to next priority if not
            context.incrementPriority();
            return evalToEvalResult(context);
        }

        // do not request which sub to choose from view if there is only one that is forced
        else if (currentSubs.size() == 1 && !currentSubs.get(0).isOptional()) {
            return new EvalResult(false, currentSubs.get(0).eval(context));
        }
        // else request which expression to choose from view
        else {
            Response toAsk = new PickWeaponEffectResponse(
                    mPriorityMap.entrySet().stream()
                            .map(entry ->
                                    new Map.Entry<>(entry.getKey(), entry.getValue().stream()
                                            .map(WeaponEffectInfo::from)))
                            .collect(Collectors.toMap(
                                    entry -> entry.getKey(),
                                    entry -> entry.getValue()
                            ))
            );

            return new EvalResult(
                    false,
                    requestInfoFromPlayer(context, new ResponseLiteral(toAsk))
            );
        }
    }
}
