package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.PowerUpCard;
import it.polimi.se2019.model.action.AmmoPayment;
import it.polimi.se2019.util.ArrayUtils;
import it.polimi.se2019.view.View;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    private void manageAmmoPayment(ShootContext context, Set<Effect> effectsToSelect, List<Effect> effectsToPay) {
        AmmoValue totalEffectsCost = effectsToPay.stream()
                .map(Effect::getCost)
                .reduce(new AmmoValue(0, 0, 0), AmmoValue::add);

        context.getView().showMessage(String.format(
                "Paying for following effects: %s (cost: %s)",
                effectsToPay.stream().map(Effect::getId).collect(Collectors.toList()),
                totalEffectsCost
        ));

        context.getInteraction().manageAmmoPayment(
                context.getShooterColor(),
                totalEffectsCost,
                effectsToPay.toString(),
                () -> manageEffectSelection(context, effectsToSelect)
        );
    }

    private void manageEffectSelection(ShootContext context, Set<Effect> effectsToSelect) {
        if (effectsToSelect.isEmpty())
            return;

        // get view for communicating with client
        ShootInteraction interaction = context.getInteraction();
        View view = context.getView();
        // if only one non-optional effect is present no user interaction is necessary
        // TODO: check if this is duplicated inside interaction.selectEffects
        if (effectsToSelect.size() == 1 && !effectsToSelect.iterator().next().isOptional()) {
            // directly evaluate this effect and go on to next priority
            discardEvalResult(effectsToSelect.iterator().next().getBehaviour().eval(context));
            return;
        }

        // user input is requested for optional effect choice AND effect evaluation order
        List<String> selectedEffectIDs = interaction.selectEffects(view, mSubexpressions, effectsToSelect);

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

        // once verified, the selected effect IDs are turned into actual effects
        List<Effect> selectedEffects = new ArrayList<>();
        for (String id : selectedEffectIDs) {
            selectedEffects.add(
                    effectsToSelect.stream()
                            .filter(eff -> eff.getId().equals(id))
                            .findFirst()
                            .orElseThrow(() ->
                                    new InputMismatchException("Illegal effect names!")));
        }

        // manage payment for selected effects
        manageAmmoPayment(context, effectsToSelect, selectedEffects);

        // evaluate selected effects
        selectedEffects.stream()
                .map(Effect::getBehaviour)
                .forEach(e -> discardEvalResult(e.eval(context)));

        // remove selected effects from effects to select and go on selecting
        manageEffectSelection(
                context,
                effectsToSelect.stream()
                        .filter(eff -> !selectedEffects.contains(eff))
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public final Expression eval(ShootContext context) {
        // client interaction loop
        for (Map.Entry<Integer, Set<Effect>> entry : mSubexpressions.entrySet()) {
            final Set<Effect> effectsToSelect = new HashSet<>(entry.getValue());

            manageEffectSelection(context, effectsToSelect);
        }

        return new Done();
    }
}
