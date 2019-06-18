package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.view.View;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        // get view for communicating with client
        View view = context.getView();

        // client interaction loop
        for (Map.Entry<Integer, Set<Effect>> entry : mSubexpressions.entrySet()) {
            // prettier names for entry fields
            final Set<Effect> effectsToSelect = entry.getValue();

            // this loops keeps requesting input for the current priority until it is valid
            while (!effectsToSelect.isEmpty()) {
                // if only one non-optional effect is present no user interaction is necessary
                if (effectsToSelect.size() == 1 && !effectsToSelect.iterator().next().isOptional()) {
                    // directly evaluate this effect and go on to next priority
                    discardEvalResult(effectsToSelect.iterator().next().getBehaviour().eval(context));
                    break;
                }

                // user input is requested for optional effect choice AND effect evaluation order
                List<String> selectedEffectIDs = selectEffects(context, mSubexpressions, effectsToSelect);

                // an empty choice is interpreted specially: it corresponds with the will of picking no
                // optional effects. If any non-optional effects are still available, they are offered again
                // with another selection (since they must be picked)
                if (selectedEffectIDs.isEmpty()) {
                    // if all effects are already non-optional, issue a warning to the user
                    if (effectsToSelect.stream().noneMatch(Effect::isOptional))
                        view.showMessage("It is mandatory to select all non-optional effects!");

                    // strip effects to select from optionals
                    effectsToSelect.removeAll(effectsToSelect.stream()
                            .filter(Effect::isOptional)
                            .collect(Collectors.toSet()));
                }

                // once verified, effect IDs are turned into actual effects
                List<Effect> selectedEffects = new ArrayList<>();
                for (String id : selectedEffectIDs) {
                    selectedEffects.add(
                            effectsToSelect.stream()
                                    .filter(eff -> eff.getId().equals(id))
                                    .findFirst()
                                    .orElseThrow(() ->
                                            new InputMismatchException(
                                                    "nonexistent ID in effect selection: " + id + "\n" +
                                                    "Available IDs in requested selection: " + effectsToSelect.stream()
                                                            .map(Effect::getId)
                                                            .collect(Collectors.toList())))
                    );
                }

                // evaluate selected effects
                selectedEffects.stream()
                        .map(Effect::getBehaviour)
                        .forEach(e -> discardEvalResult(e.eval(context)));

                // remove selected effects from effects to select
                effectsToSelect.removeAll(selectedEffects);
            }
        }

        return new Done();
    }
}
