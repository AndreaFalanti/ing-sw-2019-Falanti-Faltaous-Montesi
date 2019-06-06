package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.weapon.behaviour.Done;
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
            // ask user which effect to pick
            View view = context.getView();
            Set<String> selectedEffects = view.selectEffects(mSubexpressions, curPriority);

            // validate user input
            // TODO: actually do this

            // evaluate picked subexpressions
            mSubexpressions.get(curPriority).stream()
                    .filter(eff -> selectedEffects.contains(eff.getId()))
                    .forEach(eff -> discardEvalResult(eff.getBehaviour().eval(context)));
        }

        return new Done();
    }
}
