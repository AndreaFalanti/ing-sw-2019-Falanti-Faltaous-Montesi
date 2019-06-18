package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.view.View;

import java.util.*;

public class PickEffect extends Expression {
    // subexpressions
    private SortedMap<Integer, Set<Effect>> mSubexpressions;

    // maximum priority encountered
    private int mMaximumPriority;

    // trivial constructor
    public PickEffect() {
        mSubexpressions = new TreeMap<>();
        mMaximumPriority = 0;
    }

    /**
     * Add a subexpression
     * @param value subexpression to add
     */
    public void addEffect(Effect value) {
        // update maximum priority
        if (value.getPriority() > mMaximumPriority)
            mMaximumPriority = value.getPriority();

        // initialize priority list if not already there
        mSubexpressions.computeIfAbsent(
                value.getPriority(),
                key -> new HashSet<>()

        );

        // get subexpression list corresponding to the added subexpression's priority
        Set<Effect> correspondingPriorityList = mSubexpressions.get(value.getPriority());

        // add subexpression
        correspondingPriorityList.add(value);
    }

    @Override
    public Expression eval(ShootContext context) {
        for (int curPriority : mSubexpressions.keySet()) {
            Set<Effect> currentSubs = mSubexpressions.get(curPriority);

            // ask user which effect to pick if optional effects are present
            final List<String> selectedEffects = new ArrayList<>();
            if (currentSubs.stream().anyMatch(Effect::isOptional)) {
                selectedEffects.addAll(selectEffects(context, mSubexpressions, curPriority));

                // TODO: validate user input
            }

            // evaluate picked and non-optional subexpressions (non-optionals are executed after optionals
            // in a random order if none is specified
            mSubexpressions.get(curPriority).stream()
                    .filter(eff -> selectedEffects.contains(eff.getId()))
                    .forEach(eff -> discardEvalResult(eff.getBehaviour().eval(context)));
            mSubexpressions.get(curPriority).stream()
                    .filter(eff -> !eff.isOptional() && !selectedEffects.contains(eff.getId()))
                    .forEach(eff -> discardEvalResult(eff.getBehaviour().eval(context)));
        }

        return new Done();
    }
}
